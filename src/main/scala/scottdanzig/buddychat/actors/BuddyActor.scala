package scottdanzig.buddychat.actors

import akka.actor.Actor
import akka.event.Logging
import scala.util.Random

class BuddyActor extends Actor {
  val log = Logging(context.system, this)

  val rand = new Random(System.currentTimeMillis()+self.path.hashCode);

  def receive = {
    case Message(msg) if sender.path.name.equals("user") => {
      log.debug("Buddy {} received {}", self.path.name, msg)
      rand.nextInt(3) match {
        case 0 => { context.parent ! Message(msg+" sounds good!") }
        case 1 => { context.parent ! Message(msg+" sounds bad!") }
        case 2 => { context.parent ! Message("I don't care about "+msg) }
      }
    }
    case Message(msg) => {
//      log.warning("BuddyActor ignored message "+msg+" from another buddy.")
    }
    case _ => log.warning("BuddyActor received unknown message.")
  }
}