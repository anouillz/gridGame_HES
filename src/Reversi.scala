import java.awt.Color
import hevs.graphics.FunGraphics
import javax.sound.sampled.AudioSystem
import java.io.File


/**
 * The game is played on a 8x8 grid (Default)
 * The game is played by two players (Default)
 * The players take turns to place their coins on the grid
 * The player's coin's color is either black or white
 * The game ends when the grid is completely filled or when no more legal moves for both players are possible
 * The player with the most coins of his color wins
 *
 * @version 1.0
 *
 */
class Reversi {

  // better than Color.green
  var darkGreen: Color = new Color(50,100,70)

  //creates grid
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)
  grid = fillGrid(grid) // necessary outside of play because of other functions

  var answer: Char = ' '
  var gm : GameManager = new GameManager(false)
  var display: FunGraphics = new FunGraphics(650,800,"Reversi | Othello")


  /**
   * Method that starts the game
   *  - Fills the grid
   *  - Displays the grid
   *  - Asks for the first player's move
   *  - Checks if the move is legal
   *  - Places the coin if the move is legal
   *  - Updates the grid's visuals
   *  - Changes the turn
   *  - Repeats until the grid is filled or no more legal moves for both players are possible
   *  - Displays the winner
   *  - Asks if the players want to play again
   *
   * @return true if the players want to play again
   */
  def play(): Boolean ={

    //in case of replay
    grid = fillGrid(grid)

    // game loop -> while grid not filled and no more legal moves for both players
    while((!checkFillGrid()) && (!(bothCheckLegalMoves()))){
      gridGraphics()
      updateGraphics()

      if (!gm.turn){
        // if no more legal moves for BLACK -> WHITE plays
        if(checkLegalMoves()){
          println("changed turn")
          gm.changeTurn()
          legalMovesGraphics(Color.white)
        } else {
          legalMovesGraphics(Color.black)
          var step1: String = gm.askPlacement()
          placeCoin(step1(0).toInt - 48 ,step1(1).toInt - 48,Color.BLACK)
        }

      } else {
        if(checkLegalMoves()){
          println("changed turn")
          gm.changeTurn()
          legalMovesGraphics(Color.black)
        } else {
          legalMovesGraphics(Color.white)
          var step1: String = gm.askPlacement()
          placeCoin(step1(0).toInt - 48 ,step1(1).toInt - 48,Color.WHITE)
        }
      }
    }


    if (countBlack()>countWhite()){
      updateGraphics()
      scoreGraphics(s"Black win: $countBlack")
      answer = Dialogs.getChar("Do you wanna play again ? (y)")
      return true
    }
    else {
      updateGraphics()
      scoreGraphics(s"White win: $countWhite")
      answer = Dialogs.getChar("Do you wanna play again ? (y)")
      return true
    }
    return false
  }

  /**
   * Method that draws the grid, rows, columns and the coins
   */
  def gridGraphics(): Unit = {
    var i: Int = 115
    var j: Int = 115
    var nb: Int = 75
    var count1: Int = 0
    var lett: Int = 85
    var count2: Int = 'A'
    display.setPenWidth(4f)

    // Background
    display.setColor(darkGreen)
    display.drawFillRect(0,0,650,800)

    //draws main board
    display.setColor(Color.BLACK)
    display.drawRect(45,45,560,560)

    display.setPenWidth(2f)
    //draws grid
    while(i < 605){
      display.drawLine(i, 45, i, 605)
      i += 70
    }
    while(j < 605){
      display.drawLine(45, j, 605, j)
      j += 70
    }

    //draws row and col
    while(nb<605){
      display.drawString(nb, 29, s"$count1", Color.BLACK, 20)
      nb += 70
      count1 += 1
    }
    while(lett < 605){
      display.drawString(15, lett, s"${count2.toChar}", Color.BLACK, 20)
      lett += 70
      count2 += 1
    }

  }

  /**
   * Method that draws the coins
   *
   * @param x: coordinate of chosen placement
   * @param y: coordinate of chosen placement
   * @param couleur: Color of selected coin
   */
  def coinGraphics(x: Int, y: Int, couleur: Color): Unit = {
    display.setColor(couleur)
    display.drawFilledCircle(x*70 + 48, y*70 + 48, 65)
  }

  /**
   * updates the grid's visuals
   */
  def updateGraphics(): Unit ={
    for(i <- grid.indices){
      for(j <- grid(i).indices){
        coinGraphics(j,i, grid(i)(j).c)
      }
    }

    // draws current score
    display.setColor(Color.white)
    display.drawFilledCircle(140, 650, 65)
    display.drawString(165,750,s"$countWhite",Color.WHITE, 30)

    display.setColor(Color.black)
    display.drawFilledCircle(450, 650, 65)
    display.drawString(475,750,s"$countBlack",Color.black, 30)
  }

  /**
   * draws the final score
   *
   * @param s: String that contains the score, will depend on the winner
   */
  def scoreGraphics(s: String): Unit = {
    display.setColor(darkGreen)
    display.drawFillRect(0, 650, 650, 150)
    display.drawString(225, 700, s, Color.BLACK, 40)
  }

  /**
   * draws the legal moves for the current player with a small yellow circle
   *
   * @param color: Color of current player's coin
   */
  def legalMovesGraphics(color: Color): Unit = {
    var tab: Array[Coin] = allLegalMoves(color)
    display.setColor(Color.yellow)

    for(ind <- tab.indices){
      display.drawFilledCircle((tab(ind).col+1)*70, (tab(ind).row+1)*70, 20)
    }
  }

  /**
   * Method that checks if the selected placement by the player is occupied or not
   *
   * @param row: coordinate of chosen placement
   * @param col: coordinate of chosen placement
   * @return true if the placement is occupied
   */
  def isOccupied(row: Int, col: Int): Boolean = {
    if (!grid(row)(col).busy){
      return false
    }
    return true
  }


  /**
   * Method that check if the selected placement by the player is possible or not
   *  - Has to be next to the opposite color
   *  - Has to be within the grid's borders
   *  - The opposite coin's color has to be trapped between the player's coin's color
   *
   * @param x: coordinate of chosen placement
   * @param y: coordinate of chosen placement
   * @param color: Color of current player's coin
   * @return true if there is a legal move
   */
  def isLegal(x: Int, y: Int, color: Color): Boolean = {
        if (isOccupied(x, y)){
          return false
        }

    // we check all possible directions, up, down and diagonals
    for (i <- -1 to 1) {
      for (j <- -1 to 1) {
        if (i != 0 || j != 0) { // ignore position of chosen coin
          var nx: Int = x + i
          var ny: Int = y + j
          var continueLoop: Boolean = true //control the loop

          // check if the next position is inside the grid AND contains a coin of the opposite color
          if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy && grid(nx)(ny).c != color) {
            nx += i
            ny += j

            // continue in the same direction
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && continueLoop) {
              // if the next position is empty, set continueLoop to false to stop the loop
              // it means that the move is not legal
              if (!grid(nx)(ny).busy) {
                continueLoop = false
              } else if (grid(nx)(ny).c == color) {
                // if the next position contains a coin of the player's color -> the move is legal
                return true
              }

              nx += i
              ny += j
            }
          }
        }
      }
    }

    // if no valid line of coins was found in any direction, the move is not legal
    return false
  }


  /**
   * Method that returns an array of all the legal moves for the current player
   *
   * @param color: Color of current player's coin
   * @return array of all legal moves
   */
  def allLegalMoves(color: Color): Array[Coin] = {
    var count: Int = 0

    // used to know the size of the array
    for (i <- grid.indices){
      for(j <- grid(i).indices){
        if(isLegal(i,j,color)) count += 1
      }
    }

    var tab: Array[Coin] = new Array(count)
    var k = 0
    for (i <- grid.indices){
      for(j <- grid(i).indices){
        if(isLegal(i,j,color)) {
          tab(k) = new Coin(i, j, Color.yellow, false)
          k += 1
        }
      }
    }
    return tab

  }

  /**
   * Method that places the coin if the chosen placement is not occupied and legal
   * Let's the user know if the chosen placement is already occupied
   *
   * @param x: coordinate of chosen placement
   * @param y: coordinate of chosen placement
   * @param color: Color of current player's coin
   * @return
   */
  def placeCoin(x:Int, y: Int, color: Color): Unit = {
    if(isOccupied(x,y)) {
      Dialogs.displayMessage("Place occupied, choose a valid placement")
    }
    // if the placement is not occupied and legal -> place the coin
    if(!(isOccupied(x,y)) && isLegal(x,y,color)){
      grid(x)(y).busy = true
      grid(x)(y).c = color

      updateCoins(x,y,color)
      gm.changeTurn()
    }
}


  /**
   * This function updates the coins that might have been changed by a player's turn
   * Is called in placeCoin
   *
   * @param x: coordinate of chosen placement
   * @param y: coordinate of chosen placement
   * @param color: Color of selected coin
   */
  def updateCoins(x: Int, y: Int, c: Color): Unit = {
    // all possible directions
    for (i <- -1 to 1) {
      for (j <- -1 to 1) {
        if (i != 0 || j != 0) { // ignore current coin
          var nx: Int = x + i
          var ny: Int = y + j
          var continueFlipping: Boolean = true

          // we check all directions
          if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy && grid(nx)(ny).c != c) {
            // continue in the same direction
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy && continueFlipping) {
              if (grid(nx)(ny).c == c) {
                // flip coins if the same color coin is found
                var flipX: Int = x + i
                var flipY: Int = y + j
                while ((flipX != nx || flipY != ny) && grid(flipX)(flipY).busy) {
                  // plays the sound of a flipped coin
                  try {
                    val audioInputStream = AudioSystem.getAudioInputStream(new File("res/coin-flip-shimmer-85750.wav"))
                    val clip = AudioSystem.getClip
                    clip.open(audioInputStream)
                    clip.start()
                  } catch {
                    case e: Exception => e.printStackTrace()
                  }

                  grid(flipX)(flipY).c = c
                  flipX += i
                  flipY += j
                }
                // stop flipping coins if the same color coin is found
                continueFlipping = false
              }
              // proceed to next coin in the line
              if (continueFlipping) {
                nx += i
                ny += j
              }
            }
          }
        }
      }
    }
  }


  /**
   * Method that checks one of possible EndGames end if all the grid is filled which means that the game is over
   *
   * @return True if grid is completely filled
   */
  def checkFillGrid(): Boolean = {
    for (i <- grid.indices) {
      for (j <- grid(i).indices) {
        if (!grid(i)(j).busy) {
          return false
        }
      }
    }
    return true
  }

  /**
   * Method that checks if there is no more legal moves for the current player
   *
   * @return True if no more legal moves for the current player
   */
  def checkLegalMoves(): Boolean = {

    if(gm.turn) {
      for (i <- grid.indices) {
        for (j <- grid(i).indices) {
          if (isLegal(i, j, Color.WHITE)) {
            return false
          }
        }
      }
      return true
    } else {
      for (i <- grid.indices) {
        for (j <- grid(i).indices) {
          if (isLegal(i,j,Color.BLACK)){
            // si au moins un move possible ->
            return false
          }
        }
      }
      return true
    }

  }

  /**
   * Method that checks if there is no more legal moves for both players -> end of game
   *
   * @return True if no more legal moves for both players
   */
  def bothCheckLegalMoves(): Boolean = {

    var white: Boolean = true
    var black: Boolean = true

    // check white
    for (i <- grid.indices) {
      for (j <- grid(i).indices) {
        if (!(isLegal(i, j, Color.WHITE))) {
          white = false
        }
      }
    }

    //check black
    for (i <- grid.indices) {
      for (j <- grid(i).indices) {
        if (!(isLegal(i, j, Color.BLACK))) {
          black = false
        }
      }
    }

    if(white && black){
      return true
    }

    return false
  }


  /**
   * Fills the gird
   * All coins are green by default
   * 4 Coins are placed in the center by default
   *
   * @param tab: array2D of Coins
   * @return
   */
  def fillGrid(tab : Array[Array[Coin]]): Array[Array[Coin]] = {
    for(i <- tab.indices){
      for(j <- tab(i).indices){
        var token: Coin = new Coin(i, j, darkGreen, false)
        tab(i)(j) = token
      }
    }
    for (i : Int <- 3 to 4){
      for(j : Int <- 3 to 4){
        grid(i)(j).busy = true
      }
    }
    grid(3)(3).c = Color.white
    grid(4)(4).c = Color.white
    grid(3)(4).c = Color.black
    grid(4)(3).c = Color.black
    return tab
  }

  /**
   * Counts the number of white coins
   *
   * @return number of white coins
   */
  def countWhite(): Int = {
    var count : Int = 0
    for (i <- grid.indices){
      for (j <- grid(i).indices){
        if (grid(i)(j).c == Color.white){
          count+=1
        }
      }
    }
    return count
  }

  /**
   * Counts the number of black coins
   *
   * @return number of black coins
   */
  def countBlack(): Int = {
    var count: Int = 0
    for (i <- grid.indices) {
      for (j <- grid(i).indices) {
        if (grid(i)(j).c == Color.black) {
          count += 1
        }
      }
    }
    return count
  }

  /**
   * Method that displays the grid in the console
   * Used for tests
   *
   * @return String that contains the grid
   */
  override def toString: String = {
    var s: String = ""
    s += "Game:  \n\t"
    for (i <- grid(0).indices) {
      s += " " + i
    }
    s += "\n"

    for (i <- grid.indices) {
      s += (i +65).toChar + "\t"
      for (j <- grid(i).indices) {
        //s += "\t"
        if (grid(i)(j).col <= 8) {
          if (grid(i)(j).busy) {
            if(grid(i)(j).c == Color.BLACK) {
              s += " X"
            }
            if(grid(i)(j).c == Color.WHITE) {
              s += " O"
            }
            if(grid(i)(j).c == Color.GREEN) {
              s += " "
            }
          } else {
            s += "  "
          }
        } else {
          if (grid(i)(j).busy) {
            s += "  X"
          } else {
            s += "   "
          }
        }
      }
      s += "\n"
    }
    return s
  }

}

