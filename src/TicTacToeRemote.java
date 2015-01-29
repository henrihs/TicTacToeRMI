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

	public static void main(String[] args) {
		if (args.length < 2){
			System.out.println("Too few arguments");
			return;
		}
		else if (args[0] == "server"){
			tictactoe = new TicTacToe(1);
		}
		else if (args[0] == "client") {
			tictactoe = new TicTacToe(0);
		}
	}

}
