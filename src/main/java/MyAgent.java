import java.util.Random;
/**
 * Describe your basic strategy here.
 * @author <RyBry>
 *
 *MyAgent is primarily defensive. 
 *It utilizes left and right sweeps to find "trigger" tokens
 *and then checks for a series of 3-token patterns(blocks) followed by 2-token patterns(prediction blocks).
 *
 *To look for winning moves, MyAgent simply changes it's color to iAmRedOp while calling the block methods
 *and then changes its color back to it's original before executing the move method.
 *
 *If no Winning nor blocking/prediction blocking moves are found, MyAgent will use a tryWin method to
 *fill up the middle column and create vertical stacks of like colored MyAgent tiles. 
 *
 *If no strategic move is found, MyAgent will pick a random spot to place on
 *
 */
public class MyAgent extends Agent {
	/**
	 * A random number generator to randomly decide where to place a token.
	 */
	private Random random;
	//For troubleshooting; print MyAgent moves to console
	private boolean printMoves = false;

	//Useful for switching block methods to attack methods
	private boolean iAmRedOp = !iAmRed;
	private boolean iAmRedOg = iAmRed;
	/**
	 * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
	 *
	 * @param game The game the agent will be playing.
	 * @param iAmRed True if the agent is Red, False if the agent is Yellow.
	 */
	public MyAgent(Connect4Game game, boolean iAmRed) {
		super(game, iAmRed);
		random = new Random();
	}

	private Connect4Slot slot(int j, int k) 
	{
		return myGame.getColumn(j).getSlot(k);
	}

	/**
	 * The move method is run every time it is this agent's turn in the game. You may assume that
	 * when move() is called, the game has at least one open slot for a token, and the game has not
	 * already been won.
	 *
	 * <p>By the end of the move method, the agent should have placed one token into the game at some
	 * point.</p>
	 *
	 * <p>After the move() method is called, the game engine will check to make sure the move was
	 * valid. A move might be invalid if:
	 * - No token was place into the game.
	 * - More than one token was placed into the game.
	 * - A previous token was removed from the game.
	 * - The color of a previous token was changed.
	 * - There are empty spaces below where the token was placed.</p>
	 *
	 * <p>If an invalid move is made, the game engine will announce it and the game will be ended.</p>
	 *
	 */
	public void move() {
		int iCanWin = iCanWin();
		//Check if: I can win, they can win, or if there is a possible future win.
		//If none are true, then choose random
		if(iCanWin != -1) 
		{
			if(printMoves) {System.out.println(" 											^1");}
			moveOnColumn(iCanWin);
			return;
		}

		int theyCanWin = theyCanWin();
		if(theyCanWin != -1) 
		{
			if(printMoves) {System.out.println(" 											^2");}
			moveOnColumn(theyCanWin);
			return;
		}

		int tryWin = tryWin();
		if(tryWin != -1) 
		{
			if(printMoves) {System.out.println(" 											^3");}
			moveOnColumn(tryWin);
			return;
		}
		if(printMoves) 
		{
			System.out.println("Choosing Random");	 
			System.out.println(" 											^4");
		}
		moveOnColumn(randomMove());
	}

