import java.awt.Color

class Reversi {
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)
  var selected: Coin


  // TODO
  def play(): Unit ={
    grid = fillGrid(grid)

  }

  // TODO

  def isOccupied(row: Int, col: Int): Boolean = {
    if (!grid(row)(col).busy){

    }
    return true
  }

  // TODO

  def isLegal(): Boolean = {
    return true
  }

  // TODO

  def placeCoin(x: Int, y: Int): Unit = {
    // TODO
  }

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
}


class Coin(var row: Char, var col: Int, var c: Color, var busy: Boolean){
  // TODO

}

class GameManager {


  // TODO
  def askGameMode(b : Boolean): Unit = {
    // TODO
  }

  def askTurn(): Unit = {
    // TODO
  }

  def displayscore(): String = {
    // TODO
    return "aa"
  }
  // TODO
}



object Reversi extends App{

}
