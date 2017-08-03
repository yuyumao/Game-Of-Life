import java.awt.{Color, Point}

import scala.swing.Panel

final case class Grid(point: Point, initLife: Boolean) extends Panel {
  private var life: Boolean = initLife
  updateColor()

  def hasLife: Boolean = life
  def setLife(value: Boolean): Unit = {
    life = value
    updateColor()
  }
  def updateColor(): Unit = {
    if(hasLife) {
      background = Color.BLACK
    } else {
      background = Color.PINK
    }
  }
}