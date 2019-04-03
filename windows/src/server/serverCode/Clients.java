import java.net.Socket;

public class Clients {
    private Socket socket;
    private String name;
    private String loggInTime;

    public Clients(String name, Socket socket, String loggInTime) {
        this.name = name;
        this.socket = socket;
        this.loggInTime = loggInTime;
    }

    public Socket getSocket() { return this.socket; }
    public String getName() { return this.name; }
    public String getLoggInTime() { return this.loggInTime; }
}
