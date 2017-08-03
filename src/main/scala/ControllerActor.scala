import akka.actor.{Actor, ActorRef}

final case class ControllerActor(actorList: List[ActorRef]) extends Actor {

  actorList.foreach { actor =>
    actor ! CalculateNextState()
  }

  var readyGrid = 0

  override def receive: Receive = {
    case CalculateNextStateReady() =>
      readyGrid += 1
      //println("CalculateNextStateReady: " + readyGrid  + " Size: " + actorList.size)
      if(readyGrid == actorList.size) {
        actorList.foreach { actor =>
          actor ! SwitchToNextState()
        }
        readyGrid = 0
      }
    case SwitchToNextStateReady() =>
      readyGrid += 1
      if(readyGrid == actorList.size) {
        Thread.sleep(500)
        actorList.foreach { actor =>
          actor ! CalculateNextState()
        }
        readyGrid = 0
      }
    case _ => throw new RuntimeException("Unknown Command")
  }
}