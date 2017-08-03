/**
  * Created by Yuyumao on 2017/8/3.
  */

import swing._
import java.awt.Point
import akka.actor.{ActorRef, ActorSystem, Props}

object MyFrame extends SimpleSwingApplication {
  val system = ActorSystem("HelloSystem")
  val width = 39
  val length = 39
  override def top = new MainFrame {
    title = "Game Of Life"
    var value = true
    var count = 0
    contents = new GridPanel(width, length) {
      val actorMap: Map[Point, ActorRef] = (1 to width).flatMap { x =>
        (1 to length).map { y =>
          val point = new Point(x, y)
          println("Put " + point)
          val grid = Grid(point, value)
          contents += grid
          count += 1
          value = !value
          (point, system.actorOf(Props(GridActor(point, grid))))
        }
      }.toMap

      actorMap.foreach { case (point, actor) =>
        val neighbors = getNeighbors(point, actorMap)
        //println(s"Neighbors for $point are $neighbors")
        actor ! AssignNeighbors(neighbors)
      }

      //Thread.sleep(500)
      system.actorOf(Props(ControllerActor(actorMap.values.toList)))
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
