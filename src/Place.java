import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Place extends Circle {
    String name;
    double x, y;
    public Place(double x, double y, String name) {
        super(x,y,10);
        this.x = x;
        this.y = y;
        this.name = name;
        super.setFill(Color.BLUE);

    }
    public String getName() {
        return this.name;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void paintSelected() {
        this.setFill(Color.RED);
    }
    public void paintUnselected() {
        this.setFill(Color.BLUE);
    }
    public Group getDisplay() {
        Text text = new Text(this.x+5,this.y+20,this.name);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Group group = new Group();
        group.getChildren().addAll(this,text);
        return group;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
