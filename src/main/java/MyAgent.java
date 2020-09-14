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
	 else if(!myGame.getColumn(3).getIsFull()) 
	 {
		 moveOnColumn(3);
		 if(printMoves)
		 {System.out.println("Going for the Middle");}
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
  
  //Defensive Block methods
  //Note: the first found tile that doesn't match MyAgent is what triggers these methods
  //so while there may appear to only be three checks, there are in fact 4 when including the trigger check
  private int vertBlock(int j) 
  {
	  //Get the lowest empty index and search below it as the first check
	  int k = getLowestEmptyIndex(myGame.getColumn(j));
	  //Check if curSlot is within game boundaries and is at least 3 above bottom
		 if(k <= myGame.getRowCount()- 4 && k >= 0)
		 {
			//If 3 vertical slots are filled of opposite MyAgent color, place a token on top 
			 if(slot(j, k + 1).getIsFilled() && slot(j, k + 1).getIsRed() != iAmRed) 
			 {
				 if(slot(j, k + 2).getIsFilled() && slot(j, k + 2).getIsRed() != iAmRed) 
				 {
					 if(slot(j, k + 3).getIsFilled() && slot(j, k + 3).getIsRed() != iAmRed)
					 	{
						 	if(printMoves)
						 	{System.out.println("Vertical Block");}
						 	return j;
					 	}
				 }
			 }
		 }
	  return -1;
  }
  
  private int horzBlockR(int j, int k) 
  {
	//Check Restrictions first
	  if(j <= myGame.getColumnCount() -4)
	  {
		  //(Found one, hole, found two)
		  if(!slot(j + 1, k).getIsFilled()) 
		  {
			  if(slot(j + 2, k).getIsFilled() && slot(j + 2, k).getIsRed() != iAmRed) 
			  {
				  if(slot(j + 3, k).getIsFilled() && slot(j + 3, k).getIsRed() != iAmRed) 
				  { 
					  //check if slot is on bottom to prevent array.outOfBounds
					  if(k == myGame.getRowCount() -1)
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Ground (one-hole-two) --->");}
						  return (j + 1);
					  }
		  			  //if it's mid-air, make sure there is a block below where to place
					  else if(slot(j + 1, k + 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Air (one-hole-two) ---->");}
						  return (j + 1); 
					  }
				  }
			  }
		  }
		  //(Found two, hole, found one)
		  if(slot(j + 1, k).getIsFilled() && slot(j + 1, k).getIsRed() != iAmRed) 
		  {
			  if(!slot(j + 2, k).getIsFilled()) 
			  {
				  if(slot(j + 3, k).getIsFilled() && slot(j + 3, k).getIsRed() != iAmRed) 
				  {
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
				  }
			  }
		  }
		  //(Found three, hole)
		  if(slot(j + 1, k).getIsFilled() && slot(j + 1, k).getIsRed() != iAmRed) 
		  {
			  if(slot(j + 2, k).getIsFilled() && slot(j + 2, k).getIsRed() != iAmRed)
			  {
				  if(!slot(j + 3, k).getIsFilled()) 
				  {
					  //If it's on ground, don't check below to avoid array outOfBounds
					  if(k == myGame.getRowCount() -1)
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Ground (three-hole) ---->");}
						  return (j + 3);
					  }
		  			  //if it's mid-air, make sure there is a block below where to place
					  else if(slot(j + 3, k + 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Horz Block Air (three-hole) ---->");}
						  return (j + 3); 
					  } 
				  }
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
	  	if(j >= 3)
		{
	  		//(Found three, hole)
			  if(slot(j - 1, k).getIsFilled() && slot(j - 1, k).getIsRed() != iAmRed) 
			  {
				  if(slot(j - 2, k).getIsFilled() && slot(j - 2, k).getIsRed() != iAmRed) 
				  {
					  if(!slot(j - 3, k).getIsFilled()) 
					  {
						  //If it's on ground, don't check below to avoid array outOfBounds
						  if(k == myGame.getRowCount() -1)
						  {
							  if(printMoves)
							  {System.out.println("Horz Block Ground (three-hole) <----");}
							  return (j - 3);
						  }
			  			  //if it's mid-air, make sure there is a block below where to place
						  else if(slot(j - 3, k + 1).getIsFilled()) 
						  {
							  if(printMoves)
							  {System.out.println("Horz Block Air (three-hole) <----");}
							  return (j - 3); 
						  } 
					  }
				  }
			  }
		}  
		  return -1;
  }
  
  private int diagBlockR(int j, int k) 
  {
	  //Check restrictions first
	  if(j <= myGame.getColumnCount() - 4 && k >= 3)
	  {
		  //(hole, found three) --- this is really like found three, and there is an empty one behind the 3-pattern
		  //Note: there is also a slightly tighter restriction for this one, and it won't work from the ground currently
		  if(j > 1 && k < myGame.getRowCount() - 2) 
		  {
			  if(slot(j + 1, k - 1).getIsFilled() && slot(j + 1, k - 1).getIsRed() != iAmRed) 
			  {
				  if(slot(j + 2, k - 2).getIsFilled() && slot(j + 2, k - 2).getIsRed() != iAmRed)
				  {
					  //check for an empty hole before the trigger tile was found && if there's a tile below it. If both true, place on the empty column
					  if(!slot(j - 1, k + 1).getIsFilled()) 
					  {
						  if(slot(j - 1, k + 2).getIsFilled()) 
						  {
							  if(printMoves)
							  {System.out.println("Diag Block (hole-three) ---->^");}
							  return (j - 1);
						  }
					  }
				  }
			  }
		  }
		  //(Found one, hole, found two)
		  if(!slot(j + 1, k - 1).getIsFilled()) 
		  {
			  if(slot(j + 2, k - 2).getIsFilled() && slot(j + 2, k - 2).getIsRed() != iAmRed) 
			  {
				  if(slot(j + 3, k - 3).getIsFilled() && slot(j + 3, k - 3).getIsRed() != iAmRed) 
				  {
					  	//If there is a slot below the empty slot, go there
					  	if(slot(j + 1, k).getIsFilled()) 
					  	{
					  		if(printMoves)
							{System.out.println("Diag Block (one-hole-two) ---->^");}
					  		return (j + 1);
					  	}
		  				//TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
				  }
			  }
		  }
		  
		  //(Found two, hole, found one)
		  if(slot(j + 1, k - 1).getIsFilled() && slot(j + 1, k - 1).getIsRed() != iAmRed) 
		  {
			  if(!slot(j + 2, k - 2).getIsFilled()) 
			  {
				  if(slot(j + 3, k - 3).getIsFilled() && slot(j + 3, k - 3).getIsRed() != iAmRed) 
				  {
					  if(slot(j + 2, k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (two-hole-one) ---->^");}
						  return (j + 2);
					  }
		  			  //TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
				  }
			  }
		  }
		//(Found three, hole)
		  if(slot(j + 1, k - 1).getIsFilled() && slot(j + 1, k - 1).getIsRed() != iAmRed) 
		  {
			  if(slot(j + 2, k - 2).getIsFilled() && slot(j + 2, k - 2).getIsRed() != iAmRed) 
			  {
				  if(!slot(j + 3, k - 3).getIsFilled()) 
				  {
					  if(slot(j + 3, k - 2).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (three-hole) ---->^");}
						  return (j + 3);
					  }
				  }
			  }
		  }
	  }
	  return -1;
  }
  
  private int diagBlockL(int j, int k) 
  {
	  //Check restrictions first
	  if(j >= 3 && k >= 3)
	  {
		  //(hole, found three) --- this is really like found three, and there is an empty one behind the 3-pattern
		  //Note: there is also a slightly tighter restriction for this one, and it won't work from the ground currently
		  if(j < myGame.getColumnCount() - 2 && k < myGame.getRowCount() - 2) 
		  {
			  if(slot(j - 1, k - 1).getIsFilled() && slot(j - 1, k - 1).getIsRed() != iAmRed) 
			  {
				  if(slot(j - 2, k - 2).getIsFilled() && slot(j - 2, k - 2).getIsRed() != iAmRed) 
				  {
					  //check for an empty hole before the trigger tile was found and if there's a tile below it to place on-top of
					  if(!slot(j + 1, k + 1).getIsFilled()) 
					  {
						  if(slot(j + 1, k + 1).getIsFilled()) 
						  {
							  if(printMoves)
							  {System.out.println("Diag Block (hole-three) ^<----");}
							  return (j + 1);
						  }
					  }
				  }
			  }
		  }
		  //(Found one, hole, found two)
		  if(!slot(j - 1, k - 1).getIsFilled()) 
		  {
			  if(slot(j - 2, k - 2).getIsFilled() && slot(j - 2, k - 2).getIsRed() != iAmRed) 
			  {
				  if(slot(j - 3, k - 3).getIsFilled() && slot(j - 3, k - 3).getIsRed() != iAmRed) 
				  {
					  //If there is a slot below the empty slot, go there
					  	if(slot(j - 1, k).getIsFilled()) 
					  	{
					  		if(printMoves)
							{System.out.println("Diag Block (one-hole-two) ^<----");}
					  		return (j - 1);
					  	}
		  				//TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
				  }
			  }
		  }
		  
		  //(Found two, hole, found one)
		  if(slot(j - 1, k - 1).getIsFilled() && slot(j - 1, k - 1).getIsRed() != iAmRed) 
		  {
			  if(!slot(j - 2, k - 2).getIsFilled()) 
			  {
				  if(slot(j - 3, k - 3).getIsFilled() && slot(j - 3, k - 3).getIsRed() != iAmRed) 
				  {
					  if(slot(j - 2, k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (two-hole-one) ^<----");}
						  return (j - 2);
					  }
		  			//TODO If there was not a slot filled below the previous, but there was one filled below THAT one, DO NOT go there if possible.
				  }
			  }
		  }
		//(Found three, hole)
		  if(slot(j - 1, k - 1).getIsFilled() && slot(j - 1, k - 1).getIsRed() != iAmRed) 
		  {
			  if(slot(j - 2, k - 2).getIsFilled() && slot(j - 2, k - 2).getIsRed() != iAmRed) 
			  {
				  if(!slot(j - 3, k - 3).getIsFilled()) 
				  {
					  if(slot(j - 3, k - 2).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diag Block (three-one) ^<----");}
						  return (j - 3);
					  }
				  }
			  }
		  }
	  }
	  return -1;
  }
  
  
  
  
  //Prediction Block methods
  private int vertPredict(int j) 
  {
	//Get the lowest empty index and search below it as the first check
	  int k = getLowestEmptyIndex(myGame.getColumn(j));
	//Check if curSlot is within game boundaries and is at least 2 above bottom
		if(k <= myGame.getRowCount()- 3 && k > 0) 
		{
			//If 2 vertical slots are filled of opposite MyAgent color, place a token on top 
			 if(slot(j, k + 1).getIsFilled() && slot(j, k + 1).getIsRed() != iAmRed) 
			 { 
				 if(slot(j, k + 2).getIsFilled() && slot(j, k + 2).getIsRed() != iAmRed)
				 {
					if(printMoves)
					{System.out.println("Vertical *Prediction* Block");}
					return j;
				 }
			 }
		}
	  return -1;
  }
  
  private int horzPredictR(int j, int k)
  {
	  //Check Restrictions first
	  //this won't work on the ground level
	  if(j <= myGame.getColumnCount() -4 && j >= 1 && k <= myGame.getRowCount() - 2) 
	  {
		  //(found two, hole*2)
		  if(slot(j + 1, k).getIsFilled() && slot(j + 1, k).getIsRed() != iAmRed) 
		  {
			  if(!slot(j + 2, k).getIsFilled()) 
			  {
				  if(!slot(j + 3, k).getIsFilled()) 
				  {
					//Check if any of the empties have tiles under them to place
					if(slot(j + 2, k + 1).getIsFilled())
				  	{
					  	if(printMoves)
					  	{System.out.println("horizontal *Prediction* Block (two-hole[here]-hole)---->");}
					  	return (j + 2);
				  	}
				  	else if(slot(j + 3, k + 1).getIsFilled()) 
				  	{
					  	if(printMoves)
					  	{System.out.println("horizontal *Prediction* Block (two-hole-hole[here])---->");}
					  	return (j + 3);
				  	}
				  }
			  }
		  }
	  
		  //(hole, found one, hole[here], found one) **Try for[here] first, otherwise try to place in other hole**
		  if(slot(j + 2, k).getIsFilled() && slot(j + 2, k).getIsRed() != iAmRed) 
		  {
			  if(!slot(j - 1, k).getIsFilled())
			  {
				  if(!slot(j + 1, k).getIsFilled()) 
				  {
					  if(slot(j + 1, k + 1).getIsFilled())
					  {
						  if(printMoves)
						  {System.out.println("horizontal *Prediction* Block (hole-one-hole[here]-one)---->");}
						  return (j + 1);  
					  }
					  else if(slot(j - 1, k + 1).getIsFilled())
					  {
						  if(printMoves)
						  {System.out.println("horizontal *Prediction* Block (hole[here]-one-hole-one)---->");}
						  return (j - 1);  
					  }		
				  }
			  }
		  }
		  //(found one, hole[here], found one, hole) **Try for[here] first, otherwise try to place in other hole**
		  if(slot(j + 2, k).getIsFilled() && slot(j + 2, k).getIsRed() != iAmRed) 
		  {
			  if(!slot(j + 1, k).getIsFilled()) 
			  {
				  if(!slot(j + 3, k).getIsFilled()) 
				  {
					  //Check if any of the empties have tiles under them to place
					  if(slot(j + 1, k + 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("horizontal *Prediction* Block (one-hole[here]-one-hole)---->");}
						  return (j + 1); 
					  }
					  else if(slot(j + 3, k + 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("horizontal *Prediction* Block (one-hole-one-hole[here])---->");}
						  return (j + 3);   
					  }	   
				  }
			  }
		  }
	  }
	  return -1;
  }
  
  private int horzPredictL(int j, int k)
  {
	 //Check Restrictions First
	  if(j >= 3 && k <= myGame.getRowCount() - 2)
		//(found two, hole*2)
		  if(slot(j - 1, k).getIsFilled() && slot(j - 1, k).getIsRed() != iAmRed) 
		  {
			  if(!slot(j - 2, k).getIsFilled()) 
			  {
				  	if(!slot(j - 3, k).getIsFilled()) 
				  	{
				  		//Check if any of the empties have tiles under them to place
				  		if(slot(j - 2, k + 1).getIsFilled())
				  		{
				  			if(printMoves)
				  			{System.out.println("horizontal *Prediction* Block (two-hole[here]-hole)<----");}
				  			return (j - 2);
				  		}
				  		else if(slot(j - 3, k + 1).getIsFilled()) 
				  		{
				  			if(printMoves)
				  			{System.out.println("horizontal *Prediction* Block (two-hole-hole[here])<----");}
				  			return (j - 3);
				  		}
				  	}
			  }
		  }
	  //Note that since (one-hole-one-hole) reversed is (hole-one-hole-one), there is no need for either method to be checked
	  //in this left sweep, as it will be caught in horzPredictR before this method is called

	  return -1;
  }
  
  private int diagPredictR(int j, int k) 
  {  
	  //Check Restrictions first
	  if(j <= myGame.getColumnCount() -4 && k >= 3) 
	  {
		//(found one, hole, found one, hole) [place on column that has a tile filled below the empty slot]
		  if(!slot(j + 1, k - 1).getIsFilled()) 
		  {
			  if(slot(j + 2, k - 2).getIsFilled() && slot(j + 2, k - 2).getIsRed() != iAmRed) 
			  {
				  if(!slot(j + 3, k - 3).getIsFilled()) 
				  {
				  //Check which (if any) empty column has a tile below that can be placed on-top of
					  if(slot(j + 1, k).getIsFilled())
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole[here]-one-hole) ---->^");}
						  return (j + 1);
					  }
					  else if(slot(j + 3, k - 2).getIsFilled())
		  			  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole-one-hole[here]) ---->^");}
						  return (j + 3);
		  			  }
				  }
			  }
		  }
		//(found two, two holes) [place on column that has a tile filled below the potential pattern]
		  if(slot(j + 1, k - 1).getIsFilled() && slot(j + 1, k - 1).getIsRed() != iAmRed) 
		  {
			  if(!slot(j + 2, k - 2).getIsFilled())
			  {
				  if(!slot(j + 3, k - 3).getIsFilled())
				  {
					  //Check which (if any) empty column has a tile below that can be placed on-top of
					  if(slot(j + 2, k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (two-hole[here]-hole) ---->^");}
						  return (j + 2);
					  }
					  else if(slot(j + 3, k - 2).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (two-hole-hole[here]) ---->^");}
						  return (j + 3);
					  }
				  }
			  }
		  }
		  //(found one, hole*2, found one)	  
		  if(!slot(j + 1, k - 1).getIsFilled()) 
		  {
			  if(!slot(j + 2, k - 2).getIsFilled()) 
			  {
				  if(slot(j + 3, k - 3).getIsFilled() && slot(j + 3, k - 3).getIsRed() != iAmRed) 
				  {
					//Check if any of the empties have tiles under them to place
					  if(slot(j + 1, k).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole[here]-hole-one)---->");}
						  return (j + 1);
					  }
					  else if(slot(j + 2, k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole-hole[here]-one)---->");}
						  return (j + 2);
					  }
				  }
			  }
		  }
	  }
	  
	//(hole, found two, hole) [place on column that has a tile filled below the empty slot]
	//Note: there is also a slightly more complicated restriction for this one, and it won't work from the ground currently
	 if(j >= 1 && j <= myGame.getColumnCount() -3 && k >= 2 && k <= myGame.getRowCount() - 3) 
	 {
		 if(slot(j + 1, k - 1).getIsFilled() && slot(j + 1, k - 1).getIsRed() != iAmRed) 
		 {
			if(!slot(j + 2, k - 2).getIsFilled()) 
			{
				if(!slot(j - 1, k + 1).getIsFilled()) 
				{
					//Check which (if any) empty column has a tile below that can be placed on-top of
					 if(slot(j - 1, k + 2).getIsFilled())
					 {
						if(printMoves)
						{System.out.println("Diagonal *Prediction* Block (hole[here]-two-hole) ---->^");}
						return (j - 1);
					}
					else if (slot(j + 2, k - 1).getIsFilled()) 
					{
						if(printMoves)
						{System.out.println("Diagonal *Prediction* Block (hole-two-hole[here]) ---->^");}
						return (j + 2);	
					}		
				}
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
		  if(!slot(j - 1, k - 1).getIsFilled()) 
		  {
			  if(slot(j - 2, k - 2).getIsFilled() && slot(j - 2, k - 2).getIsRed() != iAmRed) 
			  {
				  if(!slot(j - 3, k - 3).getIsFilled()) 
				  {
					  //Check which (if any) empty column has a tile below that can be placed on-top of
					  if(slot(j - 1, k).getIsFilled())
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole[here]-one-hole) ^<----");}
						  return (j - 1);
					  }
					  else if(slot(j - 3, k - 2).getIsFilled())
		  			  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole-one-hole[here]) ^<----");}
						  return (j - 3);
		  			  }
				  }
			  }
		  }
		//(found two, two holes) [place on column that has a tile filled below the potential pattern]
		  if(slot(j - 1, k - 1).getIsFilled() && slot(j - 1, k - 1).getIsRed() != iAmRed) 
		  {
			  if(!slot(j - 2, k - 2).getIsFilled()) 
			  {
				  if(!slot(j - 3, k - 3).getIsFilled()) 
				  {
					  //Check which (if any) empty column has a tile below that can be placed on-top of
					  if(slot(j - 2, k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (two-hole[here]-hole) ^<----");}
						  return (j - 2);
					  }
					  else if(slot(j - 3, k - 2).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (two-hole-hole[here]) ^<----");}
						  return (j - 3);
					  }
				  }
			  }
		  }
		  //(found one, hole*2, found one)	  
		  if(!slot(j - 1, k - 1).getIsFilled()) 
		  {
			  if(!slot(j - 2, k - 2).getIsFilled()) 
			  {
				  if(slot(j - 3, k - 3).getIsFilled() && slot(j - 3, k - 3).getIsRed() != iAmRed) 
				  {
					//Check if any of the empties have tiles under them to place
					  if(slot(j - 1, k).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole[here]-hole-one)<----");}
						  return (j - 1);
					  }
					  else if(slot(j - 2, k - 1).getIsFilled()) 
					  {
						  if(printMoves)
						  {System.out.println("Diagonal *Prediction* Block (one-hole-hole[here]-one)<----");}
						  return (j - 2);
					  }
				  }
			  }
		  }
	  }
	  
	//(hole, found two, hole) [place on column that has a tile filled below the empty slot]
	//Note: there is also a slightly more complicated for this one, and it won't work from the ground currently
	 if(j <= myGame.getColumnCount() -2 && j >= 2 && k >= 2 && k <= myGame.getRowCount() - 3) 
	 {
		 if(slot(j - 1, k - 1).getIsFilled() && slot(j - 1, k - 1).getIsRed() != iAmRed) 
		 {
			if(!slot(j - 2, k - 2).getIsFilled()) 
			{
				if(!slot(j + 1, k + 1).getIsFilled()) 
				{
					//Check which (if any) empty column has a tile below that can be placed on-top of
					 if(slot(j + 1, k + 2).getIsFilled())
					 {
						if(printMoves)
						{System.out.println("Diagonal *Prediction* Block (hole[here]-two-hole) ^<----");}
						return (j + 1);
					}
					else if (slot(j - 2, k - 1).getIsFilled()) 
					{
						if(printMoves)
						{System.out.println("Diagonal *Prediction* Block (hole-two-hole[here]) ^<----");}
						return (j - 2);	
					}		
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
