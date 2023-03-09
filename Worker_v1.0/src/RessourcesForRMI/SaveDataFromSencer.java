package RessourcesForRMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

public  interface SaveDataFromSencer extends Remote {

    public String[][] temperature() throws RemoteException;

    public String[][] humidity() throws RemoteException;
}