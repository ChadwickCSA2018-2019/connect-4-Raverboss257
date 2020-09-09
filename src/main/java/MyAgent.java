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
  private boolean printMoves = true;
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
	 int checkLose = theyCanWin();
	 //Check if they can win first
	 if (checkLose != -1) 
	 {
		 moveOnColumn(checkLose);
	 }
	 else if (checkWin != -1) 
	 {
		 moveOnColumn(checkWin);
	 }
	/* else if(!myGame.getColumn(3).getIsFull()) 
	 {
		 moveOnColumn(3);
		 if(printMoves)
		 {System.out.println("Going for the Middle");}
	 }*/
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
		 //Make sure current row doesn't exceed game boundaries
		 if(k != myGame.getRowCount() - 1 && k >= 0)
		 if(myGame.getColumn(j).getSlot(k + 1).getIsFilled() && myGame.getColumn(j).getSlot(k + 1).getIsRed() != iAmRed) 
		 {
			//If the slot is filled with the same color, return the column number
			if(printMoves)
			{System.out.println("Advancing Vertical");}
			return j;
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
				  }
			  }
		  }
    return -1;
  }
  
  //Defensive Block methods
  //Note: the first found tile that doesn't match MyAgent is what triggers these methods
  //so while there may appear to only be three checks, there are in fact 4 when including the trigger check
  private int vertBlock(int j) 
  {
	  //Get the lowest empty index and search below it as the first check
	  int k = getLowestEmptyIndex(myGame.getColumn(j));
	  Connect4Slot curSlot = myGame.getColumn(j).getSlot(k + 1);
	  //Check if curSlot is within game boundaries and is at least 3 above bottom
		 if(k <= myGame.getRowCount()- 4 && k >= 0)
			//If 3 vertical slots are filled of opposite MyAgent color, place a token on top 
			 if(curSlot.getIsFilled() && curSlot.getIsRed() != iAmRed) 
				 if(myGame.getColumn(j).getSlot(k + 2).getIsFilled() && myGame.getColumn(j).getSlot(k + 2).getIsRed() != iAmRed)
					 if(myGame.getColumn(j).getSlot(k + 3).getIsFilled() && myGame.getColumn(j).getSlot(k + 3).getIsRed() != iAmRed)
					 	{
						 	if(printMoves)
						 	{System.out.println("Vertical Block");}
						 	return j;
					 	}
	  return -1;
  }
  
  private int horzBlockR(int j, int k) 
  {
	//Check Restrictions first
	  if(j <= myGame.getColumnCount() -4)
	  {
		  //(Found one, hole, found two)
		  if(!myGame.getColumn(j + 1).getSlot(k).getIsFilled())
			  if(myGame.getColumn(j + 2).getSlot(k).getIsFilled() && myGame.getColumn(j + 2).getSlot(k).getIsRed() != iAmRed)
				  if(myGame.getColumn(j + 3).getSlot(k).getIsFilled() && myGame.getColumn(j + 3).getSlot(k).getIsRed() != iAmRed) 
					  //check if slot is on bottom to prevent array.outOfBounds
					  if(k == myGame.getRowCount() -1)
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Ground (one-hole-two) --->");}
						  return (j + 1);
					  }
		  			  //if it's mid-air, make sure there is a block below where to place
					  else if(myGame.getColumn(j + 1).getSlot(k + 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Air (one-hole-two) ---->");}
						  return (j + 1); 
					  }
		  //(Found two, hole, found one)
		  if(myGame.getColumn(j + 1).getSlot(k).getIsFilled() && myGame.getColumn(j + 1).getSlot(k).getIsRed() != iAmRed)
			  if(!myGame.getColumn(j + 2).getSlot(k).getIsFilled())
				  if(myGame.getColumn(j + 3).getSlot(k).getIsFilled() && myGame.getColumn(j + 3).getSlot(k).getIsRed() != iAmRed) 
					//check if slot is on bottom to prevent array.outOfBounds
					  if(k == myGame.getRowCount() -1)
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Ground (two-hole-one) ---->");}
						  return (j + 2);
					  }
		  			  //if it's mid-air, make sure there is a block below where to place
					  else if(myGame.getColumn(j + 2).getSlot(k + 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Air (two-hole-one) ---->");}
						  return (j + 2); 
					  } 
		  //(Found three, hole)
		  if(myGame.getColumn(j + 1).getSlot(k).getIsFilled() && myGame.getColumn(j + 1).getSlot(k).getIsRed() != iAmRed)
			  if(myGame.getColumn(j + 2).getSlot(k).getIsFilled() && myGame.getColumn(j + 2).getSlot(k).getIsRed() != iAmRed)
				  if(!myGame.getColumn(j + 3).getSlot(k).getIsFilled())
					  if(k == myGame.getRowCount() -1)
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Ground (three-one) ---->");}
						  return (j + 3);
					  }
		  			  //if it's mid-air, make sure there is a block below where to place
					  else if(myGame.getColumn(j + 3).getSlot(k + 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Air (three-one) ---->");}
						  return (j + 3); 
					  } 
	  }
	  return -1;
  }
  
  private int horzBlockL(int j, int k) 
  {
		//Check Restrictions first
	  	if(j >= 3)
		  {
	  		//Note: Since (one-hole-two) reversed is (two-hole-one), and there is no vertical difference between looking left or right,
	  		//checking both of those here are not required, as it will be caught in the right horizontal block before this method is called
	  		//(Found three, hole)
			  if(myGame.getColumn(j - 1).getSlot(k).getIsFilled() && myGame.getColumn(j - 1).getSlot(k).getIsRed() != iAmRed)
				  if(myGame.getColumn(j - 2).getSlot(k).getIsFilled() && myGame.getColumn(j - 2).getSlot(k).getIsRed() != iAmRed)
					  if(!myGame.getColumn(j - 3).getSlot(k).getIsFilled())
						  if(k == myGame.getRowCount() -1)
						  {
							  if(printMoves)
							  {System.out.println("Horz Block Ground (three-one) <----");}
							  return (j - 3);
						  }
			  			  //if it's mid-air, make sure there is a block below where to place
						  else if(myGame.getColumn(j - 3).getSlot(k + 1).getIsFilled()) 
						  {
							  if(printMoves)
							  {System.out.println("Horz Block Air (three-one) <----");}
							  return (j - 3); 
						  } 
		  }  
		  return -1;
  }
  
  private int diagBlockR(int j, int k) 
  {
	  //Check restrictions first
	  if(j <= myGame.getColumnCount() - 4 && k >= 3)
	  {
		  //(Found one, hole, found two)
		  if(!myGame.getColumn(j + 1).getSlot(k - 1).getIsFilled())
			  if(myGame.getColumn(j + 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j + 2).getSlot(k - 2).getIsRed() != iAmRed)
				  if(myGame.getColumn(j + 3).getSlot(k - 3).getIsFilled() && myGame.getColumn(j + 3).getSlot(k - 3).getIsRed() != iAmRed) 
					  //If there is a slot below the empty slot, go there
					  	if(myGame.getColumn(j + 1).getSlot(k).getIsFilled()) 
					  	{
					  		if(printMoves)
							{System.out.println("Diag Block (one-hole-two) ---->^");}
					  		return (j + 1);
					  	}
		  				//TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
		  
		  //(Found two, hole, found one)
		  if(myGame.getColumn(j + 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j + 1).getSlot(k - 1).getIsRed() != iAmRed)
			  if(!myGame.getColumn(j + 2).getSlot(k - 2).getIsFilled())
				  if(myGame.getColumn(j + 3).getSlot(k - 3).getIsFilled() && myGame.getColumn(j + 3).getSlot(k - 3).getIsRed() != iAmRed)
					  if(myGame.getColumn(j + 2).getSlot(k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (two-hole-one) ---->^");}
						  return (j + 2);
					  }
		  			//TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
		//(Found three, hole)
		  if(myGame.getColumn(j + 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j + 1).getSlot(k - 1).getIsRed() != iAmRed)
			  if(myGame.getColumn(j + 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j + 2).getSlot(k - 2).getIsRed() != iAmRed)
				  if(!myGame.getColumn(j + 3).getSlot(k - 3).getIsFilled())
					  if(myGame.getColumn(j + 3).getSlot(k - 2).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (three-one) ---->^");}
						  return (j + 3);
					  }
	  }
	  return -1;
  }
  
  private int diagBlockL(int j, int k) 
  {
	  //Check restrictions first
	  if(j >= 3 && k >= 3)
	  {
		  //(Found one, hole, found two)
		  if(!myGame.getColumn(j - 1).getSlot(k - 1).getIsFilled())
			  if(myGame.getColumn(j - 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j - 2).getSlot(k - 2).getIsRed() != iAmRed)
				  if(myGame.getColumn(j - 3).getSlot(k - 3).getIsFilled() && myGame.getColumn(j - 3).getSlot(k - 3).getIsRed() != iAmRed) 
					  //If there is a slot below the empty slot, go there
					  	if(myGame.getColumn(j - 1).getSlot(k).getIsFilled()) 
					  	{
					  		if(printMoves)
							{System.out.println("Diag Block (one-hole-two) ^<----");}
					  		return (j - 1);
					  	}
		  				//TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
		  
		  //(Found two, hole, found one)
		  if(myGame.getColumn(j - 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j - 1).getSlot(k - 1).getIsRed() != iAmRed)
			  if(!myGame.getColumn(j - 2).getSlot(k - 2).getIsFilled())
				  if(myGame.getColumn(j - 3).getSlot(k - 3).getIsFilled() && myGame.getColumn(j - 3).getSlot(k - 3).getIsRed() != iAmRed)
					  if(myGame.getColumn(j - 2).getSlot(k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (two-hole-one) ^<----");}
						  return (j - 2);
					  }
		  			//TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
		//(Found three, hole)
		  if(myGame.getColumn(j - 1).getSlot(k - 1).getIsFilled() && myGame.getColumn(j - 1).getSlot(k - 1).getIsRed() != iAmRed)
			  if(myGame.getColumn(j - 2).getSlot(k - 2).getIsFilled() && myGame.getColumn(j - 2).getSlot(k - 2).getIsRed() != iAmRed)
				  if(!myGame.getColumn(j - 3).getSlot(k - 3).getIsFilled())
					  if(myGame.getColumn(j - 3).getSlot(k - 2).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (three-one) ^<----");}
						  return (j - 3);
					  }
	  }
	  return -1;
  }
  
  
  //******OLD METHODS******
  
  
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
					  if(printMoves)
					  {System.out.println("Diagonal Block ---->^ ");}
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
					  if(printMoves)
					  {System.out.println("Prediction BlockR ---->^ (" + j + ", " + k + ")");}
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
					  if(printMoves)
					  {System.out.println("Diagonal Block ^<----");}
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
					  if(printMoves)
					  {System.out.println("Prediction BlockL ^<---- (" + j + ", " + k + ")");}
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
			  //is the slot two columns over empty or filled with the opposite color?
			  if(!myGame.getColumn(j + 3).getSlot(k).getIsFilled() || myGame.getColumn(j + 3).getSlot(k).getIsRed() != iAmRed) 
			  {
				  //Check: is the current row on the bottom? in which case don't check for an outOfBounds array element
				  if(k + 1 >= myGame.getRowCount()) 
				  {
					  //If on the bottom, just check if the next slot is filled
					  if(!myGame.getColumn(j + 2).getSlot(k).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Ground Right Block ---->");}
						  return (j + 2);
					  }
				  }
				  //if not on the bottom, check if the next slot is empty and the one below it is full
				  else if(!myGame.getColumn(j + 2).getSlot(k).getIsFilled() && myGame.getColumn(j + 2).getSlot(k + 1).getIsFilled()) 
				  {
					  if(printMoves)
					  {System.out.println("Sky Right Block ---->");}
					  return (j + 2);
				  }
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
			  //is the slot two columns over empty or filled with the opposite color?
			  if(!myGame.getColumn(j - 3).getSlot(k).getIsFilled() || myGame.getColumn(j - 3).getSlot(k).getIsRed() != iAmRed) 
			  {
				  //Check: is the current row on the bottom? in which case don't check for an outOfBounds array element
				  if(k + 1 >= myGame.getRowCount()) 
				  {
					  //If on the bottom, just check if the next slot is filled
					  if(!myGame.getColumn(j - 2).getSlot(k).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Ground Left Block <----");}
						  return (j - 2);
					  }
				  }
				  //if not on the bottom, check if the next slot is empty and the one below it is full
				  else if(!myGame.getColumn(j - 2).getSlot(k).getIsFilled() && myGame.getColumn(j - 2).getSlot(k + 1).getIsFilled()) 
				  {
					  if(printMoves)
					  {System.out.println("Sky Left Block <----");}
					  return (j - 2);
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
