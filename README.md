buddychat
=========

Akka FSM demo of an actor-based chat system

You're participating in a chatroom.  You run BuddyChat.  BuddyChat creates the manager of the chat.
This manager will create all the participants, both automated and human.  The one human participant
it creates represents you and will provide you an interface to make it speak in the chat room.
Whenever a participant speaks, the message goes to the chat manager who forwards the message on to
the other participants.

ChatManager is created as a finite state machine (Akka FSM).  ChatManager and the chat participants
(BuddyActor, the automated participant, and UserActor, the interface to a human) are Akka actors.
When a participant sends a Speak message to ChatManager, it's recorded by ChatManager then forwarded
to the other participants.  The chat history is passed among transitions as state data.

There's also the ability to enable and disable the chat room, toggling between ChatOnline and
ChatOffline states.  Going offline clears the chat history.  While offline, ChatManager ignores
Speak messages.
