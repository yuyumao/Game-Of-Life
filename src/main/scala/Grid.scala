import java.awt.{Color, Point}

import scala.swing.Panel

final case class Grid(point: Point, initLife: Boolean) extends Panel {
  private var life: Boolean = initLife
  updateColor()

  def hasLife: Boolean = life
  def setLife(value: Boolean): Unit = {
    println("Setting lif to : " + value)
    life = value
    updateColor()
  }
  def updateColor(): Unit = {
    life match {
      case true => background = Color.BLACK
      case false => background = Color.PINK
    }
  }


}