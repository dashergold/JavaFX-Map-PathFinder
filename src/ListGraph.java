import java.util.*;

public class ListGraph<T> implements Graph<T>{
    private final Map<T, Set<Edge<T>>> graph = new HashMap<>();


    public void add(T node) {
        graph.putIfAbsent(node, new HashSet<>());
    }
    public void remove(T node) {
        if(!graph.containsKey(node)) {
            throw new NoSuchElementException("Couldn't find node: " +node);
        }
        Set<Edge<T>> connections = new HashSet<>(graph.get(node));


        for(Edge<T> e: connections) {
            disconnect(node, e.getDestination());
        }
        graph.put(node, connections);

        graph.remove(node);

    }
    public void connect(T node1, T node2, String name, int weight) {
        if((!graph.containsKey(node1) || !graph.containsKey(node2)))  {
            throw new NoSuchElementException("Couldn't find node in map");
        }

        Edge<T> n1Connection = new Edge<>(node2, name, weight);
        Edge<T> n2Connection = new Edge<>(node1, name, weight);
        for(Edge<T> e: graph.get(node1)) {
            if(e.equals(n1Connection)) {
                throw new IllegalStateException("nodes already have an edge");
            }
        }
        Set<Edge<T>> fromConnections = graph.get(node1);
        Set<Edge<T>> toConnections = graph.get(node2);
        fromConnections.add(n1Connection);
        toConnections.add(n2Connection);
        graph.put(node1, fromConnections);
        graph.put(node2, toConnections);



    }
    public void disconnect(T node1, T node2) {
        if((!graph.containsKey(node1) || !graph.containsKey(node2)))  {
            throw new NoSuchElementException("Couldn't find node in map");
        }
        if(getEdgeBetween(node1, node2) == null) {
            throw new IllegalStateException("no edge between the nodes");
        }

        Set<Edge<T>> n1Edges = graph.get(node1);
        Set<Edge<T>> n2Edges = graph.get(node2);
        n1Edges.remove(getEdgeBetween(node1, node2));
        graph.put(node1, n1Edges);
        n2Edges.remove(getEdgeBetween(node2,node1));
        graph.put(node2,n2Edges);



    }
    public void setConnectionWeight(T node1, T node2, int weight) {
        if((!graph.containsKey(node1) || !graph.containsKey(node2)))  {
            throw new NoSuchElementException("Couldn't find node in map");
        }
        getEdgeBetween(node1,node2).setWeight(weight);
        getEdgeBetween(node2,node1).setWeight(weight);
    }
    public Set<T> getNodes() {
        HashSet<T> nodes = new HashSet<>();
        graph.keySet().forEach(node -> nodes.add(node));
        return nodes;
    }
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if(!graph.containsKey(node)) {
            throw new NoSuchElementException("no node " + node + " in the graph");
        }
        return graph.get(node);
    }
    public Edge<T> getEdgeBetween(T node1, T node2) {
        if(!graph.containsKey(node2)) {
            throw new NoSuchElementException("no node " + node2 + " in the graph");
        }
        Collection<Edge<T>> n1Edges = getEdgesFrom(node1);
        for(Edge<T> e: n1Edges) {
            if(e.getDestination().equals(node2)) {
                return e;
            }
        }
        return null;


    }
    public String toString() {
        return graph.toString();
    }
    public boolean pathExists(T from, T to) {
        if((!graph.containsKey(from) || !graph.containsKey(to)))  {
            return false;
        }
        Set<T> visited = new HashSet<>();
        return recursiveVisit(from,to,visited);
    }
    private boolean recursiveVisit(T from, T destination, Set<T> visited) {
        visited.add(from);
        if(from.equals(destination)) {
            return true;
        }
        for(Edge<T> e: graph.get(from)) {
            if(!visited.contains(e.getDestination())) {
                if(recursiveVisit(e.getDestination(),destination,visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Edge<T>> getPath(T from, T to) {
        if(!pathExists(from, to)) {
            return null;
        }
        LinkedList<LinkedList<Edge<T>>> paths = new LinkedList<>();
        for(Edge<T> e: getEdgesFrom(from)) {
            var path = new LinkedList<Edge<T>>();
            path.add(e);
            paths.add(path);
        }
        //paths.addLast( new LinkedList<>(getEdgesFrom(from)));
        HashSet<T> visited = new HashSet<>();
        var result = BFS(to, paths, visited);
        return result;


    }
    private List<Edge<T>> BFS (T finalDestination, LinkedList<LinkedList<Edge<T>>> activePaths, HashSet<T> visited) {
        while (!activePaths.isEmpty()) {
            LinkedList<Edge<T>> path = activePaths.getFirst();
            activePaths.removeFirst();
            T pathHead = path.getLast().getDestination();
            if(pathHead.equals(finalDestination)) {
                return path;
            }
            if(visited.contains(pathHead)) {
                continue;
            }
            visited.add(pathHead);
            for(Edge<T> neighbor : getEdgesFrom(pathHead)) {
                LinkedList<Edge<T>> extendedPath = new LinkedList<>(path);
                extendedPath.addLast(neighbor);
                activePaths.addLast(extendedPath);
            }
        }
        return null;
    }


}
