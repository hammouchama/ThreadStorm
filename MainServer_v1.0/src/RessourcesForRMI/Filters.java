package RessourcesForRMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

import RessourcesForRMI.ImageDivider.SubMatrix;
/**
 * Filters
 */
public interface Filters extends Remote {

    public SubMatrix applyFilterConvolution(SubMatrix inputMatrix, float[][] ker) throws RemoteException;

    public SubMatrix applyFilterColorHalftone(SubMatrix inputSubMatrix) throws RemoteException;

    public SubMatrix applyFilterSmartBlur(SubMatrix inputSubMatrix) throws RemoteException;
}