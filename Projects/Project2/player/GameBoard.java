/* GameBoard.java */

package player;
import list.*;

public class GameBoard {
	public final static int DIMENSION = 8;
	public final static int MAXSCORE = 1000;
	public final static int MINSCORE = -1000;

	protected Chip[][] board;

/*
	Construct a GameBoard Object with size == DIMENSION * DIMENSION.
*/
	public GameBoard() {
		board = new Chip[DIMENSION][DIMENSION];
		for ( int i=0; i<DIMENSION; i++ ) {
			for ( int j=0; j<DIMENSION ; j++) {
				board[i][j] = new Chip(this, i, j);
			}
		}
	}

/*
	Set Chip == WHITE/BLACK/VOID at (x, y).
	If color is not valid, then do nothing.
*/
	public void setChipAt(int color, int x, int y) {
		board[x][y] = new Chip(color, this, x, y);
	}

// Return chip at (x, y)
	public Chip getChipAt(int x, int y) {
		return board[x][y];
	}

/*
	Return int[]
	int[0] is the number of same chip around (x,y)
	int[1], int[2] are the location of the adjacent same chip, if there's only one same chip around.
*/
	public int[] findSameChipAround(int x, int y, int color) {
		int i, j;
		int[] count = {0, -1, -1};
		int chipX = -1;
		int chipY = -1;
		if (x==0) {
			i = 0;
		} else {
			i = x - 1;
		}
		while (i<=x+1&&i<=GameBoard.DIMENSION-1) {
			if (y==0) {
				j = 0;
			} else {
				j = y - 1;
			}
			while (j<=y+1&&j<=GameBoard.DIMENSION-1) {
				if (board[i][j].color()==color) {
					chipX = i;
					chipY = j;
					count[0]++;
				}
				j++;
			}
			i++;
		}
		if (count[0]==1) {
			count[1] = chipX;
			count[2] = chipY;
		}

		return count;
	}

	// Check whether if add a chip at (x, y), there would a cluster.
	public boolean cluster(int x, int y, int color) {
		int[] count = findSameChipAround(x, y, color);
		if (count[0]==1) {
			if(findSameChipAround(count[1], count[2], color)[0] > 1) {
				//System.out.println(1);
				return true;
			} else {
				//System.out.println(2);
				return false;
			}
		} else if(count[0]>1) {
			//System.out.println(3);
			return true;
		} else {
			//System.out.println(4);
			return false;
		}
	}

	// Return a list of all valid moves when chipNum for color on this board.
	public List findAllMoves(int chipNum, int color) {
		List movesList = new DList();
		Move move;
		if (chipNum>0) {
			for (int i=0; i<DIMENSION ; i++ ) {
				for (int j=0; j<DIMENSION ; j++ ) {
					move = new Move(i, j);
					if (isLegalMove(color, move)) {
						movesList.insertBack(move);
					}
				}
			}
		} else {
			for (int i=0; i<DIMENSION ; i++ ) {
				for (int j=0; j<DIMENSION ; j++ ) {
					if (board[i][j].color()==color) {
						for (int k=0; k<DIMENSION ; k++ ) {
							for (int l=0; l<DIMENSION ; l++ ) {
								move = new Move(i, j, k, l);
								if (isLegalMove(color, move)) {
									movesList.insertBack(move);
								}
							}
						}
					}
				}
			}
		}
		return movesList;
	}

	// Set all chips' visited == 0.
	public void unvisitChips() {
		for ( int i=0; i<DIMENSION; i++ ) {
			for ( int j=0; j<DIMENSION ; j++) {
				board[i][j].visited = false;
			}
		}
	}


	// Check whether there's a network from (x, y).
	public boolean checkNetworkFrom(int x, int y) throws InvalidNodeException{
		Chip startChip = board[x][y];
		int color = startChip.color();
		startChip.visited = true;

		boolean[] startDirections = {false, false, false, false, false, false, false, false};
		if (color==MachinePlayer.WHITE) {
			startDirections[0] = true;
			startDirections[1] = true;
			startDirections[7] = true;
		} else {
			startDirections[5] = true;
			startDirections[6] = true;
			startDirections[7] = true;
		}

		Chip[] connectedChips = startChip.findConnectedChips(startDirections);
		Chip currentChip;
		boolean result = false;

		for (int i=0;i<connectedChips.length ;i++ ) {
			currentChip = connectedChips[i];
			if (currentChip!=null) {
				if ( (currentChip.visited==false)&&(currentChip.goal!=0) ) {
					//System.out.println("\n\nChip: (" + currentChip.x + ", " + currentChip.y + ") is on current network.");
					result = currentChip.checkNetworkChip(1, i+1);
				}
				if (result) {
					return true;
				}
			}
		}

		startChip.visited = false;
		return false;
	}

