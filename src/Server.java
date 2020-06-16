import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;

public class Server implements ConnectionListener {
    private int port;
    private Connection connection;

    CopyOnWriteArraySet<Connection> connections = new CopyOnWriteArraySet<>();
    ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(7);

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Cервер запущен...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                connection = new Connection(clientSocket);
                connections.add(connection);
                new Thread(new Reader(messages, connection, port)).start();
                for (Message msg : messages) {
                    if (msg.getClientId() != messages.take().getClientId()) {
                        connections.forEach(connection -> {
                            try {
                                connection.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect(Connection connection) {
        connections.remove(connection);
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 8095;
        Server server = new Server(port);
        server.start();
    }

}

class Reader implements Runnable {
    int port;
    CopyOnWriteArraySet<Connection> connections = new CopyOnWriteArraySet<>();
    ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(7);
    Connection connection;

    public Reader(ArrayBlockingQueue<Message> messages, Connection connection, int port) {
        this.messages = messages;
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            Message message = new Message();
            message.setDateTime();
            //   message.setClientId();
            message.setText(connection.readMessage());
            messages.put(message);
            System.out.println(connection.readMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

