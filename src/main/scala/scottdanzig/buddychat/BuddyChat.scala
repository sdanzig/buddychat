package scottdanzig.buddychat

import akka.actor.ActorSystem
import akka.event.Logging
import akka.actor.Props
import scottdanzig.buddychat.actors._

object BuddyChat {
  
  val system = ActorSystem()
  
  val log = Logging(system, BuddyChat.getClass().getName())
  
  def main(args: Array[String]): Unit = run()
  
  def run() = {
    log.debug("Initializing chat system.")
    val manager = system.actorOf(Props[ChatManager], "manager")
    manager ! CreateChat
  }
}