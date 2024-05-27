import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ConnectionInterface extends Alert{
    private TextField nameField;
    private TextField timeField;
    private Graph<Place> locationGraph;

    public ConnectionInterface(Place p1, Place p2)  {
        super(AlertType.CONFIRMATION);
        setTitle("Connection");
        setHeaderText("Connection from " +p1.getName() + " to " +p2.getName());


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        nameField = new TextField();
        timeField = new TextField();
        grid.add(new Label("Name:"),0,0);
        grid.add(nameField,1,0);
        grid.add(new Label("Time:"),0,1);
        grid.add(timeField,1,1);
        getDialogPane().setContent(grid);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        nameField.textProperty().addListener((obs, oldV, newV) ->
                okButton.setDisable(newV.trim().isEmpty() || !isValidTime(timeField.getText()))
        );
        timeField.textProperty().addListener((obs, oldV, newV) ->
                okButton.setDisable(newV.trim().isEmpty() || !isValidTime(newV))
        );
    }
    public ConnectionInterface(Place p1, Place p2, PathFinder pathFinder) {
        super(AlertType.CONFIRMATION);
        this.locationGraph = pathFinder.getLocationGraph();
        Edge<Place> edge = locationGraph.getEdgeBetween(p1,p2);
        setTitle("Connection");
        setHeaderText("Connection from " +p1.getName() + " to " +p2.getName());
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        nameField = new TextField(edge.getName());
        nameField.setEditable(false);
        timeField = new TextField(String.valueOf(edge.getWeight()));
        timeField.setEditable(false);
        grid.add(new Label("Name:"),0,0);
        grid.add(nameField,1,0);
        grid.add(new Label("Time:"),0,1);
        grid.add(timeField,1,1);
        getDialogPane().setContent(grid);


    }
    public Optional<PlaceResult> showAndWaitResult() {
        Optional<ButtonType> result = showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return Optional.of(new PlaceResult(nameField.getText(), Integer.parseInt(timeField.getText())));
        }
        return Optional.empty();
    }
    private boolean isValidTime(String time) {
        if(time == null || time.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(time);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    public class PlaceResult {
        private String name;
        private int time;
        public PlaceResult(String name, int time) {
            this.name = name;
            this.time = time;
        }
        public String getName() {
            return name;
        }
        public int getTime() {
            return time;
        }
    }
}
