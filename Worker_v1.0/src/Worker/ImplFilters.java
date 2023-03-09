package Worker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.jhlabs.image.ColorHalftoneFilter;
import com.jhlabs.image.SmartBlurFilter;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import RessourcesForRMI.ImageDivider.SubMatrix;

/**
 * ImplFilters
 */
public class ImplFilters extends UnicastRemoteObject implements RessourcesForRMI.Filters  {

    
    public ImplFilters() throws RemoteException {
        super();
    }

    // convolution--------------------------------------------------
    @Override
    public SubMatrix applyFilterConvolution(SubMatrix inputSubMatrix, float[][] ker) {
        System.out.println("applying Convolution");
        int[][] inputMatrix = inputSubMatrix.matrix;
        int height = inputMatrix.length;
        int width = inputMatrix[0].length;
        // float[] ker = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
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
        // convolution--------------------------------------------------
        // gryskil-----------------------------------------------------
        BufferedImage outputBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        convolveOp.filter(inputImage, outputBufferedImage);
        int[][] outputMatrix = new int[height][width];
        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width; x++) {
                outputMatrix[y-1][x-1] = outputBufferedImage.getRGB(x, y);
            }
        }


        inputSubMatrix.matrix=outputMatrix;
        System.out.println("---Sending data");
        return inputSubMatrix;
    }


    // Filter Color Halftone-----------------------------------------------------
    public SubMatrix applyFilterColorHalftone(SubMatrix inputSubMatrix) {
        System.out.println("applying 'Filter Color Halftone'");
        int[][] inputMatrix = inputSubMatrix.matrix;
        
        ColorHalftoneFilter colorHalftoneFilter = new ColorHalftoneFilter();
        colorHalftoneFilter.setCyanScreenAngle((float)Math.toRadians( 0 ));
        colorHalftoneFilter.setMagentaScreenAngle((float)Math.toRadians( 0 ));
        colorHalftoneFilter.setYellowScreenAngle((float)Math.toRadians( 0 ));
        
        
        int[][] outputMatrix=colorHalftoneFilter.filter2(inputMatrix);
        
        inputSubMatrix.matrix=outputMatrix;
        System.out.println("---Sending data");
        return inputSubMatrix;
    }


    // Filter Smart Blur-----------------------------------------------------
    public SubMatrix applyFilterSmartBlur(SubMatrix inputSubMatrix) {
        System.out.println("applying Filter Smart Blur");
        int[][] inputMatrix = inputSubMatrix.matrix;
        SmartBlurFilter  smartBlurFilter  = new SmartBlurFilter ();
        smartBlurFilter.setRadius(10); // adjust the blur radius
        smartBlurFilter.setThreshold(20); // adjust the threshold
        
        
        int[][] outputMatrix=smartBlurFilter.filter2(inputMatrix);
        
        inputSubMatrix.matrix=outputMatrix;
        System.out.println("---Sending data");
        return inputSubMatrix;
    }

}