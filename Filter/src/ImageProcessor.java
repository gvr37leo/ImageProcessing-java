import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ImageProcessor {

    public static BufferedImage sobel(BufferedImage image){
        Color[][] tempimage = new Color[image.getHeight()][image.getWidth()];
        int[][] xkernel = {
                {-1,0,1},
                {-2,0,2},
                {-1,0,1}
        };
        int[][] ykernel = {
                {1,2,1},
                {0,0,0},
                {-1,-2,-1}
        };
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                Position[][] grid = getGrid(x,y,3,3,image);
                int xdir = combineGrid(grid, xkernel, image);
                int ydir = combineGrid(grid, ykernel, image);

                int C = (int)(Math.pow(Math.pow(xdir, 2) + Math.pow(ydir, 2), 0.5));
                int grayColor = (int)map(C, 0, 1200, 0, 255);
                tempimage[y][x] = new Color(grayColor, grayColor, grayColor);

            }
        }
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                image.setRGB(x, y, tempimage[y][x].getRGB());
            }
        }

        return image;
    }

    public static int combineGrid(Position[][] grid, int[][] kernel, BufferedImage image){
        int total = 0;
        for (int gridx = 0; gridx < grid.length; gridx++) {
            for (int gridy = 0; gridy < grid[0].length; gridy++) {
                if (grid[gridy][gridx] != null && kernel[gridy][gridx] != 0) {
                    Color pixel = new Color(image.getRGB(grid[gridy][gridx].x, grid[gridy][gridx].y));
                    total += pixel.getRed() * kernel[gridy][gridx];
                }
            }
        }
        return total;
    }

    public static BufferedImage blur(BufferedImage image, int[][] kernel) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Position[][] grid = getGrid(x, y, kernel.length, kernel[0].length, image);
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                int totalMultiplication = 0;
                for (int gridx = 0; gridx < grid.length; gridx++) {
                    for (int gridy = 0; gridy < grid[0].length; gridy++) {
                        if (grid[gridx][gridy] != null) {
                            Color pixel = new Color(image.getRGB(grid[gridx][gridy].x, grid[gridx][gridy].y));
                            totalR += pixel.getRed() * kernel[gridx][gridy];
                            totalG += pixel.getGreen() * kernel[gridx][gridy];
                            totalB += pixel.getBlue() * kernel[gridx][gridy];
                            totalMultiplication += kernel[gridx][gridy];
                        }
                    }
                }
                Color newPixel = new Color(totalR / totalMultiplication, totalG / totalMultiplication, totalB / totalMultiplication);
                image.setRGB(x, y, newPixel.getRGB());
            }
        }
        return image;
    }

    public static BufferedImage greyScale(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(image.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
                image.setRGB(j, i, newColor.getRGB());
            }
        }
        return image;
    }

    private static Position[][] getGrid(int xpos, int ypos, int width, int height, BufferedImage image) {
        Position[][] grid = new Position[height][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!(xpos + x - width / 2 < 0 || ypos + y - height / 2 < 0 || xpos + x >= image.getWidth() || ypos + y >= image.getHeight())) {
                    grid[y][x] = new Position(xpos + x - width / 2, ypos + y - height / 2);
                }
            }
        }
        return grid;
    }

    private static double map(double value, double start1, double stop1, double start2, double stop2){
        double difference1 = stop1 - start1;
        double biggertThenstart = value - start1;
        double factor1 = biggertThenstart / difference1;

        double difference2 = stop2 - start2;
        double ssd = factor1 * difference2;
        return start2 + ssd;
    }

    public static int[][] generateKernel(int size, int mean, int sd) {
        int[][] kernel = new int[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                kernel[x][y] = 1;
            }
        }
        return kernel;
    }

    private static class Position {
        public int x;
        public int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
