import java.awt.*;

public class Shape {

    public Point center = new Point();

    public void paint(Graphics g) {
        System.out.println("tried painting null shape");
    }

    public double signedDistance(Point point) {
        return -1;
    }
}
