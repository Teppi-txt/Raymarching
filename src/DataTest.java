import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataTest {
    ArrayList<Shape> shapes = new ArrayList<>();

    //camera
    int fov = 80;
    int cameraAngle = -40;
    double minSignedDistance;
    Point point = new Point(200, 200);

    BufferedImage frame;

    public static void main(String[] args) throws IOException {
        DataTest run = new DataTest();
    }
    public BufferedImage renderImageFromRays(int outputWidth, int outputHeight, ArrayList<Raycast> rays, int camAngle) {
        BufferedImage render = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = (Graphics2D) render.getGraphics();
        canvas.setColor(Color.LIGHT_GRAY);
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
            while (minSignedDistance > 2 && distanceToIntersection < ray.maxLength) {
                //for horizontal movement, if quadrant 1 or 4, plus, else minus
                rayPoint.x += Math.cos(Math.toRadians(ray.angle)) * minSignedDistance;
                rayPoint.y += Math.sin(Math.toRadians(ray.angle)) * minSignedDistance;
                minSignedDistance = rayPoint.signedDistanceToScene(shapes);
                distanceToIntersection += minSignedDistance;
            }
            //System.out.println(distanceToIntersection);
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

    public DataTest() throws IOException {

        //create scene
        for (int i = 0; i < 5; i++) {
            Point center = new Point(Math.random() * 1200, Math.random() * 800);
            shapes.add(new Circle(center, Math.random() * 100));
        }
        for (int i = 0; i < 5; i++) {
            Point center = new Point(Math.random() * 1200, Math.random() * 800);
            shapes.add(new Rect(center, Math.random() * 100, Math.random() * 100));
        }

        //thinking of a way to generate the scene using a code
        //circle needs 3 values, rect needs 4 values
        //c-1-1-1|r-1-1-1-1
        //c --> circle, -1-1 --> center point. -1 --> radius
        long startTimeMilli = System.currentTimeMillis();
        for (int i = 0; i < 360; i++) {
            ArrayList<Raycast> rays = new ArrayList<>();
            //create the set of rays for the camera
            for (double rayIndex = i - fov/2.0; rayIndex < i + fov/2.0; rayIndex += 0.5) {
                Raycast ray = new Raycast(point, rayIndex);
                rays.add(ray);
            }

            frame = renderImageFromRays(1200, 800, rays, i);
            File renderPath = new File("output/render" + i + ".png");
            ImageIO.write(frame, "png", renderPath);

        }
        System.out.println("Process finished in: " + (System.currentTimeMillis() - startTimeMilli) + " ms");
    }
}
