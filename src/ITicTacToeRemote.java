import java.rmi.*;


public interface ITicTacToeRemote extends Remote {
	
	public void refreshGui() throws RemoteException;
	
}
