package Ressources;

import java.io.Serializable;

public class DataNoise implements Serializable {
    public int[][] image;
    public double density;

    public DataNoise(int[][] image, double density) {
        this.image = image;
        this.density = density;
    }
}
