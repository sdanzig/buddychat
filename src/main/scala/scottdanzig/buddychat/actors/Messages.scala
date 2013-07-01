package scottdanzig.buddychat.actors

// Messages the chat manager can handle
sealed trait ChatManagementSystemMessage

// Messages for an actor representing a live user
sealed trait UserSystemMessage

// Messages for any participants in the chat system
sealed trait ChatParticipantSystemMessage extends UserSystemMessage

// Messages for the actor responsible for retrieving user input
sealed trait ConsoleSystemMessage

case class CreateChat extends ChatManagementSystemMessage
case class StartChat extends ChatManagementSystemMessage
case class StopChat extends ChatManagementSystemMessage
case class KillChat extends ChatManagementSystemMessage
case class Begin extends ChatParticipantSystemMessage
case class Speak(text: String) extends ChatParticipantSystemMessage with ChatManagementSystemMessage
case class MessageFromConsole(text: String) extends UserSystemMessage
case class EnableConsole extends ConsoleSystemMessage