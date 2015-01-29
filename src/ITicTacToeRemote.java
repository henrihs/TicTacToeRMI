import java.rmi.*;


public interface ITicTacToeRemote extends Remote {
	
	public void remoteSetCell(int x, int y, char mark) throws RemoteException;
	
	public void remoteLookup(String url) throws RemoteException;
	
}
