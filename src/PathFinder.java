import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jdk.jshell.spi.ExecutionControl;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class PathFinder extends Application {
    private Graph<Place> locationGraph = new ListGraph<>();
    private Menu menu = new Menu("File");
    private Pane center = new Pane();
    private BorderPane root;
    private boolean changed;
    private final double sceneWidth = 600;
    private final double sceneHeight = 100;
    private Stage stage;
    private Image map;
    private Scene scene;
    private Button findPath;
    private Button showConnection;
    private Button newPlace;
    private Button newConnection;
    private Button changeConnection;
    private Place p1 = null;
    private Place p2 = null;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        root = new BorderPane();
        FlowPane tools = new FlowPane(Orientation.VERTICAL);
        root.setTop(tools);
        root.setCenter(center);
        root.setStyle("-fx-font-size: 14;");
        scene = new Scene(root,sceneWidth,sceneHeight);

        tools.setMaxHeight(sceneHeight);
        tools.setMaxWidth(sceneWidth);
        //tools.setStyle("-fx-background-color: red");
        center.setId("outputArea");

        menu.setId("menuFile");
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menu");
        menuBar.getMenus().add(menu);
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        MenuItem newMapItem = new MenuItem("New Map");
        newMapItem.setOnAction(event -> {
            if(!changed) {
                openImage("file:europa.gif");
            }
            else {
                if(unsavedChangesWarning("Unsaved changes, continue anyway?")) {
                    changed = false;
                    openImage("file:europa.gif");
                }
            }
        });
        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(event -> {
            if(!changed) {
                openFile("europa.graph");
                //openFile("saveTest.txt");
            }
            else {
                if(unsavedChangesWarning("Unsaved changes, continue anyway?")) {
                    changed=false;
                    openFile("europa.graph");
                }
            }
        });
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(this::saveLocationMap);
        MenuItem saveImageItem = new MenuItem("Save Image");
        saveImageItem.setOnAction(event -> {
            WritableImage image = new WritableImage((int)map.getWidth(), (int)map.getHeight());
            center.snapshot(new SnapshotParameters(), image);
            File file = new File("capture.png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",file);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> {
            if (!changed) {
                System.exit(0);
            }
            if (unsavedChangesWarning("Unsaved changes, exit anyway?")) {
                System.exit(0);
            }
        });
        menu.getItems().addAll(newMapItem,openItem,saveItem,saveImageItem,exitItem);
        newMapItem.setId("menuNewMap");
        openItem.setId("menuOpenFile");
        saveItem.setId("menuSaveFile");
        saveImageItem.setId("menuSaveImage");
        exitItem.setId("menuExit");

        FlowPane buttons = new FlowPane();
        findPath = new Button("Find Path");
        showConnection = new Button("Show Connection");
        showConnection.setOnAction(event -> {
            if(p1 != null && p2 != null) {
                if(locationGraph.getEdgeBetween(p1,p2) == null) {
                    error("No connection between " + p1.getName() + " and "+p2.getName()+"!");
                }
                else {
                    displayConnection();
                }
            }
            else {
                error("Two places must be selected!");
            }
        });
        newPlace = new Button("New Place");
        newPlace.setOnAction(event -> {
            scene.setCursor(Cursor.CROSSHAIR);
            newPlace.setDisable(true);
            center.setOnMouseClicked(new NewPlaceHandler());
        });
        newConnection = new Button("New Connection");
        newConnection.setOnAction(event -> {
            if(p1 != null && p2 != null) {
                createConnection(p1,p2);
            }
            else {
                error("Two places must be selected!");
            }
        });
        changeConnection = new Button("Change Connection");
        findPath.setId("btnFindPath");
        showConnection.setId("btnShowConnection");
        newPlace.setId("btnNewPlace");
        changeConnection.setId("btnChangeConnection");
        newPlace.setId("btnNewConnection");
        buttons.setAlignment(Pos.CENTER);
        //buttons.setPadding(new Insets(5));
        buttons.setHgap(5);
        buttons.getChildren().addAll(findPath,showConnection,newPlace,newConnection,changeConnection);
        tools.getChildren().addAll(menuBar, buttons);

        stage.setScene(scene);
        stage.setTitle("PathFinder");
        stage.setOnCloseRequest(event -> {
            if(changed) {
                if(!unsavedChangesWarning("Unsaved changes, exit anyway?")) {
                    event.consume();
                }
            }
        });
        stage.show();
    }
    private void displayConnection() {
        ConnectionInterface ci = new ConnectionInterface(p1,p2,this);
        ci.showAndWait();
    }
    private void createConnection(Place p1, Place p2) {
        if(locationGraph.getEdgeBetween(p1,p2) == null) {
            ConnectionInterface dialog = new ConnectionInterface(p1, p2);
            Optional<ConnectionInterface.PlaceResult> res = dialog.showAndWaitResult();
            if (res.isPresent()) {
                ConnectionInterface.PlaceResult pr = res.get();
                locationGraph.connect(p1, p2, pr.getName(), pr.getTime());
                ConnectionLine c = new ConnectionLine(p1.getCenterX(), p1.getCenterY(), p2.getCenterX(), p2.getCenterY());
                center.getChildren().add(c);
            }
        } else {
            error(p1.getName()+ " and " + p2.getName() + " are already connected!");
        }

    }
    private void error(String content) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error!");
        error.setHeaderText(null);
        error.setContentText(content);
        error.showAndWait();
    }


    private class PlaceClickedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Place p = (Place)event.getSource();
            if (p == p1) {
                p1.paintUnselected();
                p1 = null;
            } else if (p == p2) {
                p2.paintUnselected();
                p2 = null;
            } else {
                if(p1 == null) {
                    p1 = p;
                    p.paintSelected();
                }
                else if(p2 == null) {
                    p2 = p;
                    p.paintSelected();
                }
            }
        }
    }
    private class NewPlaceHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            //todo fix the cancel button work PLEASEXDDD
            double x = event.getX();
            double y = event.getY();
            newPlace(x,y);
            scene.setCursor(Cursor.DEFAULT);
            newPlace.setDisable(false);
            center.setOnMouseClicked(null);
        }
    }
    private void newPlace(double x, double y) {
        TextInputDialog tid = new TextInputDialog();
        tid.setHeaderText(null);
        tid.setTitle("Name");
        tid.setContentText("Name of place:");
        DialogPane dp = tid.getDialogPane();
        Button okButton = (Button) dp.lookupButton(ButtonType.OK);
        TextField tf = tid.getEditor();
        tf.textProperty().addListener((obs, oldV, newV) -> okButton.setDisable(newV.trim().isEmpty()));
        okButton.setDisable(tf.getText().trim().isEmpty());
        Optional<String> res =  tid.showAndWait();
        if(res.isPresent()) {
            Place p =  new Place(x,y,tid.getEditor().getText());
            p.setOnMouseClicked(new PlaceClickedHandler());
            center.getChildren().addAll(p);
            locationGraph.add(p);
            p.setId(p.getName());
            changed = true;
        }
    }

    private void openFile(String fileName) {
        locationGraph = new ListGraph<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String mapData = reader.readLine();
            openImage(mapData);
            String locationData = reader.readLine();
            String[] data = locationData.split(";");
            for(int i = 0; i < data.length-1; i+=3) {
                String name = data[i];
                double x = Double.parseDouble(data[i+1]);
                double y = Double.parseDouble(data[i+2]);
                Place p = new Place(x,y,name);
                p.setOnMouseClicked(new PlaceClickedHandler());
                locationGraph.add(p);
                p.setId(p.getName());
                center.getChildren().add(p);
            }
            String line;
            while ((line = reader.readLine()) != null){
                String[] edgeData = line.split(";");
                Place from = null, to = null;
                for(Place p: locationGraph.getNodes()) {
                    if(p.getName().equals(edgeData[0])) {
                        from = p;
                    }
                    if(p.getName().equals(edgeData[1])) {
                        to = p;
                    }
                }
                if(locationGraph.getEdgeBetween(from,to) == null) {
                    locationGraph.connect(from, to, edgeData[2], Integer.parseInt(edgeData[3]));
                    center.getChildren().add(new ConnectionLine(from.getCenterX(),from.getCenterY(),to.getCenterX(),to.getCenterY()));
                }
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            //todo add alerts
            e.printStackTrace();
        }
        catch (IOException e) {
            //todo add alerts
            e.printStackTrace();
        }
    }
    private void openImage(String image) {
        center.getChildren().clear();
        map = new Image(image);
        ImageView imageView = new ImageView(map);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        center.getChildren().add(imageView);
        stage.setWidth(map.getWidth()+20);
        stage.setHeight(map.getHeight()+sceneHeight+40);
    }
    private void saveLocationMap(ActionEvent event) {
        try{
            //todo change output file later
            if(map != null) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("saveTest.txt"));
                writer.write(map.getUrl()+"\n");
                Iterator<Place> it = locationGraph.getNodes().iterator();
                while(it.hasNext()) {
                    Place p = it.next();
                    writer.write(p.getName() +";"+p.getCenterX()+";"+p.getCenterY());
                    if(it.hasNext()) {
                        writer.write(";");
                    }
                }
                Map<Place,Collection<Edge<Place>>> destinationEdges = getConnections();
                for(Place p: destinationEdges.keySet()) {
                    Collection<Edge<Place>> edges = destinationEdges.get(p);
                    for(Edge<Place> e: edges) {
                        writer.write("\n"+p.getName()+";"+e.getDestination().getName()+";"+e.getName()+";"+e.getWeight());
                    }
                }
                changed = false;
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Map<Place, Collection<Edge<Place>>> getConnections() {
        Map<Place, Collection<Edge<Place>>> result = new HashMap<>();
        Set<Place> nodes = locationGraph.getNodes();
        for(Place p: nodes) {
            Collection<Edge<Place>> edges = locationGraph.getEdgesFrom(p);
            result.put(p,edges);
        }
        return result;
    }
    private boolean unsavedChangesWarning(String content) {
        Alert unsavedChangeWarning = new Alert(Alert.AlertType.CONFIRMATION);
        unsavedChangeWarning.setTitle("Warning!");
        unsavedChangeWarning.setHeaderText(null);
        unsavedChangeWarning.setContentText(content);
        Optional<ButtonType> res = unsavedChangeWarning.showAndWait();
        if(res.isPresent() && res.get().equals(ButtonType.CANCEL)) {
            return false;
        }
        else {
            return true;
        }
    }
    public Graph<Place> getLocationGraph() {
        return this.locationGraph;
    }




}
