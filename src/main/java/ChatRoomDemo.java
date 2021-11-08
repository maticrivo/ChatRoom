
import java.util.LinkedList;
import java.util.List;

class Person {
  public String name;
  public ChatRoom room;
  public List<String> chatLog;

  public Person(String name) {
    this.name = name;
    chatLog = new LinkedList<>();
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

    room.message(name, destination, message);
  }

  public void removeLastMessage() {
    room.removeLastMessageFromMe(this.name);

  }

  public void removeLastMessageFrom(String sender) {
    for (int i = chatLog.size() - 1; i > 0; i--) {
      String firstWordInMessage = chatLog.get(i).split(":")[0];
      if (firstWordInMessage.equals(sender)) {
        chatLog.remove(i);
        return;
      }
    }
  }
}

class ChatRoom {
  private List<Person> people;

  public ChatRoom() {
    people = new LinkedList<>();
  }

  public void broadcast(String source, String message) {
    for (Person person : people)
      if (!person.name.equals(source))
        person.receive(source, message);
  }

  public void removeLastMessageFromMe(String sender) {
    for (var person : people) {
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
    broadcast("room", leaveMsg);
    people.remove(p);
  }

  public void message(String source, String destination, String message) {
    people.stream()
      .filter((p) -> p.name.equals(destination))
      .findFirst()
      .ifPresent((p) -> p.receive(source, message));
  }
}

public class ChatRoomDemo {
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

    jane.privateMessage("Simon", "Hi Simon, I think you entered the wrong room!");

    simon.removeLastMessage();
    room.leave(simon);
  }
}
