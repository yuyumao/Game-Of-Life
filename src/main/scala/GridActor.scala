import java.awt.Point

import akka.actor.{Actor, ActorRef}

final case class GridActor(co: Point, grid: Grid) extends Actor {
  var neighbors: List[ActorRef] = Nil
  var answers: List[Boolean] = Nil
  var controllerActor: Option[ActorRef] = None
  override def receive: Receive = {
    case DoYouHaveLifeAnswer(life) =>
      answers = life :: answers
      if(answers.size == neighbors.size) {
        controllerActor.get ! CalculateNextStateReady()
      }
    case DoYouHaveLife() =>
      sender ! DoYouHaveLifeAnswer(grid.hasLife)
    case CalculateNextState() =>
      controllerActor = Some(sender())
      neighbors.foreach(_ ! DoYouHaveLife())
    case SwitchToNextState() =>
      grid.setLife(workOutNextState)
      answers = Nil
      sender ! SwitchToNextStateReady()
    case AssignNeighbors(nei) =>
      neighbors = nei
    case aa =>
      println("Error "+ aa)
      throw new RuntimeException("Unknown Command")
  }

  def workOutNextState = {
    val lifes = answers.count(a => a)
    grid.hasLife match {
      case true if lifes < 2 => false
      case true if lifes >= 2 && lifes <=3 => true
      case true if lifes > 3 => false
      case false if lifes == 3 => true
      case _ => false
    }
  }
}