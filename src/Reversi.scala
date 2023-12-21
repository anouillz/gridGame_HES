import java.awt.Color

class Reversi {
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)
  var selected: Coin
  var gm : GameManager = new GameManager(false)

  // TODO
  def play(): Unit ={
    grid = fillGrid(grid)

  }

  // TODO

  def isOccupied(row: Int, col: Int): Boolean = {
    if (!grid(row)(col).busy){
      return false
    }
    return true
  }

  // TODO

  def isLegal(): Boolean = {
    return true
  }

  // TODO

  def placeCoin(x: Int, y: Int): Unit = {
    if(!(isOccupied(x,y)) && isLegal()){
      grid(x)(y).busy = true
      if (gm.turn) {
        grid(x)(y).c = Color.white
      } else
        grid(x)(y).c = Color.black
      gm.turn = !gm.turn
    }
    // TODO
  }


  def updateCoins(c : Color,x : Int, y : Int)

  //TODO

  def endGame(): Boolean = {
    // TODO
    return true
  }



  def fillGrid(tab : Array[Array[Coin]]): Array[Array[Coin]] = {
    for(i <- tab.indices){
      for(j <- tab(i).indices){
        var token: Coin = new Coin((i + ('A'.toInt)).toChar, j+1, Color.green,false)
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

}


class Coin(var row: Char, var col: Int, var c: Color, var busy: Boolean){
}

class GameManager(var turn : Boolean) {


  // TODO
  def askGameMode(b : Boolean): Unit = {
    // TODO
  }

  def askTurn(): Unit = {
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
}



object Reversi extends App{

}
