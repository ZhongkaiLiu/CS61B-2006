/* GameTest.java */

package player;
import list.*;

public class GameTest {
	public static void main(String[] args) {
		MachinePlayer player1 = new MachinePlayer(MachinePlayer.BLACK);

		Move move1 = new Move(0,1);
		Move move2 = new Move(6,5);
		Move move3 = new Move(5,5);
		Move move4 = new Move(3,3);
		Move move5 = new Move(3,5);
		Move move6 = new Move(5,7);
		Move move7 = new Move(1,1);

		//player1.forceMove(move1);
		player1.opponentMove(move1);
		Move move8 = player1.chooseMove();
		player1.forceMove(move8);
		player1.opponentMove(move2);
		System.out.println();
		Move move9 = player1.chooseMove();
		player1.forceMove(move9);
		System.out.println();
		player1.opponentMove(move4);
		Move move10 = player1.chooseMove();
		player1.forceMove(move10);
		//System.out.println(move8);
		//System.out.println(move9);

		player1.printBoard();

		try {
			System.out.println(player1.myBoard.checkNetwork(player1.myColor));
		} catch(Exception err) {
			System.out.println(err);
		}

		/*
		//player1.opponentMove(move2);
		//player1.performMove(move1, player1.myColor);

		player1.printBoard();
		List allMoves = player1.myBoard.findAllMoves(player1.numberOfChips, player1.myColor);
		ListNode currentNode = allMoves.front();
		Move nextMove;
		
		try {
			while (true) {
	        nextMove = (Move) currentNode.item();
	        System.out.println(nextMove);
	        //player1.performMove(nextMove, player1.myColor);
	        if (currentNode==allMoves.back()) {
	          break;
	        }
        currentNode = currentNode.next();
        }
		} catch(Exception err) {
			System.out.println(err);
		}
		*/
      	
		//player1.printBoard();
	}
}