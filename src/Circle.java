import java.awt.*;

public class Circle extends Shape{
    Point center;
    double radius;

    public Circle() {
        this.center = new Point();
        this.radius = 1;
    }

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)(this.center.x - radius), (int)(this.center.y - radius), (int) radius * 2, (int) radius * 2);
        center.paint(g);

        g.setColor(Color.RED);
        //g.fillOval((int)(sensor.x - 3), (int)(sensor.y - 3), 6, 6);
    }

    public double signedDistance(Point point) {
        return point.distanceToPoint(this.center) - this.radius;
    }
}
