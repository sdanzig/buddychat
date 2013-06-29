package scottdanzig.buddychat.actors

// Messages for any actor handling a chat message
case class Speak(text: String) extends ChatSystemMessage

// Messages the chat manager can handle
sealed trait ChatSystemMessage
sealed trait ChatManagementSystemMessage extends ChatSystemMessage
case class CreateChat extends ChatManagementSystemMessage
case class StartChat extends ChatManagementSystemMessage
case class StopChat extends ChatManagementSystemMessage
case class KillChat extends ChatManagementSystemMessage

// Messages for any participants in the chat system (not the manager)
sealed trait ChatParticipantSystemMessage extends ChatSystemMessage
case class Begin extends ChatParticipantSystemMessage

// Messages for an actor representing a live user
sealed trait UserOnlySystemMessage extends ChatSystemMessage
case class MessageFromConsole(text: String) extends UserOnlySystemMessage

// Messages for the actor responsible for retrieving user input
sealed trait ConsoleOnlySystemMessage
case class EnableConsole extends ConsoleOnlySystemMessage
