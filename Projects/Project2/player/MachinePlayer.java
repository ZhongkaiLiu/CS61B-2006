/* MachinePlayer.java */

package player;
import list.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  public final static int WHITE = 1;
  public final static int BLACK = 0;

  protected int myColor;
  protected int numberOfChips;
  protected int searchDepth;
  protected int searchDepthSTEP;
  protected GameBoard myBoard;
  protected List movesRecord;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    myColor = color;
    searchDepth = 1;
    searchDepthSTEP = 1;
    numberOfChips = 10;
    myBoard = new GameBoard();
    movesRecord = new DList();
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    myColor = color;
    this.searchDepth = searchDepth;
    searchDepthSTEP = searchDepth;
    numberOfChips = 10;
    myBoard = new GameBoard();
    movesRecord = new DList();
  }

  // Perform moves on this board by this player
  // And record this move in the movesRecord list at back.
  public void performMove(Move move, int color) {
    if (move.moveKind==Move.ADD) {
        this.myBoard.setChipAt(color, move.x1, move.y1);
    } else if (move.moveKind==Move.STEP) {
        this.myBoard.setChipAt(Chip.VOID, move.x1, move.y1);
        this.myBoard.setChipAt(color, move.x2, move.y2);
    }
    movesRecord.insertBack(move);
  }

  // Undo the last move in the movesRecord list
  public void undoMove() throws InvalidNodeException {
    Move lastMove = (Move) movesRecord.back().item();
    if (lastMove.moveKind==Move.ADD) {
        this.myBoard.setChipAt(Chip.VOID, lastMove.x1, lastMove.y1);
    } else if (lastMove.moveKind==Move.STEP) {
        this.myBoard.setChipAt(myColor, lastMove.x1, lastMove.y1);
        this.myBoard.setChipAt(Chip.VOID, lastMove.x2, lastMove.y2);
    }
    movesRecord.back().remove();
  }


  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    int bestScore, reply;
    int depth = searchDepth;
    Move bestMove = new Move();
    try {
      if (myBoard.checkNetwork(WHITE)||myBoard.checkNetwork(BLACK)) {
        return bestMove;
      }

      if (myColor==WHITE) {
        bestScore = GameBoard.MINSCORE-1;
      } else {
        bestScore = GameBoard.MAXSCORE+1;
      }
      List allMoves = myBoard.findAllMoves(numberOfChips, myColor);
      //System.out.println("List size = " + allMoves.length());
      ListNode currentNode = allMoves.front();
      Move nextMove;

      if (numberOfChips==0) {
        depth = searchDepthSTEP;
      }

      while (true) {
        nextMove = (Move) currentNode.item();
        //System.out.println("current move is" + nextMove);
        performMove(nextMove, myColor);
        reply = moveScore(myColor, GameBoard.MINSCORE-1, GameBoard.MAXSCORE+1, depth-1);
        undoMove();
        if ( (myColor==WHITE)&&(reply>bestScore) ) {
          bestMove = nextMove;
          bestScore = reply;
        } else if ( (myColor==BLACK)&&(reply<bestScore)) {
          bestMove = nextMove;
          bestScore = reply;
        }
        if (currentNode==allMoves.back()) {
          break;
        }
        currentNode = currentNode.next();
      }
    } catch(InvalidNodeException err) {
      System.out.println(err);
    }
    if (numberOfChips>0) {
      numberOfChips--;
    }
    //System.out.println("Best move is " + bestMove);
    performMove(bestMove, myColor);
    return bestMove;
  } 


  /*
  // Fake chooseMove()
  public Move chooseMove() {
    numberOfChips--;
    return new Move(numberOfChips-3, numberOfChips-3);
  }
  */

  // moveScore() gives the best score evaluate in depth
  public int moveScore(int color, int alpha, int beta, int depth) throws InvalidNodeException {
    int bestScore, opponent, reply;
    if (depth==0) {
      return myBoard.evaluateBoard();
    }

    if (color==WHITE) {
      bestScore = alpha;
      opponent = BLACK;
    } else {
      bestScore = beta;
      opponent = WHITE;
    }

    List allMoves = myBoard.findAllMoves(numberOfChips, myColor);
    ListNode currentNode = allMoves.front();
    Move nextMove;
    while (true) {
      nextMove = (Move) currentNode.item();
      performMove(nextMove, myColor);
      reply = moveScore(opponent, alpha, beta, depth-1);
      undoMove();
      if ( (color==WHITE)&&(reply>bestScore) ) {
        bestScore = reply;
        alpha = reply;
      } else if ( (color==BLACK)&&(reply<bestScore)) {
        bestScore = reply;
        beta = reply;
      }
      if (alpha>=beta) {
        return bestScore;
      }
      if (currentNode==allMoves.back()) {
        break;
      }
      currentNode = currentNode.next();
    }
    return bestScore;
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    int opponent;
    if (myColor==WHITE) {
      opponent = BLACK;
    } else {
      opponent = WHITE;
    }
    if ( myBoard.isLegalMove(opponent, m)) {
      performMove(m, opponent);
      return true;
    } else {
      return false;
    }
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if ( myBoard.isLegalMove(myColor, m) ) {
      performMove(m, myColor);
      return true;
    } else {
      return false;
    }
  }

  public void printBoard() {
    Chip[][] board = myBoard.board;
    System.out.println("Number of Chips = " + numberOfChips);
    for (int i = 0; i<GameBoard.DIMENSION ;i++ ) {
      for (int j = 0;j<GameBoard.DIMENSION ;j++ ) {
        if (board[j][i].color==Chip.WHITE) {
          System.out.print("| W ");
        } else if (board[j][i].color==Chip.BLACK) {
          System.out.print("| B ");
        } else {
          System.out.print("|   ");
        }
      }
      System.out.print("|");
      System.out.println();
    }
  }

}
