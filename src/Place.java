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
        this.setId(name);

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
        System.out.println("Painting " + this.getId() + " as selected (RED).");
        this.setFill(Color.RED);
    }
    public void paintUnselected() {
       // System.out.println("Painting " + this.getId() + " as unselected (BLUE).");
        this.setFill(Color.BLUE);
    }
    @Override
    public String toString() {
        return this.name;
    }
}
