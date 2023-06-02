import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SignedDistanceTest extends JPanel implements ActionListener{
    ArrayList<Shape> shapes = new ArrayList<>();
    int moveSpeed = 4;
    double minSignedDistance = 100;

    //camera
    int fov = 80;
    int cameraAngle = -40;

    Point velocity = new Point();
    ArrayList<Raycast> rays = new ArrayList<>();
    Point point = new Point(200, 200);

    public static void main(String[] args) {
        SignedDistanceTest c = new SignedDistanceTest();

    }

    public void paint(Graphics g) {
        g.fillRect(0, 0, 1200, 800);
        //g.fillOval(10, 10, 100, 100);
        for (Shape circle : shapes) {
            g.setColor(new Color(45, 44, 44));

            g.setColor(Color.BLACK);
            circle.paint(g);
        }

//        for (Raycast ray : rays) {
//            ray.paint(g);
//        }

        //
        //paint signed distance
        Point rayPoint = new Point(point.x, point.y);
        minSignedDistance = rayPoint.signedDistanceToScene(shapes);
        int count = 0;
        while (minSignedDistance > 2 && count < 10) {
            g.drawOval((int) (rayPoint.x - minSignedDistance), (int) (rayPoint.y - minSignedDistance), (int) minSignedDistance * 2, (int) minSignedDistance * 2);
            rayPoint.x += minSignedDistance;
            minSignedDistance = rayPoint.signedDistanceToScene(shapes);
            count++;
        }

        point.paint(g);
    }

    public SignedDistanceTest() {
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

//        for (double rayIndex = cameraAngle - fov/2.0; rayIndex < cameraAngle + fov/2.0; rayIndex += 0.5) {
//            Raycast ray = new Raycast(point, rayIndex);
//            rays.add(ray);
//        }

        int timerInterval = 20;
        Timer t = new Timer(timerInterval, this);
        t.start();

        frame.add(this);
        frame.setVisible(true);

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
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        point.add(velocity);
        System.out.println(point.x);
        repaint();
    }

}