	// Check whether there's a network for the color on this board.
	public boolean checkNetwork(int color) throws InvalidNodeException {
		boolean result = false;
		int count = 0;
		int countGoal1 = 0;
		int countGoal2 = 0;

		// Case 1: no goal chip, no network.
		if (color==MachinePlayer.WHITE) {
			for (int j = 1; j<DIMENSION-1 ; j++ ) {
				if (board[DIMENSION-1][j].color()==MachinePlayer.WHITE) {
					countGoal2++;
				}
				if (board[0][j].color()==MachinePlayer.WHITE) {
					countGoal1++;
				}
			}
		} else {
			for (int i = 1; i<DIMENSION-1 ; i++ ) {
				if (board[i][DIMENSION-1].color()==MachinePlayer.BLACK) {
					countGoal2++;
				}
				if (board[i][0].color()==MachinePlayer.BLACK) {
					countGoal1++;
				}
			}
		}
		if (countGoal1<1||countGoal2<1) {
			return false;
		}

		// Case 2: If there's less than 6 chips, no network.
		if (color==MachinePlayer.WHITE) {
			for (int j = 1; j<DIMENSION-1 ; j++ ) {
				for (int i = 0;i<DIMENSION ; i++ ) {
					if (board[i][j].color()==MachinePlayer.WHITE) {
						count++;
					}
				}
			}
		} else {
			for (int i = 1; i<DIMENSION-1 ; i++ ) {			
				for (int j = 0;j<DIMENSION ; j++ ) {
					if (board[i][j].color()==MachinePlayer.BLACK) {
						count++;
					}
				}
			}
		}
		if (count<6) {
			return false;
		}

		// Case 3: Complicated search for network
		if (color==MachinePlayer.WHITE) {
			for (int j = 1;j<DIMENSION-1 ;j++ ) {
				if (board[0][j].color()==MachinePlayer.WHITE) {
					unvisitChips();
					result = checkNetworkFrom(0, j);
				}
				if (result) {
					return true;
				}		
			}
		} else {
			for (int i = 1; i<DIMENSION-1 ; i++ ) {
				if (board[i][0].color()==MachinePlayer.BLACK) {

					//System.out.println("Start from: (" + i + ", 0)");

					unvisitChips();
					result = checkNetworkFrom(i, 0);
				}
				if (result) {
					return true;
				}
			}
		}
		return false;
	}



	public int countNetworkFrom(int x, int y) throws InvalidNodeException{
		Chip startChip = board[x][y];
		int color = startChip.color();
		startChip.visited = true;

		boolean[] startDirections = {false, false, false, false, false, false, false, false};
		if (color==MachinePlayer.WHITE) {
			startDirections[0] = true;
			startDirections[1] = true;
			startDirections[7] = true;
		} else {
			startDirections[5] = true;
			startDirections[6] = true;
			startDirections[7] = true;
		}

		Chip[] connectedChips = startChip.findConnectedChips(startDirections);
		Chip currentChip;
		int result = 1;
		int length = 1;

		for (int i=0; i<connectedChips.length ;i++ ) {
			currentChip = connectedChips[i];
			if (currentChip!=null) {
				if ( (currentChip.visited==false)&&(currentChip.goal!=0) ) {
					length = currentChip.countNetworkChip(1, i+1);
				}
				if (length>result) {
					result = length;
				}
			}
		}
		startChip.visited = false;
		return result;
	}

	public int countNetwork(int color) throws InvalidNodeException {
		int result = 0;
		int length = 0;

		if (color==MachinePlayer.WHITE) {
			for (int j = 1;j<DIMENSION-1 ;j++ ) {
				if (board[0][j].color()==MachinePlayer.WHITE) {
					unvisitChips();
					length = countNetworkFrom(0, j);
				}
				if (length>result) {
					result = length;
				}
			}
		} else {
			for (int i = 1; i<DIMENSION-1 ; i++ ) {
				if (board[i][0].color()==MachinePlayer.BLACK) {
					unvisitChips();
					length = countNetworkFrom(i, 0);
				}
				if (length>result) {
					result = length;
				}
			}
		}
		return result;
	}










