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
	//  System.out.println("I am Red: " + iAmRed);
	 int checkWin = iCanWin();
	 int checkLoose = theyCanWin();
	 //Check if they can win first
	 if (checkLoose != -1) 
	 {
		 moveOnColumn(checkLoose);
	 }
	 else if (checkWin != -1) 
	 {
		 moveOnColumn(checkWin);
	 }
	 else if(!myGame.getColumn(3).getIsFull()) 
	 {
		 moveOnColumn(3);
	//	 System.out.println("Going for the Middle");
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
  

  public int iCanWin()     //TODO Play to win horizontally & Diagonally
  {
		  for (int j = 0; j < myGame.getColumnCount(); j++) 
		  {
			int valReturn = playVertical(j);
			if(valReturn != -1) 
			{return valReturn;}
		  }
    return -1;
  }
  
  private int playVertical(int j) 
  {
	  //Find the lowest empty index for column j
		 int k = getLowestEmptyIndex(myGame.getColumn(j));
		 Connect4Slot curSlot = myGame.getColumn(j).getSlot(k + 1); 
		 //Make sure current row doesn't exceed game boundaries
		 if(k != myGame.getRowCount() - 1 && k >= 0)
		 if(curSlot.getIsFilled())
		 {
			//If the slot is filled with the same color, return the column number
			if(curSlot.getIsRed() == iAmRed) 
			{
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
		  //Check all columns and their rows for a yellow
		  for(int j = 0; j < myGame.getColumnCount(); j++) 
		  {
			  for(int k = 0; k < myGame.getColumn(j).getRowCount(); k++) 
			  {
				  Connect4Slot initSlot = myGame.getColumn(j).getSlot(k);
				  //If an opposite tile is found, run the checks
				  if(initSlot.getIsFilled() && initSlot.getIsRed() != iAmRed) 
				  {
					//Check to see if a strategic column was chosen (!= -1)
					 int valReturn = verticalBlock(j);
					 if(valReturn != -1) 
					 {return valReturn;}
					  
					 valReturn = rightDiagonal(j,k);
					 if(valReturn != -1) 
					 {return valReturn;}
					 
					 valReturn = leftDiagonal(j,k);
					 if(valReturn != -1) 
					 {return valReturn;}
					 
					 valReturn = horizontalBlockR(j,k);
					 if(valReturn != -1)
					 {return valReturn;}
					 
					 valReturn = horizontalBlockL(j,k);
					 if(valReturn != -1) 
					 {return valReturn;}
				  }
			  }
		  }
    return -1;
  }
  
  private int verticalBlock(int j) {
		  int k = getLowestEmptyIndex(myGame.getColumn(j));
			 //System.out.println("Lowest index: " + k);
			 Connect4Slot curSlot = myGame.getColumn(j).getSlot(k + 1);
			 //Check if curSlot is within game boundaries and is worth checking (3 above bottom)
			 if(k <= myGame.getColumnCount()- 5 && k >= 0)
				 if(curSlot.getIsFilled())
				 {
					//If 3 vertical slots are opposite color, place a token on top 
					 if(curSlot.getIsRed() != iAmRed && myGame.getColumn(j).getSlot(k + 2).getIsRed() != iAmRed && myGame.getColumn(j).getSlot(k + 3).getIsRed() != iAmRed) 
					 {
						 System.out.println("Vertical Block");
						 return j;
					 }
			 }
	  return -1;
  }
  
  private int rightDiagonal(int j, int k)
  {
		  //Check restrictions first
		  if(j <= myGame.getColumnCount() - 4 && k >= 3)
		  {
			  //**2 Pattern Block**
			  //If the one to the right column and up a row is filled & not equal to myAgent color...
			  if(myGame.getColumn(j + 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j + 1).getSlot(k - 1).getIsRed() != iAmRed) 
			  {
				  //If the next column over and a row up is empty, but there is a tile below it, place a tile on-top!
				  if(!myGame.getColumn(j + 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j + 2).getSlot(k - 1).getIsFilled()) 
				  {
					  System.out.println("Diagonal Block ---->^ ");
					  return (j + 2);
				  }
			  }
			  
			  //**Prediction Block**
			  //If there is an empty slot in a potential diagonal that has a tile under it, continue
			  if(!myGame.getColumn(j + 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j + 1).getSlot(k).getIsFilled()) 
			  {
				  //if the diagonal after the empty is also not the same color as MyAgent, place a column in the previous empty slot
				  if(myGame.getColumn(j + 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j + 2).getSlot(k - 2).getIsRed() != iAmRed) 
				  {
					  System.out.println("Prediction BlockR ---->^");
					  return (j + 1);
				  }
			  }
		  }
	  return -1;
  }
  
  private int leftDiagonal(int j, int k) 
  {
		  //Check restrictions first
		  if(j >= 3 && k >= 3)
		  {
			  //If the one to the left column and up a row is filled & not myAgent Color...
			  if(myGame.getColumn(j - 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j - 1).getSlot(k - 1).getIsRed() != iAmRed) 
			  {
				  //If the next column over and a row up is empty, but there is a tile below it, place a tile on-top!
				 if(!myGame.getColumn(j - 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j - 2).getSlot(k - 1).getIsFilled()) 
				 {
					  System.out.println("Diagonal Block ^<----");
					  return (j - 2);
				 }
			  }
			  
			  //**Prediction Block**
			  //If there is an empty slot in a potential diagonal that has a tile under it, continue
			  if(!myGame.getColumn(j - 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j - 1).getSlot(k).getIsFilled()) 
			  {
				  //if the diagonal after the empty is also not the same color as MyAgent, place a column in the previous empty slot
				  if(myGame.getColumn(j - 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j - 2).getSlot(k - 2).getIsRed() != iAmRed) 
				  {
					  System.out.println("Prediction BlockL ^<----");
					  return (j - 1);
				  }
			  }
		  }
	  return -1;
  }
  
  private int horizontalBlockR(int j, int k) 
  {
	  //Check Restrictions first
	  if(j <= myGame.getColumnCount() -4)
	  {
	  //If the slot to the right is filled and it is not the same color as MyAgent
		  if(myGame.getColumn(j + 1).getSlot(k).getIsFilled() && myGame.getColumn(j + 1).getSlot(k).getIsRed() != iAmRed) 
		  {
			  //Check: is the current row on the bottom? in which case don't check for an outOfBounds array element
			  if(k + 1 >= myGame.getRowCount()) 
			  {
				  //If on the bottom, just check if the next slot is filled
				  if(!myGame.getColumn(j + 2).getSlot(k).getIsFilled()) 
				  {
					  System.out.println("Ground Right Block ---->");
					  return (j + 2);
				  }
			  }
			  //if not on the bottom, check if the next slot is empty and the one below it is full
			  else if(!myGame.getColumn(j + 2).getSlot(k).getIsFilled() && myGame.getColumn(j + 2).getSlot(k + 1).getIsFilled()) 
			  {
				  System.out.println("Sky Right Block ---->");
				  return (j + 2);
			  }
		  }
	  }
	  return -1;
  }
  
  private int horizontalBlockL(int j, int k)
  {
	//Check Restrictions first
	  if(j >= 3)
	  {
	  //If the slot to the left is filled and it is not the same color as MyAgent
		  if(myGame.getColumn(j - 1).getSlot(k).getIsFilled() && myGame.getColumn(j - 1).getSlot(k).getIsRed() != iAmRed) 
		  {
			  //Check: is the current row on the bottom? in which case don't check for an outOfBounds array element
			  if(k + 1 >= myGame.getRowCount()) 
			  {
				  //If on the bottom, just check if the next slot is filled
				  if(!myGame.getColumn(j - 2).getSlot(k).getIsFilled()) 
				  {
					  System.out.println("Ground Left Block <----");
					  return (j - 2);
				  }
			  }
			  //if not on the bottom, check if the next slot is empty and the one below it is full
			  else if(!myGame.getColumn(j - 2).getSlot(k).getIsFilled() && myGame.getColumn(j - 2).getSlot(k + 1).getIsFilled()) 
			  {
				  System.out.println("Sky Left Block <----");
				  return (j - 2);
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
