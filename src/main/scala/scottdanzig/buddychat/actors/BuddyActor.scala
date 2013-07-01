package scottdanzig.buddychat.actors

import akka.actor.Actor
import akka.event.Logging
import scala.util.Random

class BuddyActor extends Actor {
  val log = Logging(context.system, this)

  val rand = new Random(System.currentTimeMillis() + self.path.hashCode);

  // BuddyActors only respond to messages from the user
  def receive = {
    case message: ChatParticipantSystemMessage => message match {
      case Speak(msg) => {
        if (sender.path.name.equals("user")) {
          log.debug("Buddy {} received {}", self.path.name, msg)
          rand.nextInt(3) match {
            case 0 => { context.parent ! Speak(msg + " sounds good!") }
            case 1 => { context.parent ! Speak(msg + " sounds bad!") }
            case 2 => { context.parent ! Speak("I don't care about " + msg) }
          }
        } else {
          log.debug("BuddyActor ignored speaking of " + msg + " from another buddy.")
        }
      }
      case Begin() => {
        log.debug("BuddyActor ignored begin instruction.")
      }
    }
  }
}