import java.awt.Color
import hevs.graphics.FunGraphics
import javax.swing.{JFrame, JOptionPane, JPasswordField}


class Reversi {

  // better than Color.green
  var darkGreen: Color = new Color(50,100,70)
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)
  grid = fillGrid(grid)
  var gm : GameManager = new GameManager(false)
  var display: FunGraphics = new FunGraphics(650,800,"Reversi")


  def play(): Unit ={


    // TODO: rajouter bothCheckLegal dans la condition - ajouter si les joueurs veulent rejouer

    while((!checkFillGrid()) || (!(bothCheckLegalMoves()))){
      gridGraphics()
      print(toString)
      updateGraphics()
      println(gm.turn)
      // si turn = true ET checklegal pour blanc = vrai => CHANGETURN (vice versa)
      if (!gm.turn){
        if(checkLegalMoves()){
          println("changed turn")
          gm.changeTurn()
          println(s"${gm.displayScore(countWhite(), countBlack())} ")
        }
        legalMovesGraphics(Color.black)
        var step1: String = gm.askPlacement()
        placeCoin(step1(0).toInt - 48 ,step1(1).toInt - 48,Color.BLACK)
        println(s"${gm.displayScore(countWhite(), countBlack())} ")
      } else {
        if(checkLegalMoves()){
          println("changed turn")
          gm.changeTurn()
          println(s"${gm.displayScore(countWhite(), countBlack())} ")
        }
        legalMovesGraphics(Color.white)
        var step1: String = gm.askPlacement()
        placeCoin(step1(0).toInt - 48 ,step1(1).toInt - 48,Color.WHITE)
        println(s"${gm.displayScore(countWhite(), countBlack())} ")
      }

    }

    //TODO: Implémentez ce qui se passe quand méthode checkLegalMoves est vrai


    //TODO: Dans le display, trouver la position pour afficher le score et l'actualiser en fonction
    if (countBlack()>countWhite()){
      print(s"Black win : $countBlack")
    }
    else print(s"White win : $countWhite()")
  }

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

  def coinGraphics(x: Int, y: Int, couleur: Color): Unit = {
    display.setColor(couleur)
    display.drawFilledCircle(x*70 + 48, y*70 + 48, 65)
  }

  def updateGraphics(): Unit ={
    for(i <- grid.indices){
      for(j <- grid(i).indices){
        coinGraphics(j,i, grid(i)(j).c)
      }
    }

    display.setColor(Color.white)
    display.drawFilledCircle(140, 650, 65)
    display.drawString(165,750,s"$countWhite",Color.WHITE, 30)

    display.setColor(Color.black)
    display.drawFilledCircle(450, 650, 65)
    display.drawString(475,750,s"$countBlack",Color.black, 30)


  }

  def legalMovesGraphics(color: Color): Unit = {
    var tab: Array[Coin] = allLegalMoves(color)
    display.setColor(Color.yellow)

    for(ind <- tab.indices){
      display.drawFilledCircle((tab(ind).col+1)*70, (tab(ind).row+1)*70, 20)
    }

  }

  def isOccupied(row: Int, col: Int): Boolean = {
    if (!grid(row)(col).busy){
      return false
    }
    return true
  }


  /**
    @param x: coordinate of chosen placement
    @param y: coordinate of chosen placement

   */

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
          //println("Position is already occupied")
          //gm.askPlacement()
          return false
        }

    // on regarde toutes les directions possibles
    for (i <- -1 to 1) {
      for (j <- -1 to 1) {
        if (i != 0 || j != 0) { // ignore position of chosen coin
          var nx: Int = x + i
          var ny: Int = y + j
          var continueLoop: Boolean = true //control the loop

          // Check if the next position is inside the grid and contains a coin of the opposite color
          if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy && grid(nx)(ny).c != color) {
            nx += i
            ny += j

            // continue in the same direction
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && continueLoop) {
              // If the next position is empty, set continueLoop to false to stop the loop
              if (!grid(nx)(ny).busy) {
                continueLoop = false
              } else if (grid(nx)(ny).c == color) {
                // If the next position contains a coin of the same color -> the move is legal
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


  // array of all possible legal placements
  // TODO: where to call it ?
  def allLegalMoves(color: Color): Array[Coin] = {
    // var tab: Array[Array[Coin]] =
    var count: Int = 0

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
      println("Place occupied")
      //TODO: display
    }
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

          // Check initial adjacent coin
          if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy && grid(nx)(ny).c != c) {
            // Move in the direction to find a coin of the same color
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy && continueFlipping) {
              if (grid(nx)(ny).c == c) {
                // Flip coins if the same color coin is found
                var flipX: Int = x + i
                var flipY: Int = y + j
                while ((flipX != nx || flipY != ny) && grid(flipX)(flipY).busy) {
                  grid(flipX)(flipY).c = c
                  flipX += i
                  flipY += j
                }
                // Stop flipping
                continueFlipping = false
              }
              // Proceed to next coin in the line
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
        if (!(grid(i)(j).busy)) {
          return false
        }
      }
    }
    return true
  }

  /**
   *Method that checks
   *
   * @param couleur: Color that we want to check
   * @return true if grid is filled with only one color
   */
  def checkOneColorOnly(couleur: Color) : Boolean = {
    for (i <- grid.indices){
      for (j <- grid(i).indices){
        if (grid(i)(j).busy){
          if(couleur != grid(i)(j).c){
            return false
          }
        }
      }
    }
    return true
  }

  // return true quand aucun move est Legal -> passe son tour
  // TODO: Modifier pour que la fonction de print rien ni change rien
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

  //returns true if no more legal moves for any color
  def bothCheckLegalMoves(): Boolean = {

    var white: Boolean = false
    var black: Boolean = false

    // check white
    for (i <- grid.indices) {
      for (j <- grid(i).indices) {
        if (!(isLegal(i, j, Color.WHITE))) {
          white = true
        }
      }
    }

    //check black
    for (i <- grid.indices) {
      for (j <- grid(i).indices) {
        if (!(isLegal(i, j, Color.BLACK))) {
          black = true
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

  // test game in the console -- O = white coins -- X = black coins
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


class Coin(var row: Int, var col: Int, var c: Color, var busy: Boolean){

}

class GameManager(var turn : Boolean) {
  def askGameMode(): Unit = {
    println("Game mode: 1 player (1) - 2 players (2)")
    var gameMode = Input.readInt()
  }

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
   * @return
   */
  def askPlacement(): String = {
    var choice: String = ""
    var caseSelected : String = ""

    if (!turn){
      println("Player 1 place your next coin (ex: A1)")
      choice = Input.readString().toUpperCase
      caseSelected = ((choice(0)-65).toString + choice(1).toString)
      while(!((choice(0) < 'A' || choice(0) > 'H') || (choice(1).toInt < 0 || choice(1).toInt > 7))){ // Check if the user's input is within the expected range
        println("Use the format: A0")
        choice = Input.readString().toUpperCase
        caseSelected = ((choice(0)-65).toString + choice(1).toString)
      }
    } else if (turn){
      println("Player 2 place your next coin (ex: A0)")
      choice = Input.readString().toUpperCase
      caseSelected = ((choice(0)-65).toString + choice(1).toString)
      while(!((choice(0) < 'A' || choice(0) > 'H') || (choice(1).toInt < 0 || choice(1).toInt > 7))) { // Check if the user's input is within the expected range
        println("Use the format: A0")
        choice = Input.readString().toUpperCase
        caseSelected = ((choice(0)-65).toString + choice(1).toString)
      }
    }
    return caseSelected
  }



}



object Reversi extends App{

  var g: Reversi = new Reversi
  g.play()
  //g.fillGrid(g.grid)
  //println(g.toString)
}
