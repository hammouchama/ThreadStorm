package MainServer;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.jhlabs.image.ColorHalftoneFilter;
import com.jhlabs.image.SmartBlurFilter;

import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;


public class ImageFilters {
    // convolution--------------------------------------------------
    public static int[][] applyFilterConvolution(int[][] inputMatrix, float[][] ker) {
        int height = inputMatrix.length;
        int width = inputMatrix[0].length;
        // *Kernel
        int rows = ker.length;
        int columns = ker[0].length;

        float[] arraykernel = new float[rows * columns];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                arraykernel[index++] = ker[i][j];
            }
        }


        Kernel kernel = new Kernel(3, 3, arraykernel);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage inputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inputImage.setRGB(x, y, inputMatrix[y][x]);
            }
        }
        //--------------------------------------------------
        //-----------------------------------------------------
        BufferedImage outputBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        convolveOp.filter(inputImage, outputBufferedImage);
        int[][] outputMatrix = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                outputMatrix[y][x] = outputBufferedImage.getRGB(x, y);
            }
        }
        return outputMatrix;
    }

    // addNoise-----------------------------------------------------
    public static int[][] addNoise(int[][] image, double noiseLevel) {
        int height = image.length;
        int width = image[0].length;
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image[i][j];
                if (random.nextDouble() < noiseLevel / 2) {
                    pixel = 0xffffffff;
                } else if (random.nextDouble() < noiseLevel / 2) {
                    pixel = 0xff000000;
                }
                image[i][j] = pixel;
            }
        }
        return image;
    }

    // Grayscale-----------------------------------------------------
    public static int[][] convertToGrayscale(int[][] colorImage) {
        int height = colorImage.length;
        int width = colorImage[0].length;
        int[][] grayImage = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = colorImage[y][x];

                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                int gray = (int) (0.3 * red + 0.59 * green + 0.11 * blue);

                grayImage[y][x] = (gray << 16) | (gray << 8) | gray;
            }
        }
        return grayImage;
    }

    // Filter Color Halftone-----------------------------------------------------
    public static int[][] applyFilterColorHalftone(int[][] colorImage) {
        ColorHalftoneFilter colorHalftoneFilter = new ColorHalftoneFilter();
        colorHalftoneFilter.setCyanScreenAngle((float)Math.toRadians( 0 ));
        colorHalftoneFilter.setMagentaScreenAngle((float)Math.toRadians( 0 ));
        colorHalftoneFilter.setYellowScreenAngle((float)Math.toRadians( 0 ));
        
        
        int[][] intImageRes=colorHalftoneFilter.filter2(colorImage);
        
        return intImageRes;
    }

    // Filter Smart Blur-----------------------------------------------------
    public static int[][] applyFilterSmartBlur(int[][] colorImage) {
        SmartBlurFilter  smartBlurFilter  = new SmartBlurFilter ();
        smartBlurFilter.setRadius(10); // adjust the blur radius
        smartBlurFilter.setThreshold(20); // adjust the threshold
        
        
        int[][] intImageRes=smartBlurFilter.filter2(colorImage);
        
        return intImageRes;
    }

}
