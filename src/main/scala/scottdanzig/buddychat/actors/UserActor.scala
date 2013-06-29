package scottdanzig.buddychat.actors

import akka.actor.Actor
import akka.event.Logging
import akka.actor.Props

class UserActor extends Actor {
  val log = Logging(context.system, this)

  val console = context.actorOf(Props[ConsoleActor], "console")

  def receive = {
    case Begin => {
      log.debug("Enabling console")
      console ! EnableConsole
    }
    case MessageFromConsole(msgFromConsole) => {
      msgFromConsole match {
        case "done" => {
          log.debug("done received")
          context.parent ! KillChat
        }
        case "stop" => {
          log.debug("stop received")
          context.parent ! StopChat
        }
        case "start" => {
          log.debug("start received")
          context.parent ! StartChat
        }
        case _ => {
          context.parent ! Message(msgFromConsole)
          log.debug("Message from console received: {}",msgFromConsole)
        }
      }
    }
    case Message(msg) => {
      println(msg)
    }
    case _ => log.warning("UserActor received unknown message.")
  }
}

case class Begin
case class MessageFromConsole(text: String)