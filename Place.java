import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Place extends Circle {
    String name;
    double x, y;
    //todo add name in the bottom right of the place item.
    public Place(double x, double y, String name) {
        super(x,y,10);
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
}
