import java.util.Random;
/**
 * Describe your basic strategy here.
 * @author <RyBry>
 *
 */
public class MyAgent extends Agent {
  /**
   * A random number generator to randomly decide where to place a token.
   */
  private Random random;

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
	 int checkWin = iCanWin();
	 int checkLoose = theyCanWin();
	 //Check if they can win
	 if (checkLoose != -1) 
	 {
		 moveOnColumn(checkLoose);
	 }
	 else if (checkWin != -1) 
	 {
		 moveOnColumn(checkWin);
	 }
	 else moveOnColumn(randomMove());
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
  
  //Determine myAgent color. Check a column. If there is a matching colored tile at the top of the column, place another one in the row above it.
  public int iCanWin() {
	  //If i'm red, win for red
	  if(iAmRed)
	  {
		  System.out.println("I'm red!");
		  
		  //Win Vertically
		  for (int j = 0; j < myGame.getColumnCount(); j++) 
		  {
			  System.out.println("Checking Column: " + j);
			 //Find the lowest empty index for column j
			 int k = getLowestEmptyIndex(myGame.getColumn(j));
			 System.out.println("Lowest index: " + k);
			 Connect4Slot curSlot = myGame.getColumn(j).getSlot(k + 1);
			 
			 //Make sure current row doesn't exceed game boundaries
			 if(k != myGame.getRowCount() - 1 && k >= 0)
			 if(curSlot.getIsFilled())
			 {
				  //System.out.println("Slot: " + (k -1) + " Is filled! ");
				//Is the slot filled with the same color, return the column number
				if(curSlot.getIsRed()) 
				{
					System.out.println("Slot: " + (k) + " Is Red!");
					System.out.println("***********************");
					return j;
				}
			 } 
		  }
	  }
		System.out.println("No strategic move, choosing random");
    return -1;
    //TODO Fix issue where red will not try other column
    //TODO diagonal checking
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
	  //If I'm red, block yellows
	  if(iAmRed) 
	  {
		  
		//Win Vertically
		  for (int j = 0; j < myGame.getColumnCount(); j++) 
		  {
			  int k = getLowestEmptyIndex(myGame.getColumn(j));
				 //System.out.println("Lowest index: " + k);
				 Connect4Slot curSlot = myGame.getColumn(j).getSlot(k + 1);
				 //Check if curSlot is within game boundaries and is worth checking (3 above bottom)
				 if(k <= myGame.getColumnCount()- 5 && k >= 0)
					 if(curSlot.getIsFilled())
					 {
						//If 3 vertical slots in a row aren't red, take priority and place a red one on top 
						 if(!curSlot.getIsRed() && !myGame.getColumn(j).getSlot(k + 2).getIsRed() && !myGame.getColumn(j).getSlot(k + 3).getIsRed()) 
						 {
							 System.out.println("Blocked Opponent");
							 return j;
						 }
					 }
		  }
	  }
    return -1;
  }

  /**
   * Returns the name of this agent.
   *
   * @return the agent's name
   */
  public String getName() {
    return "My Agent";
  }
}
