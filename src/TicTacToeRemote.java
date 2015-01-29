import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class TicTacToeRemote extends UnicastRemoteObject implements ITicTacToeRemote {

	protected TicTacToeRemote() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void remoteSetCell(int x, int y, char mark) throws RemoteException {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
