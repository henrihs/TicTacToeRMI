import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class TicTacToeRemote extends UnicastRemoteObject implements ITicTacToeRemote {

	private static TicTacToe tictactoe;

	protected TicTacToeRemote() throws RemoteException {
		super();
	}

	@Override
	public void remoteSetCell(int x, int y, char mark) throws RemoteException {
		tictactoe.setStatusMessage("Your move");
		tictactoe.togglePlayer();
		if( tictactoe.getBoardModel().setCell(x, y, mark)){
			tictactoe.setStatusMessage(mark + " has won, you lost!");
			tictactoe.gameEnded();
		}
	}
	
	@Override
	public void createReferenceToClient(ITicTacToeRemote obj) throws RemoteException {
		
		ITicTacToeRemote opponent = obj;
		tictactoe = new TicTacToe(1, opponent);
		tictactoe.setStatusMessage("Connected to client, wait for your opponent to make the first move!");		
	}

	/**
	 * 
	 * @param args
	 * @param Server: server <own ip:port>
	 * @param Client: client <server ip:port>
	 */
	public static void main(String[] args) {
		if (args.length < 2){
			System.out.println("Too few arguments");
			return;
		}
		
		// We had to comment out this line because it raised an AccessControlException on the server
//		System.setSecurityManager(new RMISecurityManager());

		try {

			TicTacToeRemote local = new TicTacToeRemote();
			if (args[0].equals("server"))
				Naming.rebind("rmi://" + args[1] + "/ITicTacToeRemote", local);

			if (args[0].equals("client")){
				ITicTacToeRemote opponent = (ITicTacToeRemote) Naming.lookup("rmi://" + args[1] + "/ITicTacToeRemote");
				opponent.createReferenceToClient(local);
				tictactoe = new TicTacToe(0, opponent);
				tictactoe.setStatusMessage("Connected to " + args[1] + ", do your first move!");
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
