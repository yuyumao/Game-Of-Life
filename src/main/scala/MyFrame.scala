/**
  * Created by Yuyumao on 2017/8/3.
  */

import swing._
import java.awt.Point

import akka.actor.{ActorRef, ActorSystem, Props}
import scala.swing.BorderPanel.Position.{Center, South}
import scala.swing.event.ButtonClicked

object MyFrame extends SimpleSwingApplication {
  val system = ActorSystem("HelloSystem")
  val width = 70
  val length = 70
  var controllerActor: ActorRef = null

  override def top = new MainFrame {
    title = "Game Of Life"
    var value = true

    val startButton = new Button {
      text = "Start"
      reactions += {
        case ButtonClicked(_) =>
          controllerActor ! StartSystem()
      }
    }

    val stopButton = new Button {
      text = "Stop"
      reactions += {
        case ButtonClicked(_) =>
          controllerActor ! StopSystem()
      }
    }

    val centerPanel = new GridPanel(width, length) {
      val actorMap: Map[Point, ActorRef] = (1 to width).flatMap { x =>
        (1 to length).map { y =>
          val point = new Point(x, y)
          val grid = Grid(point, initLife = false)
          contents += grid
          value = !value
          (point, system.actorOf(Props(GridActor(point, grid))))
        }
      }.toMap

      actorMap.foreach { case (point, actor) =>
        val neighbors = getNeighbors(point, actorMap)
        actor ! AssignNeighbors(neighbors)
      }
      controllerActor = system.actorOf(Props(ControllerActor(actorMap.values.toList)))
    }

    contents = new BorderPanel {
      layout(centerPanel) = Center
      layout(new GridPanel(2, 1) {
        contents += startButton
        contents += stopButton
      }) = South
    }

  }


  def getNeighbors(point: Point, map: Map[Point, ActorRef]): List[ActorRef] = {
    val pointList = List(
      new Point(point.x - 1, point.y),
      new Point(point.x + 1, point.y),
      new Point(point.x - 1, point.y + 1),
      new Point(point.x, point.y + 1),
      new Point(point.x + 1, point.y + 1),
      new Point(point.x - 1, point.y - 1),
      new Point(point.x, point.y - 1),
      new Point(point.x + 1, point.y - 1)
    )
    pointList.flatMap {
      case point: Point if validPoint(point) => map.get(point)
      case other => println(other); None
    }

  }

  def validPoint(point: Point): Boolean = {
    if(point.x > 0 && point.x <= width && point.y > 0 && point.y <= length) {
      true
    } else {
      false
    }
  }

}
