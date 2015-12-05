import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Main {
    public static void main(String[] args) throws IOException {
        int[][] kernel = ImageProcessor.generateKernel(3,1,1);
        String inputBig = "C:\\Users\\Gebruiker\\Documents\\Filter\\inputBig.png";
        String input = "C:\\Users\\Gebruiker\\Documents\\Filter\\input.png";
        String outputPath = "C:\\Users\\Gebruiker\\Documents\\Filter\\output.png";

        BufferedImage image = getImage(inputBig);

//        BufferedImage imageOut = ImageProcessor.blur(image, kernel);
        BufferedImage imageOut = ImageProcessor.greyScale(image);
        imageOut = ImageProcessor.sobel(imageOut);
        File output = new File(outputPath);
        ImageIO.write(imageOut, "png", output);
    }

    public static BufferedImage getImage(String path) throws IOException {
        File input = new File(path);
        return ImageIO.read(input);
    }
}
