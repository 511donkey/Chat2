import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Connection implements AutoCloseable {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
    }

    public void sendMessage(Message message) throws IOException {
        out.write(message.getText());
        out.flush();
    }

    public String readMessage() throws IOException {
        return (String) in.readLine();
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
    }
}
