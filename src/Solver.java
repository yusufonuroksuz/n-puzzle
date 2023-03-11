
package project;
   
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Solver {
	private PriorityObject winner;

	// priority = moves + manhattan
	// if priority is low, it's good.
	// find a solution to the initial board
	public Solver(Puzzle root) {
		System.out.println("Starting the solver...");
		if (root == null)
			throw new IllegalArgumentException();
		solve(root);
		System.out.println("Solving is finished...");
	}
	
	private int gCounter = 0; // our depth counter for A* searching tree
	
	private boolean solvability = true;
	
	public boolean isSolvable() {
		return solvability;
	}
	
	/**
	 * @param root
	 * takes a root puzzle, creates a A* search tree for the target board
	 * in every iteration of our while loop we check the leaves of the A* tree and 
	 * choose the node with least f value. This way we go to the most probable 
	 * solution step every iteration. When we finally come accross a solution 
	 * we know it has the least depth among other solutions thanks to definiton 
	 * of f, g, h and our a* algorithm. This is easy to perform for us because 
	 * of Priority Queue always gives us the smaller one using the compare method 
	 * defined for PriorityObjects. PriorityObjects are the nodes of our tree, they 
	 * store the previous node and f, g and h values for itself.
	 */
	private void solve(Puzzle root) {
		solvability = true;
		PriorityObject temptwin;
		PriorityObject initialStatetwin = new PriorityObject(root.twinCopy(), 0, null);
		temptwin = initialStatetwin;
		PriorityQueue<PriorityObject> mayBeSolutionstwin = new PriorityQueue<>();
		
		PriorityObject temp;
		PriorityObject initialState = new PriorityObject(root, 0, null);
		temp = initialState;
		PriorityQueue<PriorityObject> mayBeSolutions = new PriorityQueue<>();
		
		while (!temp.board.isCompleted() && !temptwin.board.isCompleted()) {
			for (Puzzle adj : temp.board.getAdjacents()) {
				PriorityObject obje = new PriorityObject(adj, temp.g+1, temp);
				if (temp.prev != null) if (adj.isSame(temp.prev.board)) continue; // we do not add the previous state as a new node to our tree to avoid infinite loops
				mayBeSolutions.add(obje);
			}
			
			for (Puzzle adj : temptwin.board.getAdjacents()) {
				PriorityObject obje = new PriorityObject(adj, temptwin.g+1, temp);
				if (temptwin.prev != null) if (adj.isSame(temptwin.prev.board)) continue; // we do not add the previous state as a new node to our tree to avoid infinite loops
				mayBeSolutionstwin.add(obje);
			}
			
			PriorityObject newStatetwin = mayBeSolutionstwin.poll();
			temptwin = newStatetwin;
			
			PriorityObject newState = mayBeSolutions.poll();
			temp = newState;
		}
		if (temptwin.board.isCompleted()) {
			solvability = false;
			System.out.println("Board is unsolvable");
		}
		else {
			winner = temp;
			gCounter = temp.g;
		}
		
		
	}

	/**
	 * @return
	 * returns depth of our solution  (how many steps to achieve the solution)
	 */
	public int getMoves() {
		if (this.isSolvable()) return gCounter;
		else return 0;
	}

	/**
	 * @return
	 * Backtraces from the solution, PriorityObject winner, to our initial state
	 * and stores them in a Stack. This is thanks to PriorityObjects are nodes in 
	 * the a* search tree that stores the previous object (parent).
	 * By using another Stack we reverse the first stack because sadly for-each loop 
	 * iterates according to FiFo logic although Stack are used because of their LiFo 
	 * logic.  
	 */
	public Iterable<Puzzle> getSolution() {
		Stack<Puzzle> solutionSet = new Stack<Puzzle>();
		if (this.isSolvable()) {
			solutionSet.push(winner.board);
			PriorityObject po = winner;
			while (po.prev != null) {
				solutionSet.push(po.prev.board);
				po = po.prev;
			}
			Stack<Puzzle> solutionSetReverse = new Stack<Puzzle>();
			int lenght = solutionSet.size();
			for (int i = 0; i < lenght; i++) solutionSetReverse.push(solutionSet.pop());
			return solutionSetReverse;
		}
		else return solutionSet;
	}

	/**
	 * @author yusuf
	 * the class to store puzzle objects in an comparable way and
	 * also to make the suitable for a tree node which stores its parent
	 */
	private class PriorityObject implements Comparable<PriorityObject> {
		private Puzzle board;
		private int f;
		private PriorityObject prev;
		private int g;
		
		/**
		 * @param board
		 * @param g
		 * @param prev
		 * the constructor
		 */
		private PriorityObject(Puzzle board, int g, PriorityObject prev) {
			this.g = g;
			this.board = board;
			this.prev = prev;
			this.f = this.calculateF();
		}
		
		/**
		 * @return
		 */
		private int calculateF() {
			return this.g + this.board.h();
		}

		/**
		 *the compare method to implement comparable interface
		 */
		@Override
		public int compareTo(PriorityObject o) {
			return (this.f-o.f);
		}
		
		
	}

	// test client
	public static void main(String[] args) throws IOException {

		File input = new File("input.txt");
		Scanner sc = new Scanner(input);
		int lenght = sc.nextInt();
		int[][] newBoard = new int[lenght][lenght];
		for (int i = 0; i < lenght; i++) for (int j = 0; j < lenght; j++) newBoard[i][j] = sc.nextInt();
		sc.close();
		Puzzle initial = new Puzzle(newBoard);
		// Read this file int by int to create 
		// the initial board (the Puzzle object) from the file
		
		
		// solve the puzzle here. Note that the constructor of the Solver class already calls the 
		// solve method. So just create a solver object with the Puzzle Object you created above 
		// is given as argument, as follows:
		
		Solver solver = new Solver(initial);  // where initial is the Puzzle object created from input file

		// You can use the following part as it is. It creates the output file and fills it accordingly.
		File output = new File("output.txt");
		output.createNewFile();
		PrintStream write = new PrintStream(output);
		if (solver.isSolvable()) {
			write.println("Minimum number of moves = " + solver.getMoves());
			for (Puzzle board : solver.getSolution())
				write.println(board);
			
		}
		else write.println("Board is unsolvable");
		write.close();
	}
}

