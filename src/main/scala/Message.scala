import akka.actor.ActorRef

sealed trait Message

final case class CalculateNextState() extends Message
final case class CalculateNextStateReady() extends Message

final case class SwitchToNextState() extends Message
final case class SwitchToNextStateReady() extends Message

final case class AssignNeighbors(neighbors: List[ActorRef]) extends Message
final case class AssignNeighborsReady() extends Message

final case class DoYouHaveLife() extends Message
final case class DoYouHaveLifeAnswer(life: Boolean) extends Message