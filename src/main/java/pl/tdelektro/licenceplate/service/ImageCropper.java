package pl.tdelektro.licenceplate.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageCropper {


    public static String croppImage(String imagePath, String vertices){

        try {
            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Parse the JSON string containing the vertices
            List<Float> xCoordinates = new ArrayList<>();
            List<Float> yCoordinates = new ArrayList<>();

            // Extract x and y coordinates using regex
            Pattern pattern = Pattern.compile("x: (\\d+\\.?\\d*)\\ny: (\\d+\\.?\\d*)");
            Matcher matcher = pattern.matcher(vertices);
            while (matcher.find()) {
                xCoordinates.add(Float.parseFloat(matcher.group(1)));
                yCoordinates.add(Float.parseFloat(matcher.group(2)));
            }

            // Find the minimum and maximum coordinates
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = Float.MIN_VALUE;
            float maxY = Float.MIN_VALUE;
            for (float x : xCoordinates) {
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
            }
            for (float y : yCoordinates) {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }

            // Define the cropping rectangle
            Rectangle croppingRect = new Rectangle((int) (minX * image.getWidth()), (int) (minY * image.getHeight()),
                    (int) ((maxX - minX) * image.getWidth()), (int) ((maxY - minY) * image.getHeight()));

            // Create a BufferedImage for the cropped image
            BufferedImage croppedImage = new BufferedImage(croppingRect.width, croppingRect.height, BufferedImage.TYPE_INT_RGB);

            // Create a Graphics2D object and draw the cropped portion of the image onto the new BufferedImage
            Graphics2D g2d = croppedImage.createGraphics();
            g2d.drawImage(image.getSubimage(croppingRect.x, croppingRect.y, croppingRect.width, croppingRect.height), 0, 0, null);
            g2d.dispose();

            // Save the cropped image
            File outputFile = new File("src/main/resources/cropped_image.jpg");
            ImageIO.write(croppedImage, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "src/main/resources/cropped_image.jpg";
    }

}
