
package project;

import java.util.Stack;


public class Puzzle {
	private final int[][] tiles;

	public Puzzle(int[][] tiles) {
		this.tiles = this.copy(tiles);
		}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(tiles.length + "\n");
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				str.append(" "+tiles[i][j]);
			}
			str.append("\n");
		}
		return str.toString();

	}

	/**
	 * @return
	 * returns dimension of the board
	 */
	public int dimension() {
		return this.tiles.length;
	}
	/**
	 * @param value
	 * @return
	 * for a given value returns the indexes of the current tile it is in in an integer array
	 * returns 0, 0 if no such value exists on the board (because there is no mischief in input this is unimportant)
	 */
	private int[] getIndex(int value) {
		int lenght = this.dimension();
		int[] indexes = {0, 0};
		for (int i = 0 ; i < lenght; i++)
		    for(int j = 0 ; j < lenght ; j++) {
		         if ( this.tiles[i][j] == value) {
		             indexes[0] = i;
		             indexes[1] = j;
		             break;
		         }
		    }
		return indexes;
	}

	/**
	 * @param value
	 * @return
	 * returns the Manhattan distance for a given value 
	 * using getIndex method above
	 */
	private int distanceFor(int value) {
		if (value == 0) return 0;
		else {
			int lenght = this.dimension();
			int iCoordinate = (value-1)/lenght; // i index of the value for the target board
			int jCoordinate = (value-1)%lenght; // j index of the value for the target board
			return Math.abs(this.getIndex(value)[0]-iCoordinate) + Math.abs(this.getIndex(value)[1]-jCoordinate);
		}
	}
	
	/**
	 * @return
	 * sum of Manhattan distances between tiles and goal
	 * The Manhattan distance between a board and the goal board is the sum
	 * of the Manhattan distances (sum of the vertical and horizontal distance)
	 * from the tiles to their goal positions.
	 */
	public int h() {
		int lenght = this.dimension();
		int totalDistance = 0;
		for (int i = 0 ; i < lenght; i++)
		    for(int j = 0 ; j < lenght ; j++) {
		         totalDistance += distanceFor(this.tiles[i][j]);
		    }
		return totalDistance;
	}

	/**
	 * @return
	 * checks if the current board is same as target board
	 * uses the sum of Manhattan distances 
	 */
	public boolean isCompleted() {
		if (this.h() == 0) return true;
		else return false;
	}
	
	/**
	 * @param puzzle
	 * @return
	 * checks if the current board is same as the given
	 * puzzle objects board.
	 */
	public boolean isSame(Puzzle puzzle) {
		int lenght = this.dimension();
		boolean result = true;
		for (int i = 0 ; i < lenght; i++)
		    for(int j = 0 ; j < lenght ; j++) {
		         if (this.tiles[i][j] != puzzle.tiles[i][j]){
		        	 result = false;
		        	 break;
		         }
		    }
		return result;
	}
	public boolean isSolvable() {
		int lenght = this.dimension();
	    int index = 0;
	    int count = 0;
	    int linearLenght = lenght*lenght-1 ;
	    int[] linearBoard = new int[linearLenght];
	    for (int i = 0; i < lenght; i++)
	        for (int j = 0; j < lenght; j++) {
	        	if (this.tiles[i][j] == 0) continue;
	        	linearBoard[index] = this.tiles[i][j];
	        	index++;
	        }
	    for (int i = 0; i < linearLenght - 1; i++) 
	    	for (int j = i+1; j < linearLenght; j++) 
	    		if (linearBoard[i] > linearBoard[j]) 
	    			count++;
	              
	    if (count % 2 == 1 && lenght == 3) return false;
	    else return true;
	}
	
	/**
	 * @return
	 * in an n-Puzzle (n+1)! initial state is current
	 * for these states there is a bijective match-up 
	 * between (n+1)!/2 solvable and (n+1)!/2 unsolvable
	 * cases. We can generate an anti-case by just changing
	 * two consecutive non-empty tiles' order. So to check 
	 * solvability we create and anti-case by this fucntion and 
	 * perform our solver for both case at the same time. One 
	 * of them going to be solved if the solved one is the twin
	 * then we will conclude our initial board is unsolvable
	 */
	public Puzzle twinCopy() {
		int lenght = this.dimension();
		int[][] twinBoard = this.copy(this.tiles);
		if (twinBoard[0][0]*twinBoard[0][1] != 0) {
			int temp = twinBoard[0][0];
			twinBoard[0][0] = twinBoard[0][1];
			twinBoard[0][1] = temp;
		}
		else {
			int temp = twinBoard[lenght-1][lenght-1];
			twinBoard[lenght-1][lenght-1] = twinBoard[lenght-1][lenght-2];
			twinBoard[lenght-1][lenght-2] = temp;
		}
		Puzzle twin = new Puzzle(twinBoard);
		return twin;
	}

	/**
	 * moves the empty tile 1 tile up
	 */
	private void emptyUp() {
		int iCo = this.getIndex(0)[0];
		int jCo = this.getIndex(0)[1];
		this.tiles[iCo][jCo] = this.tiles[iCo - 1][jCo];
		this.tiles[iCo - 1][jCo] = 0;
	}
	/**
	 * moves the empty tile 1 tile down
	 */
	private void emptyDown() {
		int iCo = this.getIndex(0)[0];
		int jCo = this.getIndex(0)[1];
		this.tiles[iCo][jCo] = this.tiles[iCo + 1][jCo];
		this.tiles[iCo + 1][jCo] = 0;
	}
	/**
	 * moves the empty tile 1 tile left
	 */
	private void emptyLeft() {
		int iCo = this.getIndex(0)[0];
		int jCo = this.getIndex(0)[1];
		this.tiles[iCo][jCo] = this.tiles[iCo][jCo - 1];
		this.tiles[iCo][jCo - 1] = 0;
	}
	/**
	 * moves the empty tile 1 tile right
	 */
	private void emptyRight() {
		int iCo = this.getIndex(0)[0];
		int jCo = this.getIndex(0)[1];
		this.tiles[iCo][jCo] = this.tiles[iCo][jCo + 1];
		this.tiles[iCo][jCo + 1] = 0;
	}

	// Returns any kind of collection that implements iterable.
	// For this implementation, I choose stack.
	/**
	 * @return
	 * by using empty tile movements, given above, finds adjacent
	 * puzzle objects which are created by moving the empty tile once
	 */
	public Iterable<Puzzle> getAdjacents() {
		Stack<Puzzle> adjacents = new Stack<Puzzle>();
		int lenght = this.dimension();
		int iCo = this.getIndex(0)[0];
		int jCo = this.getIndex(0)[1];
		if (iCo < lenght - 1) {
			Puzzle adj = new Puzzle(this.tiles);
			adj.emptyDown();
			adjacents.push(adj);
		}
		if (iCo > 0) {
			Puzzle adj = new Puzzle(this.tiles);
			adj.emptyUp();
			adjacents.push(adj);
		}
		if (jCo < lenght - 1) {
			Puzzle adj = new Puzzle(this.tiles);
			adj.emptyRight();
			adjacents.push(adj);
		}
		if (jCo > 0) {
			Puzzle adj = new Puzzle(this.tiles);
			adj.emptyLeft();
			adjacents.push(adj);
		}
		return adjacents;
	}

	/**
	 * @param source
	 * @return
	 * copying a given board, useful for 
	 * sustaining immutability in constructor
	 */
	private int[][] copy(int[][] source) {
		int lenght = source.length;
		int[][] copied = new int[lenght][lenght];
		for (int i = 0; i < lenght; i++) for (int j = 0; j < lenght; j++) copied[i][j] = source[i][j];
		return copied;
	}


	// You can use this main method to see your Puzzle structure.
	// Actual solving operations will be conducted in Solver.main method
	public static void main(String[] args) {
		int[][] array = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
		Puzzle board = new Puzzle(array);
		System.out.println(board);
		System.out.println(board.dimension());
		System.out.println(board.h());
		System.out.println(board.isCompleted());
		Iterable<Puzzle> itr = board.getAdjacents();
		for (Puzzle neighbor : itr) {
			System.out.println(neighbor);
			System.out.println(neighbor.equals(board));
		}
	}
}

