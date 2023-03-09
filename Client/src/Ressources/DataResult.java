package Ressources;

import java.io.Serializable;

public class DataResult implements Serializable {
    public int[][] image=null;
    public int index=0;
    public String statue=null;

    public DataResult(int width,int height){
        image = new int[width][height];
    }
    public DataResult(int[][] image){
        this.image=image;
    }
    public DataResult(int[][] image,int index){
        this.image=image;
        this.index=index;
    }
    public DataResult(String statue){
        this.statue=statue;
    }

    public int[][] getData() {
        return image;
    }
}
