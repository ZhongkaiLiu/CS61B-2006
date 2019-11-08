/* Chip.java */
package player;
import list.*;

public class Chip {
	public final static int WHITE = 1;
	public final static int BLACK = 0;
	public final static int VOID = -1;

	protected int color;
	protected GameBoard myBoard;
	protected boolean visited;
	protected int goal; //Start == 0, Mid == 1, End == 2;
	protected int x, y;

	// Construct a chip with color c on board.
	public Chip(int c, GameBoard board, int xx, int yy) {
		switch (c) {
			case WHITE:
				color = WHITE;
				break;
			case BLACK:
				color = BLACK;
				break;
			default:
				color = VOID;
				break;
		}
		myBoard = board;
		visited = false;
		x = xx;
		y = yy;
		goal = 1;
		if (color==WHITE) {
			if (x==0) {
				goal = 0;
			} else if (x==GameBoard.DIMENSION-1) {
				goal = 2;
			}
		} else if (color==BLACK) {
			if (y==0) {
				goal = 0;
			} else if (y==GameBoard.DIMENSION-1) {
				goal = 2;
			}
		}
	}

	// Construct a void chip on board.
	public Chip(GameBoard board, int xx, int yy) {
		color = VOID;
		myBoard = board;
		visited = false;
		x = xx;
		y = yy;
		goal = 1;
	}
	// Return the color of the chip.
	public int color() {
		return color;
	}

	// Start from (x, y) go on direction to find the same color chip as chip at (x, y).
	public Chip findChip(int direction, int color, int xx, int yy) {
		while (true) {
			switch (direction) {
				case 1:
					xx++;
					break;
				case 2:
					xx++;
					yy--;
					break;
				case 3:
					yy--;
					break;
				case 4:
					xx--;
					yy--;
					break;
				case 5:
					xx--;
					break;
				case 6:
					xx--;
					yy++;
					break;
				case 7:
					yy++;
					break;
				case 8:
					xx++;
					yy++;
					break;
			}	
			if (xx<0||yy<0||xx>=GameBoard.DIMENSION||yy>=GameBoard.DIMENSION) {
				break;
			}
			if (myBoard.board[xx][yy].color()==color) {
				return myBoard.board[xx][yy];
			} else if (myBoard.board[xx][yy].color()!=VOID) {
				return null;
			}
		}
		return null;
	}

	// Return a list of all connected chips.
	public Chip[] findConnectedChips(boolean[] directions) {
		Chip[] chipsArray = new Chip[8];
		Chip foundChip;

		for (int i=0; i<directions.length; i++) {
			chipsArray[i] = null;
			if (directions[i]) {
				foundChip = findChip(i+1, this.color, this.x, this.y);
				chipsArray[i] = foundChip;
			}
		}
		return chipsArray;
	}

	// Return a list of all connected chips.
	public int countConnectedChips(boolean[] directions) {
		int count = 0;
		Chip foundChip;

		for (int i=0; i<directions.length; i++) {
			if (directions[i]) {
				foundChip = findChip(i+1, this.color, this.x, this.y);
				if (foundChip!=null) {
					count++;
				}
			}
		}
		return count;
	}



	// Check valid network from this chip with a length = networkLength.
	// Return true when network >=6, and ends at a goal chip.
	public boolean checkNetworkChip(int networkLength, int visitedDirection) throws InvalidNodeException {
		this.visited = true;
		networkLength++;
		//System.out.println("Now the network's length is " + networkLength);

		// case 1, the network >=6 and ends at a goal chip, then it's a valid network.
		// case 2, the network meets a goal chip, but networkLength < 6, it's not a valid network.
		if (this.goal==2) {
			if (networkLength>=6) {
				return true;
			} else {
				//System.out.println("This network ends with length = "+networkLength);
				this.visited = false;
				return false;
			}
		}

		boolean[] directions = {true, true, true, true, true, true, true, true};
		directions[visitedDirection-1] = false;
		
		if (visitedDirection>4) {
			directions[visitedDirection-5] = false;
		} else {
			directions[visitedDirection+3] = false;
		}

		/*
		System.out.println("The directions should be investigated are: ");
		for (int i=0; i<directions.length ;i++ ) {
			if (directions[i]) {
				System.out.print((i+1) + ", ");
			}
		}
		*/

		Chip[] connectedChips = this.findConnectedChips(directions);
		Chip currentChip;
		boolean result = false;

		for (int i=0;i<connectedChips.length ;i++ ) {
			currentChip = connectedChips[i];
			if (currentChip!=null) {
				if ( (currentChip.visited==false)&&(currentChip.goal!=0) ) {
					//System.out.println("\n\nChip: (" + currentChip.x + ", " + currentChip.y + ") is on current network.");
					result = currentChip.checkNetworkChip(networkLength, i+1);
				}
				if (result) {
					return true;
				}
			}
		}

		//System.out.println("\n\nCurrent Chip: (" + this.x + ", " + this.y + ") has investigated all its connected chips, but no good network.");
		this.visited = false;
		return false;
	}


	public int countNetworkChip(int networkLength, int visitedDirection) throws InvalidNodeException {
		this.visited = true;
		networkLength++;
		int result = networkLength;
		int length = networkLength;

		if (this.goal==2) {
			this.visited = false;
			return result;
		}

		boolean[] directions = {true, true, true, true, true, true, true, true};
		directions[visitedDirection-1] = false;
		if (visitedDirection>4) {
			directions[visitedDirection-5] = false;
		} else {
			directions[visitedDirection+3] = false;
		}


		Chip[] connectedChips = this.findConnectedChips(directions);
		Chip currentChip;

		for (int i=0;i<connectedChips.length ;i++ ) {
			currentChip = connectedChips[i];
			if (currentChip!=null) {
				if ( (currentChip.visited==false)&&(currentChip.goal!=0) ) {
					length = currentChip.countNetworkChip(networkLength, i+1);
				}
				if (length>result) {
					result = length;
				}
			}
		}
		this.visited = false;
		return result;
	}

}