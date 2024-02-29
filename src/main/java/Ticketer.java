import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Ticketer {

    public static void main(String[] args) {
        try {
            var server = HttpServer.create(new InetSocketAddress(8080), 0);
            new TicketAPIController(server);
            server.start();
        } catch (IOException ignore) {}
    }
}