	/**
	 * Drops a token into a particular column so that it will fall to the bottom of the column.
	 * If the column is already full, nothing will change.
	 *
	 * @param columnNumber The column into which to drop the token.
	 */
	public void moveOnColumn(int columnNumber) {
		// Find the top empty slot in the column
		// If the column is full, lowestEmptySlot will be -1
		int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));
		// if the column is not full
		if (lowestEmptySlotIndex > -1) {
			// get the slot in this column at this index
			Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);
			// If the current agent is the Red player...
			if (iAmRed) {
				lowestEmptySlot.addRed(); // Place a red token into the empty slot
			} else {
				lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
			}
		}
	}

	/**
	 * Returns the index of the top empty slot in a particular column.
	 *
	 * @param column The column to check.
	 * @return
	 *      the index of the top empty slot in a particular column;
	 *      -1 if the column is already full.
	 */
	public int getLowestEmptyIndex(Connect4Column column) {
		int lowestEmptySlot = -1;
		for  (int i = 0; i < column.getRowCount(); i++) {
			if (!column.getSlot(i).getIsFilled()) {
				lowestEmptySlot = i;
			}
		}
		return lowestEmptySlot;
	}

	/**
	 * Returns a random valid move. If your agent doesn't know what to do, making a random move
	 * can allow the game to go on anyway.
	 *
	 * @return a random valid move.
	 */
	public int randomMove() {
		int i = random.nextInt(myGame.getColumnCount());
		while (getLowestEmptyIndex(myGame.getColumn(i)) == -1) {
			i = random.nextInt(myGame.getColumnCount());
		}
		return i;
	}

	/**
	 * Returns the column that would allow the agent to win.
	 *
	 * <p>You might want your agent to check to see if it has a winning move available to it so that
	 * it can go ahead and make that move. Implement this method to return what column would
	 * allow the agent to win.</p>
	 *
	 * @return the column that would allow the agent to win.
	 */



	public int iCanWin()     //TODO Play to win horizontally & Diagonally
	{
		iAmRed = iAmRedOp;
		//Reverse the block method color to hunt for wins instead of blocks
		for(int j = 0; j < myGame.getColumnCount(); j++) 
		{
			for(int k = 0; k < myGame.getColumn(j).getRowCount(); k++) 
			{
				//If an opposite tile is found, run the checks
				if(slot(j, k).getIsFilled() && slot(j, k).getIsRed() != iAmRed) 
				{
					//If -1 is returned, no strategic move was found
					int valReturn = vertBlock(j);
					if(valReturn != -1) 
					{
						if(printMoves) 
						{
							System.out.println("^that was an intentional winning move!");
						}
						iAmRed = iAmRedOg;
						return valReturn;
					}

					valReturn = horzBlockR(j,k);
					if(valReturn != -1)
					{
						if(printMoves) 
						{ 
							System.out.println("^that was an intentional winning move!");
						}
						iAmRed = iAmRedOg;
						return valReturn;
					}

					valReturn = horzBlockL(j,k);
					if(valReturn != -1) 
					{
						if(printMoves) 
						{
							System.out.println("^that was an intentional winning move!");
						}
						iAmRed = iAmRedOg;
						return valReturn;
					}

					valReturn = diagBlockR(j,k);
					if(valReturn != -1) 
					{
						if(printMoves) 
						{
							System.out.println("^that was an intentional winning move!");
						}
						iAmRed = iAmRedOg;
						return valReturn;
					}

					valReturn = diagBlockL(j,k);
					if(valReturn != -1) 
					{
						if(printMoves) 
						{
							System.out.println("^that was an intentional winning move!");
						}
						iAmRed = iAmRedOg;
						return valReturn;
					}
				}
			}
		}
		iAmRed = iAmRedOg;
		return -1;
	}

	public int tryWin() 
	{
		iAmRed = iAmRedOg;
		//Fill up the middle column first
		int valReturn = playForMiddle();
		if(valReturn != -1) 
		{
			return valReturn;	
		}
		for (int j = 0; j < myGame.getColumnCount(); j++) 
		{
			//Vertical Streak
			valReturn = playVertical(j);
			if(valReturn != -1) 
			{
				return valReturn;	
			}
		}
		return -1;
	}

	private int playForMiddle() 
	{
		if(!myGame.getColumn(3).getIsFull()) 
		{
			if(printMoves) 
			{
				System.out.println("Placing on middle column");
			}
			return 3;
		}
		return -1;
	}

	private int playVertical(int j) 
	{
		//Find the lowest empty index for column j
		int k = getLowestEmptyIndex(myGame.getColumn(j));
		//Make sure current row doesn't exceed game boundaries
		if(k < myGame.getRowCount() - 1 && k >= 0) 
		{
			if(slot(j, k + 1).getIsFilled() && slot(j, k + 1).getIsRed() == iAmRed) 
			{
				//If the slot is filled with the same color, return the column number
				if(printMoves)
				{
					System.out.print("Advancing Vertical @(" + j + ", " + k + ")");
					System.out.println(" --- Triggered From (" + j + ", " + (k + 1) + ")");
				}
				return j;
			} 
		}
		return -1;
	}

	/**
	 * Returns the column that would allow the opponent to win.
	 *
	 * <p>You might want your agent to check to see if the opponent would have any winning moves
	 * available so your agent can block them. Implement this method to return what column should
	 * be blocked to prevent the opponent from winning.</p>
	 *
	 * @return the column that would allow the opponent to win.
	 */

	public int theyCanWin() {
		iAmRed = iAmRedOg;
		//Check all columns and their rows for a yellow
		for(int j = 0; j < myGame.getColumnCount(); j++) 
		{
			for(int k = 0; k < myGame.getColumn(j).getRowCount(); k++) 
			{

				//If an opposite tile is found, run the checks
				if(slot(j, k).getIsFilled() && slot(j, k).getIsRed() != iAmRed) 
				{
					/*If -1 is returned, no strategic move was found
					 **Defensive Blocks Below**/
					int valReturn = vertBlock(j);
					if(valReturn != -1) 
					{return valReturn;}

					valReturn = horzBlockR(j,k);
					if(valReturn != -1)
					{return valReturn;}

					valReturn = horzBlockL(j,k);
					if(valReturn != -1) 
					{return valReturn;}

					valReturn = diagBlockR(j,k);
					if(valReturn != -1) 
					{return valReturn;}

					valReturn = diagBlockL(j,k);
					if(valReturn != -1) 
					{return valReturn;}

					//**Prediction Blocks
					valReturn = vertPredict(j);
					if(valReturn != -1) 
					{return valReturn;}

					valReturn = horzPredictR(j, k);
					if(valReturn != -1) 
					{return valReturn;}

					valReturn = horzPredictL(j, k);
					if(valReturn != -1) 
					{return valReturn;}

					valReturn = diagPredictR(j, k);
					if(valReturn != -1) 
					{return valReturn;}

					valReturn = diagPredictL(j, k);
					if(valReturn != -1) 
					{return valReturn;}
				}
			}
		}
		return -1;
	}


	//****Simple Tile Checking Methods****
	//token1 is the trigger for these methods: slot(j, k)
	Connect4Slot token2;
	Connect4Slot token3;
	Connect4Slot tokenHole;
	Connect4Slot tokenHole2;

	private boolean isFilledEnemyToken(Connect4Slot token) 
	{
		if(token.getIsFilled() && token.getIsRed() != iAmRed) 
		{
			return true;
		}
		return false;
	}

	//First checks if hole slot is on ground to avoid arrayOutOfBounds
	//and then checks if there is a tile below the hole to be placed on-top of.
	private boolean isTokenBelow(int col, int row) 
	{
		if(row == myGame.getRowCount() - 1) 
		{
			return true;
		}
		else if(slot(col, row + 1).getIsFilled()) 
		{
			return true;
		}
		return false;
	}

	//Checks for two filled enemy tokens and a hole
	private boolean patternMatchesBlock(Connect4Slot token2, Connect4Slot token3, Connect4Slot tokenHole) 
	{
		if(isFilledEnemyToken(token2)) 
		{
			if(isFilledEnemyToken(token3)) 
			{
				if(!tokenHole.getIsFilled()) 
				{
					return true;
				}
			}
		}
		return false;
	}

	//Checks for one filled enemy token and two holes
	private boolean patternMatchesPredict(Connect4Slot token2, Connect4Slot tokenHole, Connect4Slot tokenHole2) 
	{
		if(isFilledEnemyToken(token2)) 
		{
			if(!tokenHole.getIsFilled()) 
			{
				if(!tokenHole2.getIsFilled()) 
				{
					return true;
				}
			}
		}
		return false;
	}
	//****END Simple Tile Checking Methods****

	
	
	//****Defensive Block methods****
	//Note: the first found tile that doesn't match MyAgent is what triggers these methods
	//so while there may appear to only be three checks, there are in fact 4 when including the trigger check
	private int diagBlockR(int j, int k) 
	{
		//Check restrictions to prevent array out of bounds
		if(j <= myGame.getColumnCount() - 4 && k >= 3)
		{
			//(Found one, hole, found two)
			tokenHole = slot(j+1, k-1);
			token2 = slot(j+2, k-2);
			token3 = slot(j+3, k-3);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k-1)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (one-hole-two) ---->^																THIS IS NEW");}
					return (j+1);
				}
			}	
			//(Found two, hole, found one)
			token2 = slot(j+1, k-1);
			tokenHole = slot(j+2, k-2);
			token3 = slot(j+3, k-3);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (two-hole-one) ---->^																THIS IS NEW");}
					return (j+2);
				}
			}
			//(Found three, hole)
			token2 = slot(j+1, k-1);
			token3 = slot(j+2, k-2);
			tokenHole = slot(j+3, k-3);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+3, k-3)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (three-hole) ---->^																THIS IS NEW");}
					return (j+3);
				}
			}
		}
		//(hole, found three)
		//Note: Different restriction to take into account checking behind the trigger token
		if(j > 0 && j < myGame.getColumnCount() - 2 && k > 1 && k < myGame.getRowCount() - 1) 
		{
			tokenHole = slot(j-1, k+1);
			token2 = slot(j+1, k-1);
			token3 = slot(j+2, k-2);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-1, k+1)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (hole-three) ---->^																THIS IS NEW");}
					return (j-1);
				}
			}
		}	
		return -1;
	}

	private int diagBlockL(int j, int k) 
	{
		//token1 is the trigger for this method: slot(j, k)
		//Check restrictions to prevent array out of bounds
		if(j > 2 && k > 2)
		{
			//(Found one, hole, found two)
			tokenHole = slot(j-1, k-1);
			token2 = slot(j-2, k-2);
			token3 = slot(j-3, k-3);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-1, k-1)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (one-hole-two) ^<----																THIS IS NEW");}
					return (j-1);
				}
			}	
			//(Found two, hole, found one)
			token2 = slot(j-1, k-1);
			tokenHole = slot(j-2, k-2);
			token3 = slot(j-3, k-3);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (two-hole-one) ^<----																THIS IS NEW");}
					return (j-2);
				}
			}
			//(Found three, hole)
			token2 = slot(j-1, k-1);
			token3 = slot(j-2, k-2);
			tokenHole = slot(j-3, k-3);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-3, k-3)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (three-hole) ^<----																THIS IS NEW");}
					return (j-3);
				}
			}	
		}
		//(hole, found three)
		//Note: Different restriction to take into account checking behind the trigger token
		if(j > 1 && j < myGame.getColumnCount() - 1 && k > 1 && k < myGame.getRowCount() - 1)  
		{
			tokenHole = slot(j+1, k+1);
			token2 = slot(j-1, k-1);
			token3 = slot(j-2, k-2);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k+1)) 
				{
					if(printMoves)
					{System.out.println("Diag Block (hole-three) ^<----																THIS IS NEW");}
					return (j+1);
				}
			}
		}
		return -1;
	}

	private int vertBlock(int j) 
	{
		Connect4Slot token4; //Only needed for this method
		//Get the lowest empty index and search below it as the first check
		int k = getLowestEmptyIndex(myGame.getColumn(j));
		//Check if target slot is at least 4 from bottom
		if(k < myGame.getRowCount() - 3 && k >= 0)
		{
			token2 = slot(j, k+1);
			token3 = slot(j, k+2);
			token4 = slot(j, k+3);			
			if(isFilledEnemyToken(token2) && isFilledEnemyToken(token3) && isFilledEnemyToken(token4))
			{
				if(printMoves)
				{System.out.println("Vertical Block Column: " + j);}
				return (j);
			}
		}
		return -1;
	}

	private int horzBlockR(int j, int k) 
	{
		Connect4Slot token2;
		Connect4Slot token3;
		Connect4Slot tokenHole;
		//Check Restrictions first
		if(j < myGame.getColumnCount() - 3)
		{
			//(Found one, hole, found two)
			token2 = slot(j+2, k);
			token3 = slot(j+3, k);
			tokenHole = slot(j+1, k);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k)) 
				{
					if(printMoves)
					{System.out.println("Horz Block (one-hole-two) --->																THIS IS NEW");}
					return (j+1);
				}
			}
			//(Found two, hole, found one)
			token2 = slot(j+1, k);
			token3 = slot(j+3, k);
			tokenHole = slot(j+2, k);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+2, k)) 
				{
					if(printMoves)
					{System.out.println("Horz Block (two-hole-one) ---->																THIS IS NEW");}
					return (j+2);
				}
			}
			//(Found three, hole)
			token2 = slot(j+1, k);
			token3 = slot(j+2, k);
			tokenHole = slot(j+3, k);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+3, k)) 
				{
					if(printMoves)
					{System.out.println("Horz Block (three-hole) ---->																THIS IS NEW");}
					return (j+3);
				}
			}
		}
		return -1;
	}

	//Note: Since (one-hole-two) reversed is (two-hole-one), and there is no vertical difference between looking left or right,
	//checking both of those here are not required, as it will be caught in horzBlockR before this method is called
	private int horzBlockL(int j, int k) 
	{
		//Check Restrictions first
		if(j > 2)
		{
			//(Found three, hole)
			token2 = slot(j-1, k);
			token3 = slot(j-2, k);
			tokenHole = slot(j-3, k);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-3, k)) 
				{
					if(printMoves)
					{System.out.println("Horz Block (three-hole) <----																THIS IS NEW");}
					return (j-3);
				}
			}
		}  
		return -1;
	}
	//****END Defensive Block methods****
	
	
	

	//****Prediction Block methods****
	private int vertPredict(int j) 
	{
		//Get the lowest empty index and search below it as the first check
		int k = getLowestEmptyIndex(myGame.getColumn(j));
		//Check if slot is at least 3 from bottom
		if(k < myGame.getRowCount() - 2 && k >= 0)
		{
			token2 = slot(j, k+1);
			token3 = slot(j, k+2);		
			if(isFilledEnemyToken(token2) && isFilledEnemyToken(token3))
			{
				if(printMoves)
				{System.out.println("Vertical *Prediction* Block Column: " + j);}
				return (j);
			}
		}
		return -1;
	}

	private int horzPredictR(int j, int k)
	{
		//Check Restrictions first
		//this won't work on the ground level
		if(j < myGame.getColumnCount() -3) 
		{
			//(Found one, hole, found two)
			token2 = slot(j+2, k);
			token3 = slot(j+3, k);
			tokenHole = slot(j+1, k);			
			if(patternMatchesBlock(token2, token3, tokenHole))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k)) 
				{
					if(printMoves)
					{System.out.println("Horz Block (one-hole-two) --->																THIS IS NEW");}
					return (j+1);
				}
			}

			//(found two, hole*2)
			token2 = slot(j+1, k);
			tokenHole = slot(j+2, k);
			tokenHole2 = slot(j+3, k);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+2, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (two-hole[here]-hole)---->																THIS IS NEW");}
					return (j+2);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j+3, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (two-hole-hole[here])---->																THIS IS NEW");}
					return (j+3);
				}
			}  
			//(found one, hole, found one, hole)
			token2 = slot(j+2, k);
			tokenHole = slot(j+1, k);
			tokenHole2 = slot(j+3, k);
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (one-hole[here]-one-hole)---->																THIS IS NEW");}
					return (j+1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j+3, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (one-hole-one-hole[here])---->															THIS IS NEW");}
					return (j+3);
				}
			}
		}
		//(hole, found one[this is the trigger], hole, found one)
		//Note: Different restriction to take into account checking behind the trigger token
		if(j > 0 && j < myGame.getColumnCount() - 2) 
		{
			token2 = slot(j+2, k);
			tokenHole = slot(j+1, k);
			tokenHole2 = slot(j-1, k);
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (hole-one-hole[here]-one)---->																THIS IS NEW");}
					return (j+1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j-1, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (hole[here]-one-hole-one)---->															THIS IS NEW");}
					return (j-1);
				}
			}
		}
		return -1;
	}

	//Note that since (one-hole-one-hole) reversed is (hole-one-hole-one), there is no need for either method to be checked
	//in this left sweep, as it will be caught in horzPredictR before this method is called
	private int horzPredictL(int j, int k)
	{
		//(found two, hole*2)
		//Check Restrictions First
		if(j > 2) 
		{
			token2 = slot(j-1, k);
			tokenHole = slot(j-2, k);
			tokenHole2 = slot(j-3, k);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-2, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (two-hole[here]-hole)<----																THIS IS NEW");}
					return (j-2);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j-3, k)) 
				{
					if(printMoves)
					{System.out.println("horizontal *Prediction* Block (two-hole-hole[here])<----																THIS IS NEW");}
					return (j-3);
				}
			}
		}
		return -1;
	}

	private int diagPredictR(int j, int k) 
	{  
		//Check Restrictions first
		if(j < myGame.getColumnCount() -3 && k > 2) 
		{
			//(found one, hole, found one, hole)
			token2 = slot(j+2, k-2);
			tokenHole = slot(j+1, k-1);
			tokenHole2 = slot(j+3, k-3);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k-1)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole[here]-one-hole) ---->^																THIS IS NEW");}
					return (j+1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j+3, k-3)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole-one-hole[here]) ---->^																THIS IS NEW");}
					return (j+3);
				}
			}

			//(found two, two holes)
			token2 = slot(j+1, k-1);
			tokenHole = slot(j+2, k-2);
			tokenHole2 = slot(j+3, k-3);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (two-hole[here]-hole) ---->^																THIS IS NEW");}
					return (j+2);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j+3, k-3)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (two-hole-hole[here]) ---->^																THIS IS NEW");}
					return (j+3);
				}
			}

			//(found one, hole*2, found one)	  
			token2 = slot(j+3, k-3);
			tokenHole = slot(j+1, k-1);
			tokenHole2 = slot(j+2, k-2);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k-1)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole[here]-hole-one) ---->^																THIS IS NEW");}
					return (j+1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j+2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole-hole[here]-one) ---->^																THIS IS NEW");}
					return (j+2);
				}
			}
		}

		//(hole, found two, hole)
		//Note: Different restriction to take into account checking behind the trigger token
		if(j > 0 && j < myGame.getColumnCount() -2 && k > 1 && k < myGame.getRowCount() - 1) 
		{
			token2 = slot(j+1, k-1);
			tokenHole = slot(j-1, k+1);
			tokenHole2 = slot(j+2, k-2);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-1, k+1)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (hole[here]-two-hole) ---->^																THIS IS NEW");}
					return (j-1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j+2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (hole-two-hole[here]) ---->^																THIS IS NEW");}
					return (j+2);
				}
			}
		}		  
		return -1;
	}

	private int diagPredictL(int j, int k) 
	{
		//Check Restrictions first
		if(j >= 3 && k >= 3) 
		{
			//(found one, hole, found one, hole) [place on column that has a tile filled below the empty slot]
			token2 = slot(j-2, k-2);
			tokenHole = slot(j-1, k-1);
			tokenHole2 = slot(j-3, k-3);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-1, k-1)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole[here]-one-hole) ^<----																THIS IS NEW");}
					return (j-1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j-3, k-3)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole-one-hole[here]) ^<----																THIS IS NEW");}
					return (j-3);
				}
			}

			//(found two, two holes)
			token2 = slot(j-1, k-1);
			tokenHole = slot(j-2, k-2);
			tokenHole2 = slot(j-3, k-3);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (two-hole[here]-hole) ^<----																THIS IS NEW");}
					return (j-2);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j-3, k-3)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (two-hole-hole[here]) ^<----																THIS IS NEW");}
					return (j-3);
				}
			}

			//(found one, hole*2, found one)	  	  
			token2 = slot(j-3, k-3);
			tokenHole = slot(j-1, k-1);
			tokenHole2 = slot(j-2, k-2);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j-1, k-1)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole[here]-hole-one)^<----																THIS IS NEW");}
					return (j-1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j-2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (one-hole-hole[here]-one)^<----																THIS IS NEW");}
					return (j-2);
				}
			}
		} 
		//(hole, one[trigger], one, hole) [place on column that has a tile filled below the empty slot]
		//Note: Different restriction to take into account checking behind the trigger token
		if(j > 1 && j < myGame.getColumnCount() -2 && k > 1 && k < myGame.getRowCount() - 1) 
		{
			token2 = slot(j-1, k-1);
			tokenHole = slot(j+1, k+1);
			tokenHole2 = slot(j-2, k-2);			
			if(patternMatchesPredict(token2, tokenHole, tokenHole2))
			{
				//Check if there is a token below tokenHole
				if(isTokenBelow(j+1, k+1)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (hole[here]-two-hole) ^<----																THIS IS NEW");}
					return (j+1);
				}
				//Check if there is a token below tokenHole2
				else if(isTokenBelow(j-2, k-2)) 
				{
					if(printMoves)
					{System.out.println("Diagonal *Prediction* Block (hole-two-hole[here]) ^<----																THIS IS NEW");}
					return (j-2);
				}
			}
		}
		return -1;
	}
	//****END Prediction Block methods****

	/**
	 * Returns the name of this agent.
	 *
	 * @return the agent's name
	 */
	public String getName() {
		return "My Agent";
	}
}
