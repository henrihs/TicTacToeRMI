import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.rmi.RemoteException;

/**
 * A Tic Tac Toe application.
 * Currently this is a stand-alone application where
 * players take alternating turns using the same computer.
 * <p/>
 * The task is to transform it to a networking application using RMI.
 */
public class TicTacToe extends JFrame implements ListSelectionListener
{
  private static final int BOARD_SIZE = 15;
  private final BoardModel boardModel;
  private final JTable board;
  private final JLabel statusLabel = new JLabel();
  private final char playerMarks[] = {'X', 'O'};
  private int currentPlayer = 0; // Player to set the next mark.
  private boolean gameEnded = false;
  private int localPlayer;
  private ITicTacToeRemote remote;

//  public static void main(String args[])
//  {
//    new TicTacToe();
//  }

  public TicTacToe(int player, ITicTacToeRemote remote)
  {
    super("TDT4190: Tic Tac Toe");

    localPlayer = player;
    this.remote = remote;
    boardModel = new BoardModel(BOARD_SIZE);
    board = new JTable(boardModel);
    board.setFont(board.getFont().deriveFont(25.0f));
    board.setRowHeight(30);
    board.setCellSelectionEnabled(true);
    for (int i = 0; i < board.getColumnCount(); i++)
      board.getColumnModel().getColumn(i).setPreferredWidth(30);
    board.setGridColor(Color.BLACK);
    board.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    DefaultTableCellRenderer dtcl = new DefaultTableCellRenderer();
    dtcl.setHorizontalAlignment(SwingConstants.CENTER);
    board.setDefaultRenderer(Object.class, dtcl);
    board.getSelectionModel().addListSelectionListener(this);
    board.getColumnModel().getSelectionModel().addListSelectionListener(this);

    statusLabel.setPreferredSize(new Dimension(statusLabel.getPreferredSize().width, 40));
    statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(board, BorderLayout.CENTER);
    contentPane.add(statusLabel, BorderLayout.SOUTH);
    pack();

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    int centerX = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getSize().width) / 2;
    int centerY = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getSize().height) / 2;
    setLocation(centerX, centerY);
    setVisible(true);
  }

  void setStatusMessage(String status)
  {
    statusLabel.setText(status);
  }

  /**
   * This has to be modified. Currently the application is stand-alone so
   * both players have to use the same computer.
   * <p/>
   * When completed, marks from the first player originates from a ListSelectionEvent
   * and is then sent to the second player. And marks from the second player is received
   * and added to the board of the first player.
   */
  public void valueChanged(ListSelectionEvent e)
  {
	if (gameEnded)
		return;
	if (currentPlayer != localPlayer)
		return;
    if (e.getValueIsAdjusting())
      return;
    int x = board.getSelectedColumn();
    int y = board.getSelectedRow();
    if (x == -1 || y == -1 || !boardModel.isEmpty(x, y))
      return;
    if (boardModel.setCell(x, y, playerMarks[currentPlayer])) {
      setStatusMessage("Player " + playerMarks[currentPlayer] + " won!");
      gameEnded();
    }
    try {
    	if (!gameEnded)
    		setStatusMessage("Wait for your turn");
		remote.remoteSetCell(x,y,playerMarks[currentPlayer]);
	} catch (RemoteException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    currentPlayer = 1 - currentPlayer; // The next turn is by the other player.
  }
  
  public BoardModel getBoardModel(){
	  return this.boardModel;
  }
  
  public void togglePlayer(){
	  currentPlayer = 1 - currentPlayer;
  }
  
  public void gameEnded(){
	  gameEnded = true;
  }
}
