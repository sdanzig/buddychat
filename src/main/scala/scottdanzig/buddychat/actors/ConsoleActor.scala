package scottdanzig.buddychat.actors

import akka.actor.Actor
import akka.event.Logging

class ConsoleActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case EnableConsole => {
      log.debug("EnableConsole received")
      acceptUserInput
    }
  }
  
  def acceptUserInput = {
	println(
"""Please type something for your buddies and press enter!
Or, you can type:
stop, to disable chat
start, to restart it
or done, to exit this program!"""
	)
	for( ln <- (io.Source.stdin.getLines.takeWhile(!_.equals("done"))) ) {
	  log.debug("Line = {}",ln)
	  context.parent ! MessageFromConsole(ln)
	}
    context.parent ! MessageFromConsole("done")
  }
}

case class EnableConsole