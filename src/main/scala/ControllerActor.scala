import akka.actor.{Actor, ActorRef}

final case class ControllerActor(actorList: List[ActorRef]) extends Actor {
  var readyGrid = 0
  var running = false
  override def receive: Receive = {
    case StartSystem() =>
      readyGrid = 0
      actorList.foreach { actor =>
        running = true
        println("Start")
        actor ! CalculateNextState()
      }
    case StopSystem() =>
      running = false
      println("Stop")
    case CalculateNextStateReady() =>
      readyGrid += 1
      if(readyGrid == actorList.size) {
        actorList.foreach { actor =>
          println("Calculate next state ready")
          actor ! SwitchToNextState()
        }
        readyGrid = 0
      }
    case SwitchToNextStateReady() =>
      readyGrid += 1
      if(readyGrid == actorList.size && running) {
        println("Calculate to next state ready")
        actorList.foreach { actor =>
          actor ! CalculateNextState()
        }
        readyGrid = 0
      }
    case _ => throw new RuntimeException("Unknown Command")
  }
}