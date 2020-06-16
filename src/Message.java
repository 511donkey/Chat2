import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private Client client;
    private String text;
    private LocalDateTime dateTime;
    private int ClientId;

    public Message() {
    }

    public Message(String text, LocalDateTime dateTime, int ClientId) {
        this.text = text;
        this.dateTime = dateTime;
        ClientId = client.getId();
    }

    public String getText() {
        return text;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setDateTime() {
        this.dateTime = LocalDateTime.now();
    }

    public void setClientId(int id) {
        this.ClientId = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "client=" + client +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                ", ClientId=" + ClientId +
                '}';
    }

    public static Message getInstance(String text, int clientId) {
        return new Message(text, LocalDateTime.now(), clientId);
    }
}
