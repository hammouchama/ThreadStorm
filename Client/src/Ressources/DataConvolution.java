package Ressources;

import java.io.Serializable;

public class DataConvolution implements Serializable {
    public int[][] image;
    public float[][] kernel;

    public DataConvolution(int[][] image, float[][] kernel) {
        this.image = image;
        this.kernel = kernel;
    }

    public  int[][]  getImage(){
        return image;
    }
}
