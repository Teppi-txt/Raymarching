import java.awt.*;

public class Rect extends Shape{
    Point center;
    double width;
    double height;

    public Rect() {
        this.center = new Point();
        this.width = 1;
        this.height = 1;
    }

    public Rect(Point center, double width, double height) {
        this.center = center;
        this.width = width;
        this.height = height;
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect((int) (center.x - width/2), (int) (center.y - height/2), (int) width, (int) height);

        g.setColor(Color.RED);
        //g.fillOval((int)(sensor.x - 3), (int)(sensor.y - 3), 6, 6);
    }

    public double signedDistance(Point point) {
        double horizOffset = Math.abs(center.x - point.x) - width/2;
        double vertOffset = Math.abs(center.y - point.y) - height/2;
        return Math.sqrt(Math.pow(Math.max(horizOffset, 0), 2) + Math.pow(Math.max(vertOffset, 0), 2));
    }
}
