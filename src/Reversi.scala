import java.awt.Color
import hevs.graphics.FunGraphics

import java.awt.event
import java.awt.event.MouseListener

class Reversi {
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)
  var gm : GameManager = new GameManager(false)
  //var display: FunGraphics = new FunGraphics(300,300,"Reversi")
  //display.addMouseListener(MouseListener)


  // TODO
  def play(): Unit ={
    //fill grid with the default configuration
    grid = fillGrid(grid)

    //asks game mode and adapts game to it
    gm.askGameMode()
    if (gm.gameMode == 1){

    }

    if(gm.gameMode == 2) {

    }
  }


  def isOccupied(row: Int, col: Int): Boolean = {
    if (!grid(row)(col).busy){
      return false
    }
    return true
  }


  // @param x, y: coordinate of choosen placement
  // @param color: color of players coin
  def isLegal(x: Int, y: Int, color: Color): Boolean = {
    if (isOccupied(x, y)){
      return false
    }

    // all possible directions
    for (i <- -1 to 1) {
      for (j <- -1 to 1) {
        if (i != 0 || j != 0) { // On ignore car cela voudrait dire rester à la même place
          var nx = x + i
          var ny = y + j

          // on continue dans la même direction si le jeton est toujours de la couleur opposée
          // tjr dans les limites de la grille
          if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && grid(nx)(ny).c != color) {
            nx += i
            ny += j

            // on continue dans la direction et on check les possibilités
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
              // La dernière cellule de la ligne est vide, donc pas possible de mettre un jeton aux coordonnées x,y
              if (!grid(nx)(ny).busy) {
                return false
              } else if (grid(nx)(ny).c == color) {
              // A la fin de la ligne, on trouve un jeton de notre couleur, on peut donc placer le jeton aux coordonnées x,y
                return true
              }

              // tant que les jetons trouvés sont de la couleur opposée, on avance dans la même direction
              nx += i
              ny += j
            }
          }
        }
      }
    }
  // If we've checked all directions and none of them worked, the move is not legal
  return false
}

  // TODO

  def placeCoin(x: Int, y: Int): Unit = {
    // TODO: Check la couleur de isLegal
    if(!(isOccupied(x,y)) && isLegal(x,y, Color.black)){
      grid(x)(y).busy = true
      if (gm.turn) {
        grid(x)(y).c = Color.white
      } else
        grid(x)(y).c = Color.black
      gm.turn = !gm.turn
    }

  }


  def updateCoins(c : Color,x : Int, y : Int): Unit = {

  }

  //TODO

  def endGame(): Boolean = {
    // TODO
    return true
  }


  def fillGrid(tab : Array[Array[Coin]]): Array[Array[Coin]] = {
    for(i <- tab.indices){
      for(j <- tab(i).indices){
        var token: Coin = new Coin((i + 'A'.toInt).toChar, j+1, Color.green,false)
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


class Coin(var row: Char, var col: Int, var c: Color, var busy: Boolean){

}

class GameManager(var turn : Boolean) {
  // Default 1 player mode
  var gameMode: Int = 1

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
  def askPlacement(): Int = {
    var choice: Int = 0
    if (!turn){
      println("Player 1 place your next coin (ex: 11)")
      choice = Input.readInt()
      while((choice > 77) || (choice < 0)){
        println("Use the format: 01")
        choice = Input.readInt()
      }
    } else if ((turn) && (gameMode == 2)){
      println("Player 2 place your next coin (ex: 11)")
      choice = Input.readInt()
      while((choice > 77) || (choice < 0)){
        println("Use the format: 01")
        choice = Input.readInt()
      }
    }
    return choice
  }




}



object Reversi extends App{

  var g: Reversi = new Reversi

// tests
  g.fillGrid(g.grid)
  g.grid(3)(2).c = Color.BLACK
  g.grid(3)(2).busy = true
  print(g.toString)

}
