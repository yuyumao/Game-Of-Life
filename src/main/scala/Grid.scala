import java.awt.{Color, Point}

import MyFrame.controllerActor

import scala.swing.Panel
import scala.swing.event.{ButtonClicked, MouseClicked}

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

  listenTo(mouse.clicks)
  reactions += {
    case e: MouseClicked =>
      life = !life
      updateColor()
  }
}