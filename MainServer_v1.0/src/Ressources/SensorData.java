package Ressources;
import java.io.Serializable;

public class SensorData implements Serializable{
     private String[][] donnees;
     public SensorData(String[][] donnees) {
          this.donnees=donnees;
     }
     

    /**
     * @return String[][] return the donnees
     */
    public String[][] getDonnees() {
        return this.donnees;
    }

    /**
     * @param donnees the donnees to set
     */
    public void setDonnees(String[][] donnees) {
        this.donnees = donnees;
    }

}