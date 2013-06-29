package scottdanzig.buddychat.actors

import akka.actor.Actor
import akka.actor.FSM
import scala.collection._
import akka.actor.Props
import scala.collection.mutable.History
import akka.actor.ActorRef
import scala.collection.mutable.History

// States
sealed trait State
case object ChatOffline extends State
case object ChatOnline extends State

// Data
sealed trait Data
case object Uninitialized extends Data
case class ChatData(chatters: List[ActorRef], messages: List[String]) extends Data

class ChatManager extends Actor with FSM[State, Data] {
  startWith(ChatOffline, Uninitialized)
  
  when(ChatOffline) {
    case Event(CreateChat, Uninitialized) => {
      log.debug("CreateChat received while in ChatOffline state.")
      val user = context.actorOf(Props[UserActor], "user")
      val list = (1 to 3).map(num => context.actorOf(Props[BuddyActor], "buddy"+num)).toList
      user ! Begin
      goto(ChatOnline) using ChatData(user :: list, List[String]())
    }
    case Event(StartChat, ChatData(chatters, _)) => {
      log.debug("StartChat received while in ChatOffline state.")
      goto(ChatOnline) using ChatData(chatters, List[String]())
    }
    case Event(Message(text), Uninitialized) => {
      log.debug("Message({}) event received while in ChatOffline state. message ignored",text)
      stay
    }
  }

  when(ChatOnline) {
    case Event(Message(text), chatData @ ChatData(chatters, msgsSoFar)) => {
      log.debug("Message({}) event received while in ChatOnline state. chatData={}", text, chatData)
      val labeledText = sender.path.name+": "+text
      (chatters diff List(sender)).foreach(_ forward Message(text))
      stay using ChatData(chatters, msgsSoFar :+ labeledText)
    }
    case Event(StopChat, ChatData(chatters, _)) => {
      log.debug("StopChat event received while in ChatOnline state.")
      goto(ChatOffline) using ChatData(chatters, Nil)
    }
  }

  whenUnhandled {
    case Event(KillChat, ChatData(_, msgsIfAny)) => {
      log.info("Chat shutting down")
      println("Shutting down...\n\n"+
        "-- Begin server chat log --")
      msgsIfAny.foreach(println(_))
      println("-- End server chat log --")
      context.system.shutdown
      stay
    }
    case Event(e, s) =>
      log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
      stay
  }
}

// Events
case class Message(text: String)
case class CreateChat
case class StartChat
case class StopChat
case class KillChat
