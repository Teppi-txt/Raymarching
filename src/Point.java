import java.awt.*;
import java.util.ArrayList;

public class Point {
    public double x;
    public double y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point max(Point point, Point point2) {
        return new Point(Math.max(point.x, point2.x), Math.max(point.y, point2.y));
    }

    public static Point min(Point point, Point point2) {
        return new Point(Math.min(point.x, point2.x), Math.min(point.y, point2.y));
    }

    public static Point abs(Point point) {
        return new Point(Math.abs(point.x), Math.abs(point.y));
    }

    public double distanceToPoint(Point point) {
        return Math.sqrt(Math.pow(Math.abs(point.x - this.x), 2) + Math.pow(Math.abs(point.y - this.y), 2));
    }
    public double signedDistanceToScene(ArrayList<Shape> scene) {
        double minSignedDistance = Double.MAX_VALUE;
        for (Shape shape : scene) {
            double distance = shape.signedDistance(this);
            if (distance < minSignedDistance) minSignedDistance = distance;
        }
        return minSignedDistance;
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)(this.x - 5), (int)(this.y - 5), 10, 10);
//        g.setColor(Color.BLACK);
//        g.drawOval((int)(this.x - 5), (int)(this.y - 5), 10, 10);
    }

    public void add(Point p2) {
        this.x += p2.x;
        this.y += p2.y;
    }

    public void subtract(Point p2) {
        this.x -= p2.x;
        this.y -= p2.y;
    }
}