	// evaluate the probability of winning for WHITE/BLACK.
	// MAX value for WHITE win board.
	// MIN value for BLACK win board.
	// Mid value for NO win board.
	// As Larger, the probability of WHITE winning larger, vice versa.
	public int evaluateBoard() throws InvalidNodeException {
		int score = 0;
		int countWhite = 0;
		int countBlack = 0;
		int countWhiteGoal1 = 0;
		int countWhiteGoal2 = 0;
		int countBlackGoal1 = 0;
		int countBlackGoal2 = 0;
		int countNetworkWhite = 0;
		int countNetworkBlack = 0;

		if (checkNetwork(MachinePlayer.WHITE)) {
			score = MAXSCORE;
		} else if (checkNetwork(MachinePlayer.BLACK)) {
			score = MINSCORE;
		}

		// Complicated method to evaluate the mid case when there's no winner.
		
		// For goal chips
		for (int j = 1; j<DIMENSION-1 ; j++ ) {
			if (board[DIMENSION-1][j].color()==MachinePlayer.WHITE) {
				countWhiteGoal1++;
				if (countWhiteGoal1==1) {
					score += 20 * ( 10 - Math.abs(4-j) );
				} else if (countWhiteGoal1==2) {
					score += 2 * ( 10 - Math.abs(4-j) );
				} else {
					score += ( 10 - Math.abs(4-j) );
				}
			}
			if (board[0][j].color()==MachinePlayer.WHITE) {
				countWhiteGoal2++;
				if (countWhiteGoal2==1) {
					score += 20 * ( 10 - Math.abs(4-j) ) ;
				} else if (countWhiteGoal2==2) {
					score += 2 * ( 10 - Math.abs(4-j) );
				} else {
					score += ( 10 - Math.abs(4-j) );
				}
			}
		}

		for (int i = 1; i<DIMENSION-1 ; i++ ) {
			if (board[i][DIMENSION-1].color()==MachinePlayer.BLACK) {
				countBlackGoal1++;
				if (countBlackGoal1==1) {
					score += - 20 * ( 10 - Math.abs(4-i) );
				} else if (countBlackGoal1==2) {
					score += - 2 * ( 10 - Math.abs(4-i) );
				} else {
					score += ( 10 - Math.abs(4-i) );
				}
			}
			if (board[i][0].color()==MachinePlayer.BLACK) {
				countBlackGoal2++;
				if (countBlackGoal2==1) {
					score += - 20 * ( 10 - Math.abs(4-i) );
				} else if (countBlackGoal2==2) {
					score += - 2 * ( 10 - Math.abs(4-i) );
				} else {
					score += ( 10 - Math.abs(4-i) );
				}
			}
		}

		// For normal chips
		for (int j = 1; j<DIMENSION-1 ; j++ ) {
			for (int i = 1;i<DIMENSION-1 ; i++ ) {
				if (board[i][j].color()==MachinePlayer.WHITE) {
					countWhite++;
					score += Math.abs(16 - Math.abs(4-i) * Math.abs(4-j));
				} else if (board[i][j].color()==MachinePlayer.BLACK) {
					countBlack++;
					score -= Math.abs(16 - Math.abs(4-i) * Math.abs(4-j));
				}
			}
		}

		// For normal connections
		boolean[] directions = {true, true, true, true, true, true, true, true};
		for (int j = 0; j<DIMENSION ; j++ ) {
			for (int i = 0;i<DIMENSION ; i++ ) {
				if (board[i][j].color()==MachinePlayer.WHITE) {
					score += 5 * board[i][j].countConnectedChips(directions);
				} else if (board[i][j].color()==MachinePlayer.BLACK) {
					score -= 5 * board[i][j].countConnectedChips(directions);
				}
			}
		}

		// For network connections
		countNetworkWhite = countNetwork(MachinePlayer.WHITE)-1;
		countNetworkBlack = countNetwork(MachinePlayer.BLACK)-1;

		score += 10 * countNetworkWhite - 10 * countNetworkBlack;
		
		if (score>MAXSCORE) {
			score--;
		} else if (score<MINSCORE) {
			score++;
		}
		return score;
	}

/*
	Check this move is legal for this color on this game board.
	If legal, return true.
*/
	public boolean isLegalMove(int color, Move move) {
		int i, j;
		if (move.moveKind==Move.ADD) {
			i = move.x1;
			j = move.y1;
		} else if(move.moveKind==Move.STEP) {
			i = move.x2;
			j = move.y2;
		} else {
			return true;
		}
		// four corners are illegal
		if ( (i==0&&j==0) || (i==0&&j== (DIMENSION-1) ) || ( i == (DIMENSION-1) &&j==0 ) || ( i == (DIMENSION-1)&&j== (DIMENSION-1) ) ){
			return false;
		}
		// goals of the opposite color are illegal
		if ( color==MachinePlayer.BLACK ) {
			if (i==0||i==(DIMENSION-1)) {
				return false;
			}
		} else {
			if (j==0||j==(DIMENSION-1)) {
				return false;
			}
		}
		// occupied squares are illegal
		if (this.getChipAt(i, j).color()!=Chip.VOID) {
			return false;
		}
		// For step move, the second location cannot be the same as the first location.
		if ( move.moveKind==Move.STEP&&(move.x1==move.x2)&&(move.y1==move.y2) ) {
			return false;
		}
		if (move.moveKind==Move.STEP) {
			setChipAt(Chip.VOID, move.x1, move.y1);
			if (this.cluster(i,j,color)) {
				return false;
			}
			setChipAt(color, move.x1, move.y1);
		}
		// chip would form a cluster is illegal
		if (move.moveKind==Move.ADD&&this.cluster(i, j, color)) {
			return false;
		}
		return true;
	}
}