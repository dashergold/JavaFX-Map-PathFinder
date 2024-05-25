import java.util.Objects;

public class Edge<T> {
    private int weight;
    private String name;
    private T destination;
    public Edge(T destination, String name, int weight) {
        if(weight < 0) {
            throw new IllegalArgumentException("Input can't be negative");
        }
        this.weight = weight;
        this.name = name;
        this.destination = destination;
    }
    public void setWeight(int weight) {
        if(weight < 0) {
            throw new IllegalArgumentException("Input can't be negative");
        }
        this.weight = weight;
    }


    public T getDestination() {
        return destination;
    }
    public int getWeight() {
        return weight;
    }
    public String getName() {
        return name;
    }
    //todo equals method
    public boolean equals(Object other) {
        if(other instanceof Edge<?> edge) {
            return destination.equals(edge.destination);
        }
        return false;
    }
    public int hashCode() {
        return Objects.hash(destination, name);
    }
    public String toString() {
        return "till " + destination +" med "+ name +" tar "+ weight;
    }
}
