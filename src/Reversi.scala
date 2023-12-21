import java.awt.Color

class Reversi {
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)


  // TODO
  def play(): Unit ={
    grid = fillGrid(grid)
    for (i : Int <- 3 to 4){
      for(j : Int <- 3 to 4){
        grid(i)(j).busy = true
      }
    }
    grid(3)(3).c = Color.white
    grid(4)(4).c = Color.white
    grid(3)(4).c = Color.black
    grid(4)(3).c = Color.black
  }

  // TODO

  def isOccupied(): Boolean = {

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
        var token: Coin = new Coin
        token.c = Color.green
        tab(i)(j) = token

      }
    }
    return tab
  }
}


class Coin{
  // TODO
  var c : Color = Color.green
  var busy : Boolean = false
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
