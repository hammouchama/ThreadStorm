package Ressources;

import java.io.Serializable;

public class DataGray implements Serializable {
    public int[][] image;

    public DataGray(int[][] image) {
        this.image = image;
    }
}