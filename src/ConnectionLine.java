import javafx.scene.shape.Line;

public class ConnectionLine extends Line {

    public ConnectionLine(double startX, double startY, double endX, double endY) {
        super(startX,startY,endX,endY);
        setStrokeWidth(3);

    }
}
