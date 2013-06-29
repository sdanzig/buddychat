package scottdanzig.buddychat.actors

import akka.actor.Actor
import akka.actor.FSM
import scala.collection._
import akka.actor.Props
import akka.actor.ActorRef
import PartialFunction._

class ChatManager extends Actor with FSM[State, Data] {
  val NUM_OF_BUDDIES = 3

  startWith(ChatOffline, Uninitialized)
  
  // When chat is offline:
  // CreateChat - create a new set of buddies and make chat go online
  // StartChat - make chat go online with existing buddies
  when(ChatOffline) {
    case Event(CreateChat, Uninitialized) => {
      log.debug("CreateChat received while in ChatOffline state.")
      val user = context.actorOf(Props[UserActor], "user")
      val list = (1 to NUM_OF_BUDDIES).map(num => context.actorOf(Props[BuddyActor], "buddy"+num)).toList
      goto(ChatOnline) using ChatData(user :: list, List[String]())
    }
    case Event(StartChat, ChatData(chatters, _)) => {
      log.debug("StartChat received while in ChatOffline state.")
      goto(ChatOnline) using ChatData(chatters, List[String]())
    }
  }

  onTransition {
    case ChatOffline -> ChatOnline => {
      for((Uninitialized, ChatData(chatters, _)) <- Some(stateData, nextStateData)) {
        chatters.foreach(_ ! Begin)
      }
    }
  }
  
  // When chat is online:
  // Speak - forward a message to chat participants and add it to the history
  // StopChat - bring the chat offline and erase the history
  when(ChatOnline) {
    case Event(Speak(text), chatData @ ChatData(chatters, msgsSoFar)) => {
      log.debug("Message({}) event received while in ChatOnline state. chatData={}", text, chatData)
      val labeledText = sender.path.name+": "+text
      (chatters diff List(sender)).foreach(_ forward Speak(text))
      stay using ChatData(chatters, msgsSoFar :+ labeledText)
    }
    case Event(StopChat, ChatData(chatters, _)) => {
      log.debug("StopChat event received while in ChatOnline state.")
      goto(ChatOffline) using ChatData(chatters, Nil)
    }
  }

  // In any state:
  // KillChat - print the chat history to console and shutdown the application
  // Any other message - invalid for the current state and ignored
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

// States of FSM
sealed trait State
case object ChatOffline extends State
case object ChatOnline extends State

// Data that may be retained within FSM
sealed trait Data
case object Uninitialized extends Data
case class ChatData(chatters: List[ActorRef], messages: List[String]) extends Data