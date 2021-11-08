import java.util.ArrayList;
import java.util.List;

class Person {
  public String name;
  public ChatRoom room;
  private List<String> chatLog;

  public Person(String name) {
    this.name = name;
    chatLog = new ArrayList<>();
  }

  public void printChatLog() {
    for (String s : chatLog) {
      System.out.println(s);
    }
  }

  public void receive(String sender, String message) {
    String s = sender + ": '" + message + "'";
    System.out.println("[" + this.name + "'s chat session] " + s);
    chatLog.add(s);
  }

  public void say(String message) {
    room.broadcast(this.name, message);
  }

  public void privateMessage(String destination, String message) {

    room.message(this.name, destination, message);
  }

  public void removeLastMessage() {
    room.removeLastMessageFromMe(this.name);

  }

  public void removeLastMessageFrom(String sender) {
    for (int i = chatLog.size() - 1; i >= 0; i--) {
      if (chatLog.get(i).startsWith(sender + ":")) {
        chatLog.remove(i);
        return;
      }
    }
  }
}

class ChatRoom {
  private List<Person> people;

  public ChatRoom() {
    people = new ArrayList<>();
  }

  public void broadcast(String source, String message) {
    for (Person person : people)
      if (!person.name.equals(source))
        person.receive(source, message);
  }

  public void removeLastMessageFromMe(String sender) {
    for (Person person : people) {
      if (!person.name.equals(sender)) {
        person.removeLastMessageFrom(sender);
      }
    }
  }

  public void join(Person p) {
    String joinMsg = p.name + " joins the chat";
    broadcast("room", joinMsg);

    p.room = this;
    people.add(p);
  }

  public void leave(Person p) {
    String leaveMsg = p.name + " leaves the chat";
    people.remove(p);
    broadcast("room", leaveMsg);

    p.room = null;
  }

  public void message(String source, String destination, String message) {
    people.stream()
      .filter((Person p) -> p.name.equals(destination))
      .findFirst()
      .ifPresent((Person p) -> p.receive(source, message));
  }
}

class ChatRoomDemo {
  public static void main(String[] args) {
    ChatRoom room = new ChatRoom();

    Person john = new Person("John");
    Person jane = new Person("Jane");

    room.join(john);
    room.join(jane);

    john.say("hi room");
    jane.say("oh, hey john");

    Person simon = new Person("Simon");
    room.join(simon);
    simon.say("hi everyone!");

    jane.privateMessage("Simon", "glad you could join us!");

    simon.removeLastMessage();
    room.leave(simon);
  }
}
