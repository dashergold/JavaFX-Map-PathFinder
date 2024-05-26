import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ConnectionInterface  extends Dialog<ConnectionInterface.PlaceResult> {
    public ConnectionInterface(Place p1, Place p2)  {
        setTitle("Connection");
        setHeaderText("Connection from " +p1.getName() + " to " +p2.getName());
        ButtonType okButtonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        getDialogPane().getButtonTypes().addAll(okButtonType,ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField nameField = new TextField();
        TextField timeField = new TextField();
        grid.add(new Label("Name:"),0,0);
        grid.add(nameField,1,0);
        grid.add(new Label("Time:"),0,1);
        grid.add(timeField,1,1);
        getDialogPane().setContent(grid);
        Button okButton = (Button) getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        nameField.textProperty().addListener((obs, oldV, newV) ->
                okButton.setDisable(newV.trim().isEmpty() || !isValidTime(timeField.getText()))
        );
        timeField.textProperty().addListener((obs, oldV, newV) ->
                okButton.setDisable(newV.trim().isEmpty() || !isValidTime(newV))
        );
        setResultConverter(dialogButton -> {
            if(dialogButton == okButtonType) {
                return new PlaceResult(nameField.getText(), Integer.parseInt(timeField.getText()));
            }
            return null;
        });
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
