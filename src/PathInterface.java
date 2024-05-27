import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PathInterface extends Alert {
    private Graph<Place> locationGraph;
    public PathInterface(Place p1, Place p2, PathFinder pathFinder) {
        super(AlertType.INFORMATION);
        this.locationGraph = pathFinder.getLocationGraph();
        setTitle("Message");
        setHeaderText("The Path from "+ p1.getName() + " to " +p2.getName()+":");
        TextArea textField = new TextArea();
        textField.setEditable(false);
        var path = locationGraph.getPath(p1,p2);
        StringBuilder sb= new StringBuilder();
        int totalTime = 0;
        for(Edge<Place> e: path) {
            sb.append(e.toString()+ "\n");
            totalTime+=e.getWeight();
        }
        sb.append("Total "+totalTime);
        textField.setText(sb.toString());

        getDialogPane().setContent(textField);

    }
}
