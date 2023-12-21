import java.awt.Color

class Reversi {
  var grid: Array[Array[Coin]] = Array.ofDim(8,8)
  fillGrid()


  // TODO
  def play(): Unit ={

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

  }

  //TODO
  def endGame(): Boolean = {
    return true
  }

  def fillGrid(): Unit = {
    for(i <- grid.indices){
      for(j <- grid(i).indices){
        var token: Coin = new Coin(Color.green, false)
        grid(i)(j) = token
      }
    }
  }
}


class Coin(c : Color, busy : Boolean){

}

class GameManager {


  def askGameMode(b : Boolean): Unit = {

  }
  def askTurn(): Unit = {

  }
  def displayscore(): String = {

    return "aa"
  }


}



object Reversi extends App{

  var r: Reversi = new Reversi
  r.fillGrid()

}
