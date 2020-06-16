import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private String ip;
    private int port;
    private Scanner scanner;
    private int id;

    ArrayList<Client> clients = new ArrayList<>();

    public Client() {
    }

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Client(String ip, int port, Scanner scanner) {
        this.ip = ip;
        this.port = port;
        this.scanner = scanner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSocket() throws IOException {
        Socket socket = new Socket(ip, port);
        return socket;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void createClient() {
        Client client = new Client(ip, port, scanner);
        System.out.println("введите id");
        id = scanner.nextInt();
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() != id) {
                client.setId(id);
            }
        }
    }

    private void sendMessage(Message message) {
        try (Connection connection = new Connection(getSocket())) {
            connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message printMessage() throws Exception {
        try (Connection connection = new Connection(getSocket());) {
            Message message = new Message();
            message.setText(connection.readMessage());
            message.setDateTime();
            message.setClientId(id);
            return message;
        }
    }

    public void start() {
        Client client = new Client();
        createClient();
        Message message = new Message();
        while (true) {
            System.out.println("введите сообшение");
            String text = scanner.nextLine();
            message.setText(text);
            message.setDateTime();
            message.setClientId(id);
            sendMessage(message);
        }
    }

    public static void main(String[] args) {
        int port = 8095;
        String ip = "127.0.0.1";
        Scanner scanner = new Scanner(System.in);

        try {
            Client client = new Client(ip, port, scanner);
            client.start();
            new Thread(new ClientReader(client)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class ClientReader implements Runnable {
    Client client;

    public ClientReader(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            try (Connection connection = new Connection(client.getSocket());) {
                while (true) {
                    Message message = client.printMessage();
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}