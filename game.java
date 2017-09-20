
package iqpuzzle;

/**
 *
 * @author andrewrosen
 */
import java.util.ArrayList;
import java.util.Stack;

public class game
{
	public Stack instructions;
	
	private KeyboardReader r;
	
	public game(int x, int y, int n)
	{
		r = new KeyboardReader();
		int[] firstFalse = {x,y};
		instructions = new Stack();
		ArrayList<int[]> falsos = new ArrayList<int[]>();
		falsos.add(firstFalse);
		boolean[][] firstBoard = new boolean[n][];
		for(int i = 0; i < n; i++)
		{
			firstBoard[i] = new boolean[i+1];
			for(int j = 0; j < firstBoard[i].length; j++)firstBoard[i][j] = true;
		}
		firstBoard[x][y] = false;
		ArrayList<int[]> firstCos = getCoordinates(firstFalse, firstBoard);
		
		int number = getSum(n)-1;
		long begin = System.currentTimeMillis();
		if(solve(number, falsos, 0, firstCos, 0, firstBoard))
                {
                    System.out.println("Instructions: ");
                }
                else System.out.println("Not possible (based on size and starting false location)");
                long end = System.currentTimeMillis();
		while(!instructions.isEmpty())
		{
			System.out.println(instructions.pop());
		}
                double run_time = (end-begin)/1000.0;
                System.out.printf("Ran for %,.3f seconds.\n", run_time);
                boolean again = r.readLine("Run Sim Again? Y/N  ::  ").equals("Y");
                if(again)
                {
                    int x_ = r.readInt("X: ");
                    int y_ = r.readInt("Y: ");
                    int n_ = r.readInt("N: ");
                    game puzz = new game(x_,y_,n_);
                }
	}
	private boolean solve(int numLeft, ArrayList<int[]> falses, int falStart, ArrayList<int[]> jumpCoords, int jumpStart, boolean[][] board)
	{
		if(numLeft == 1)return true;
		if(falStart >= falses.size())
		{
			return false;
		}
		if(jumpCoords.size() == 0) jumpCoords = getCoordinates(falses.get(falStart), board);
		if(jumpStart >= jumpCoords.size())
		{
			return solve(numLeft, falses, falStart+1, new ArrayList<int[]>(), 0, board);
		}

		//cloning board and arrayLists
		ArrayList<int[]> falseCopy = new ArrayList<int[]>();
		ArrayList<int[]> coordsCopy = new ArrayList<int[]>();
		for(int[] loc : falses)
		{
			int[] current = {loc[0],loc[1]};
                        falseCopy.add(current);
		}
		for(int[] loc : jumpCoords)
		{
			int[] current = {loc[0],loc[1]};
			coordsCopy.add(current);
		}
		boolean[][] boardCopy = new boolean[board.length][];
		for(int i = 0; i < board.length; i++)
		{
			boardCopy[i] = new boolean[i+1];
			for(int j = 0; j < boardCopy[i].length; j++)boardCopy[i][j] = board[i][j];
		}
		
		//use it \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		int[] currentCoord = jumpCoords.get(jumpStart);
		int[] currentFalse = falses.get(falStart);
		int[] middle = getMiddle(currentCoord, currentFalse);
		if(board[middle[0]][middle[1]]) // if the middle location is occupied
		{
			boardCopy[currentCoord[0]][currentCoord[1]] = false;
			boardCopy[middle[0]][middle[1]] = false;
			boardCopy[currentFalse[0]][currentFalse[1]] = true;
			falseCopy.add(currentCoord);
			falseCopy.add(middle);
			falseCopy.remove(falStart);
			if(solve(numLeft-1, falseCopy, 0, getCoordinates(falseCopy.get(0), boardCopy), 0, boardCopy))
			{
				instructions.push("{" + currentCoord[0] + ", " + currentCoord[1] + "} -> {" + currentFalse[0] + ", " + currentFalse[1] + "}");
				return true;
			}
		}
		return solve(numLeft, falses, falStart, jumpCoords, jumpStart+1, board); 
	}
	
	// returns possible jump coords that have a peg
	private ArrayList<int[]> getCoordinates(int[] loc, boolean[][] board) 
	{
		ArrayList<int[]> cos = new ArrayList<int[]>();
                
                
		for(int i = loc[0] - 2; i <= loc[0] + 2; i+=2)
			for(int j = loc[1] -2; j <= loc[1] +2; j+=2)
				try{
                                        if((j == loc[1]+2 && i == loc[0]-2 )|| (j == loc[1]-2 && i == loc[0]+2))continue;
					if(board[i][j]&&!(i == loc[0] && j == loc[1]))//&&(i == loc[0] || j == loc[1]))
					{
						int[] current = {i,j};
						cos.add(current);
					}
				}catch(Exception e){}
		return cos;
	}
	
	// returns the middle coordinate
	private int[] getMiddle(int[] one, int[] two)
	{
		int x = (one[0] + two[0])/2;
		int y = (one[1] + two[1])/2;
		int[] result = {x,y};
		return result;
	}
	private void printBoard(boolean[][] b)
	{
		for(int i = 0; i < b.length; i++)
		{
			for(int j = 0; j < b[i].length; j++)
			{
				String s = (b[i][j])? "T" : "F";
				System.out.print(s + " ");
			}
			System.out.println();
		}
		r.pause();
	}
	private int getSum(int n)
	{
		if(n == 1)return 1;
		return n + getSum(n-1);
	}
}
