import java.awt.Color
import hevs.graphics.FunGraphics

import java.awt.event
import java.awt.event.MouseListener

class Reversi {
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)
  grid = fillGrid(grid)
  var gm : GameManager = new GameManager(false)
  //var display: FunGraphics = new FunGraphics(300,300,"Reversi")
  //display.addMouseListener(MouseListener)


  // testtt

  // TODO
  def play(): Unit ={
    //fill grid with the default configuration

    // TODO: rajouter bothCheckLegal dans la condition
    while((!checkFillGrid())){
      print(toString)
      println(gm.turn)
      // si turn = true ET checklegel pour blanc = vrai => CHANGETURN (vice versa)
      if (!gm.turn){
        var step1: String = gm.askPlacement()

        placeCoin(step1(0).toInt - 48 ,step1(1).toInt - 48,Color.BLACK)
      } else {
        var step1: String = gm.askPlacement()

        placeCoin(step1(0).toInt - 48 ,step1(1).toInt - 48,Color.WHITE)
      }

    }

    //TODO: Implémentez ce qui se passe quand méthode checkLegalMoves est vrai


    if (countBlack()>countWhite()){
      print(s"Black win : $countBlack")
    }
    else print(s"White win : $countWhite()")
  }


  def isOccupied(row: Int, col: Int): Boolean = {
    if (!grid(row)(col).busy){
      return false
    }
    return true
  }


  // @param x, y: coordinate of chosen placement
  // @param color: color of players coin (the one who wants to make the move)
  def isLegal(x: Int, y: Int, color: Color): Boolean = {
    if (isOccupied(x, y)){
      //println("Position is already occupied")
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
    //println("No valid line of coins found")
    return false
}

  // TODO

  // @param x, y: coordinate of chosen coin placement
  // @param color: color of players coin (the one who wants to make the move)
  def placeCoin(x: Int, y: Int, c: Color): Unit = {

    if(!(isOccupied(x,y)) && isLegal(x,y,c)){

      grid(x)(y).busy = true
      grid(x)(y).c = c
      updateCoins(x,y,c)
      gm.changeTurn()
    }
}


  def updateCoins(x: Int, y: Int, c: Color): Unit = {
    // all possible directions
    for (i <- -1 to 1) {
      for (j <- -1 to 1) {
        if (i != 0 || j != 0) { // ignore current coin
          var nx: Int = x + i
          var ny: Int = y + j


          if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy && grid(nx)(ny).c != c) {
            nx += i
            ny += j

            // goes in the same direction
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).busy) {
              if (grid(nx)(ny).c == c) {
                // if next position is a coin of the same color, flip all the coins in the line
                var flipX: Int = x + i
                var flipY: Int = y + j
                while ((flipX != nx || flipY != ny) && grid(flipX)(flipY).busy) {
                  grid(flipX)(flipY).c = c
                  flipX += i
                  flipY += j
                }
                // instruction to leave the loop after flipping all the coins
                nx = 8

              }

              nx += i
              ny += j
            }
          }
        }
      }
    }
  }


  // return true quand tout le plateau est occupé -> fin du jeu
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

  // param couleur: Couleur dominante
  // return true quand il y a qu'une couleur -> fin du jeu
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

  // return true quand aucun move est Legal -> fin du jeu
  // TODO: Modifier pour que la fonction de print rien ni change rien
  def checkLegalMoves(): Boolean = {
    for (i <- grid.indices){
      for(j <- grid(i).indices){
        if(gm.turn){
          println("blanc")
          if (!(isLegal(i,j, Color.WHITE))) {
            // si aucun move pour le blanc -> tour du noir
            println("Blanc: Pas de placement possible, on passe ton tour")
            //gm.turn = false
            return false
          }
        } else {
          println("noir")
          if (!(isLegal(i,j,Color.BLACK))){
            // si aucun move pour le noir -> tour du blanc
            println("Noir: Pas de placement possible, on passe ton tour")
            //gm.turn = true
            return false
          }
        }
      }
    }
    println("Move possible")
    return true
  }

  def bothCheckLegalMoves(): Boolean = {
    //TODO
    return false
  }


  def fillGrid(tab : Array[Array[Coin]]): Array[Array[Coin]] = {
    for(i <- tab.indices){
      for(j <- tab(i).indices){
        var token: Coin = new Coin(i, j, Color.green, false)
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
      s += i + "\t"
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


  def displayscore(w : Int, b : Int): String = {
    return s"$w - $b"
  }

  // TODO
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
