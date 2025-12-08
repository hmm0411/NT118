package course.examples.cinepople.model;

public class ChatMessage {
    public enum Type {
        SENT, RECEIVED
    }

    private String text;
    private Type type;

    public ChatMessage(String text, Type type) {
        this.text = text;
        this.type = type;
    }

    public String getText() { return text; }
    public Type getType() { return type; }
}
