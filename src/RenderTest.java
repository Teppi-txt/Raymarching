import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RenderTest extends JPanel implements ActionListener{
    ArrayList<Shape> shapes = new ArrayList<>();
    int moveSpeed = 4;
    double minSignedDistance = 100;

    //camera
    int fov = 80;
    int cameraAngle = -40;

    boolean spectate = true;
    Point velocity = new Point();
    ArrayList<Raycast> rays = new ArrayList<>();
    Point point = new Point(200, 200);

    BufferedImage frame = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);

    public static void main(String[] args) {
        RenderTest c = new RenderTest();
    }

    public BufferedImage renderImageFromRays(int outputWidth, int outputHeight, ArrayList<Raycast> rays, int camAngle) {
        BufferedImage render = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = (Graphics2D) render.getGraphics();
        canvas.setColor(Color.ORANGE);
        canvas.fillRect(0, 0, outputWidth, outputHeight);
        int horizontalIncrement = outputWidth/(rays.size());

        for (int rayIndex = 0; rayIndex < rays.size(); rayIndex++) {
            Raycast ray = rays.get(rays.size() - rayIndex - 1);
            //gets the closest vertical and horizontal intersection

            //calculates distance to intersection
            //paint signed distance
            Point rayPoint = new Point(point.x, point.y);
            minSignedDistance = rayPoint.signedDistanceToScene(shapes);
            double distanceToIntersection = 0;
            int quadrant = ray.quadrant;
            while (minSignedDistance > 2 && distanceToIntersection < ray.maxLength) {
                //for horizontal movement, if quadrant 1 or 4, plus, else minus
                rayPoint.x += Math.cos(Math.toRadians(ray.angle)) * minSignedDistance;
                rayPoint.y -= Math.sin(Math.toRadians(ray.angle)) * minSignedDistance;
                minSignedDistance = rayPoint.signedDistanceToScene(shapes);
                distanceToIntersection += minSignedDistance;
            }
            System.out.println(distanceToIntersection);
            distanceToIntersection = Math.min(distanceToIntersection, ray.maxLength);

            //find the correct bar color for that strip
            double lengthToMaxLength = distanceToIntersection/ray.maxLength;
            Color segmentColor = lerpColor(new Color[]{Color.white, Color.black}, lengthToMaxLength);
            canvas.setColor(segmentColor);

            //witchcraft?
            double angleDif = ((camAngle) - (ray.angle));
            if (angleDif < 0) angleDif += 360;
            if (angleDif > 360) angleDif -= 360;

            double renderBoxHeight = (outputHeight * 100)/(distanceToIntersection * Math.cos(Math.toRadians(angleDif))); //witchcraft
            //assert !closestIntersection.equals(new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
            canvas.fillRect(rayIndex * horizontalIncrement, (int) (outputHeight/2 - renderBoxHeight/2), horizontalIncrement, (int) (renderBoxHeight));
        }
        return render;
    }

    public Color lerpColor(Color[] colorRamp, double i) {
        double valueBetweenColors = 1/(double)colorRamp.length - 2;
        int lowerColorIndex = (int)Math.floor(Math.abs(i/valueBetweenColors));
        int higherColorIndex = (int)Math.ceil(Math.abs(i/valueBetweenColors));
        double amount = Math.abs(i);
        //System.out.println(amount);

        double r = colorRamp[lowerColorIndex].getRed() + ((colorRamp[higherColorIndex].getRed() - colorRamp[lowerColorIndex].getRed()) * amount);
        double b = colorRamp[lowerColorIndex].getBlue() + ((colorRamp[higherColorIndex].getBlue() - colorRamp[lowerColorIndex].getBlue()) * amount);
        double g = colorRamp[lowerColorIndex].getGreen() + ((colorRamp[higherColorIndex].getGreen() - colorRamp[lowerColorIndex].getGreen()) * amount);
        return new Color((int) r, (int) g, (int) b);
        //return Color.BLUE;
    }

    public void paint(Graphics g) {
        frame = renderImageFromRays(1200, 800, rays, cameraAngle);
        if (spectate) {
            g.drawImage(frame, 0, 0, this);
        } else {
            g.fillRect(0, 0, 1200, 800);
            //g.fillOval(10, 10, 100, 100);
            for (Shape circle : shapes) {
                g.setColor(new Color(45, 44, 44));

                g.setColor(Color.BLACK);
                circle.paint(g);
            }

        for (Raycast ray : rays) {
            ray.paint(g);
        }

            //
            //paint signed distance
            Point rayPoint = new Point(point.x, point.y);
            minSignedDistance = rayPoint.signedDistanceToScene(shapes);
            int count = 0;
            int angle = -50;
            while (minSignedDistance > 2 && count < 10) {
                g.drawOval((int) (rayPoint.x - minSignedDistance), (int) (rayPoint.y - minSignedDistance), (int) minSignedDistance * 2, (int) minSignedDistance * 2);
                rayPoint.x += Math.cos(angle) * minSignedDistance;
                rayPoint.y += Math.sin(angle) * minSignedDistance;
                minSignedDistance = rayPoint.signedDistanceToScene(shapes);
                count++;
            }

            point.paint(g);
        }
    }

    public RenderTest() {
        JFrame frame = new JFrame("Window");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        for (int i = 0; i < 5; i++) {
            Point center = new Point(Math.random() * 1200, Math.random() * 800);
            shapes.add(new Circle(center, Math.random() * 100));
        }
        for (int i = 0; i < 5; i++) {
            Point center = new Point(Math.random() * 1200, Math.random() * 800);
            shapes.add(new Rect(center, Math.random() * 100, Math.random() * 100));
        }

        //create the set of rays for the camera
        for (double rayIndex = cameraAngle - fov/2.0; rayIndex < cameraAngle + fov/2.0; rayIndex += 0.5) {
            Raycast ray = new Raycast(point, rayIndex);
            rays.add(ray);
        }

        //update the display panel
        int timerInterval = 20;
        Timer t = new Timer(timerInterval, this);
        t.start();

        //setup
        frame.add(this);
        frame.setVisible(true);

        //input handling for movement
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_RIGHT)
                    velocity.x = moveSpeed;
                else if(e.getKeyCode()== KeyEvent.VK_LEFT)
                    velocity.x = -moveSpeed;
                else if(e.getKeyCode()== KeyEvent.VK_DOWN)
                    velocity.y = moveSpeed;
                else if(e.getKeyCode()== KeyEvent.VK_UP)
                    velocity.y = -moveSpeed;
                else if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
                    spectate = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_RIGHT)
                    velocity.x = 0;
                else if(e.getKeyCode()== KeyEvent.VK_LEFT)
                    velocity.x = 0;
                else if(e.getKeyCode()== KeyEvent.VK_DOWN)
                    velocity.y = 0;
                else if(e.getKeyCode()== KeyEvent.VK_UP)
                    velocity.y = 0;
                else if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
                    spectate = false;
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        point.add(velocity);
        repaint();
    }
}
