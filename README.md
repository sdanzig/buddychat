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

The last major detail is the ConsoleActor, which is used to asynchronously retrieve keyboard
input, freeing up the UserActor to respond to incoming Speak messages.

Sample output from BuddyChat:

```
Please type something for your buddies and press enter!
Or, you can type:
stop, to disable chat
start, to restart it
or done, to exit this program!
chicken
buddy1: chicken sounds bad!
buddy3: I don't care about chicken
buddy2: I don't care about chicken
the sky
buddy1: the sky sounds bad!
buddy3: the sky sounds bad!
buddy2: the sky sounds good!
done
Shutting down...

-- Begin server chat log --
user: chicken
buddy1: chicken sounds bad!
buddy3: I don't care about chicken
buddy2: I don't care about chicken
user: the sky
buddy1: the sky sounds bad!
buddy3: the sky sounds bad!
buddy2: the sky sounds good!
-- End server chat log --
[INFO] [06/30/2013 02:24:39.345] [default-akka.actor.default-dispatcher-7] [akka://default/user/manager] Chat shutting down
```
