import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class TicTacToeRemote extends UnicastRemoteObject implements ITicTacToeRemote {

	private static TicTacToe tictactoe;

	protected TicTacToeRemote() throws RemoteException {
		super();
	}

	@Override
	public void remoteSetCell(int x, int y, char mark) throws RemoteException {
		tictactoe.getBoardModel().setCell(x, y, mark);

	}

	/**
	 * 
	 * @param args
	 *  Server: server <own ip>
	 *  Client: client <own ip> <server ip>
	 */
	public static void main(String[] args) {
		if (args.length < 2){
			System.out.println("Too few arguments");
			return;
		}
		System.setSecurityManager(new RMISecurityManager());

		try {

			TicTacToeRemote local = new TicTacToeRemote();
			rebind(args[1], local);
			
			if (args[0] == "client"){
				ITicTacToeRemote opponent = local.lookup(args[2]);
				opponent.remoteLookup(args[1]);
				TicTacToe tictactoe = new TicTacToe(0, opponent);
				tictactoe.setStatusMessage("Connected to " + args[2] + ", do your first move!");
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

	public static void rebind(String url, TicTacToeRemote obj){
		try {
			Naming.rebind("rmi://" + url + "/ITicTacToeRemote", obj);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ITicTacToeRemote lookup(String url) throws MalformedURLException, RemoteException, NotBoundException{
		return (ITicTacToeRemote) Naming.lookup("rmi://" + url + "/ITicTacToeRemote");
	}

	@Override
	public void remoteLookup(String url) throws RemoteException {
		try {
			ITicTacToeRemote opponent = lookup(url);
			TicTacToe tictactoe = new TicTacToe(1, opponent);
			tictactoe.setStatusMessage("Connected to " + url + ", wait for your opponent to make the first move!");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