/**
 * Class Coin
 *  - Contains the coordinates of the coin
 *  - Contains the color of the coin
 *  - Contains a boolean that indicates if the coin is occupied or not
 *
 * @param row: coordinate of chosen placement
 * @param col: coordinate of chosen placement
 * @param c: Color of selected coin
 * @param busy: boolean that indicates if the coin is occupied or not
 */
class Coin(var row: Int, var col: Int, var c: Color, var busy: Boolean){

}

/**
 * Class GameManager
 *  - Contains the turn of the current player
 *  - Contains the score of both players
 *
 * @param turn: boolean that indicates the turn of the current player, true = white, false = black
 */
class GameManager(var turn : Boolean) {
  def changeTurn(): Unit = {
    // false = noir
    // true = blanc
    if (turn)
      turn = false
    else
      turn = true
  }


  def displayScore(w : Int, b : Int): String = {
    return s"White $w - $b Black"
  }

  /**
   * Asks player's next move with format a1 or A1, ect.., that matches the view on the display
   * Asks until players input valid format
   *
   * @return String that contains the player's next move
   */
  def askPlacement(): String = {
    var choice: String = ""
    var caseSelected : String = ""

    if (!turn){
      choice = Dialogs.getString("Player 1 (Black) : Place your next coin ( ex: \"A0\")").toUpperCase
      while ((choice.length != 2) || (choice.isEmpty)) choice = Dialogs.getString("Player 1 (Black) : Place your next coin ( ex: \"A0\")").toUpperCase
      caseSelected = ((choice(0)-65).toString + choice(1).toString)
      while(!((choice(0) < 'A' || choice(0) > 'H') || (choice(1).toInt < 0 || choice(1).toInt > 7))){ // Check if the user's input is within the expected range
        println("Use the format: A0")
        choice = Dialogs.getString("Player 1 (Black) : Place your next coin ( ex: \"A0\")").toUpperCase
        caseSelected = ((choice(0)-65).toString + choice(1).toString)
      }
    } else if (turn){
      choice = Dialogs.getString("Player 2 (White) : Place your next coin ( ex: \"A0\")").toUpperCase
      while ((choice.length != 2) || (choice.isEmpty)) choice = Dialogs.getString("Player 2 (White) : Place your next coin ( ex: \"A0\")").toUpperCase
      caseSelected = ((choice(0)-65).toString + choice(1).toString)
      while(!((choice(0) < 'A' || choice(0) > 'H') || (choice(1).toInt < 0 || choice(1).toInt > 7))) { // Check if the user's input is within the expected range
        println("Use the format: A0")
        choice = Dialogs.getString("Player 2 (White) : Place your next coin ( ex: \"A0\")").toUpperCase
        caseSelected = ((choice(0)-65).toString + choice(1).toString)
      }
    }
    return caseSelected
  }

}



object Reversi extends App{

  var g: Reversi = new Reversi

  do {
    while (!g.play()) {
      g.play()
    }
  } while (g.answer == 'y')

}
