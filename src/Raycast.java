import java.awt.*;

public class Raycast {
    Point origin;
    double angle;
    int maxLength = 500;
    int quadrant;
    Color color = new Color((int)(Math.random() * 0x1000000));
    public Raycast(Point origin, double angle) {
        this.origin = origin;
        this.angle = angle;
        if (this.angle < 0) this.angle += 360;
        if (this.angle > 360) this.angle -= 360;
        this.quadrant = getQuadrant();
    }

    public Point getPosition() {
        return origin;
    }

    public double getAngle() {
        return angle;
    }

    public int getMaxLength() {
        return maxLength;
    }

    private double sumValues(double v1, double v2, double v3) {
        return v1 + v2 + v3;
    }

    private double subtractValues(double v1, double v2, double v3) {
        return v1 - v2 - v3;
    }

    private int getQuadrant() {
        return (int) ((angle / (double) 90) % 4) + 1;
    }


    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (origin.x - 6), (int) origin.y - 6, 12, 12);
        g.drawLine((int) origin.x, (int) origin.y, (int) (origin.x - 6 + Math.cos(Math.toRadians(angle)) * this.getMaxLength()), (int) (origin.y + 6 - Math.sin(Math.toRadians(angle)) * this.getMaxLength()));
    }
}