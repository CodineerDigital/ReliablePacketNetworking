# Reliable Packet Networking
Reliable Packet Networking is a lightweight library for creating Client/Server networking using pre-defined or undefined packets.
It is not using any dependencies. Just default Java.

# Client Example
```
import com.codineerdigital.rpn.client.PacketClient;

public class Client {

    public static void main(String[] args) {
        PacketClient client = new PacketClient();
        /* Optional */
        client.registerListener(new MyPacketListener());
        client.registerParser(new MyPacketParser());
        /* Optional */
        client.connect("localhost", 34465);
    }

}
```

# Server Example
```
import com.codineerdigital.rpn.server.PacketServer;

public class Server {

    public static void main(String[] args) {
        PacketServer server = new PacketServer();
        /* Optional */
        server.registerListener(new MyPacketListener());
        server.registerParser(new MyPacketParser());
        /* Optional */
        server.start(34465);
    }

}
```

# Packet Listener Example
```
import com.codineerdigital.rpn.packets.Packet;
import com.codineerdigital.rpn.packets.PacketListener;
import com.codineerdigital.rpn.server.ClientHandler;

public class ExampleListener implements PacketListener {

    @Override
    public void packetReceived(Packet packet, String host, ClientHandler handler) {
        if (packet instanceof MyWonderfulPacket) {
            MyWonderfulPacket p = (MyWonderfulPacket) packet;
            System.out.println("My name is " + p.getName());
        }
    }
}
```

# Protocol
The protocol in Reliable Packet Networking is the way packets are constructed and is comparable to a standard for packets that guarantees communication between each client/server with identical protocol version. Because of this compatibility aspect, the protocol version should be changed as rarely as possible. The current protocol version is 2 and applies to RPN version 1.0.1 or later.
## Protocol Versions
| Protocol Version | RPN Versions |
| ------ | ------ |
| 1 | 1.0 |
| 2 | 1.0.1+ |

# FAQ
<details><summary>Which Java version is required?</summary>
The library requires Java 8 or newer.
</details>
<details><summary>Is there a maven/gradle dependency I can use?</summary>
Yes, you can find the maven/gradle dependency here: https://gitlab.codineerdigital.com/public-content/reliablepacketnetworking/-/packages/1
</details>
<details><summary>Can client and server with different Protocol versions work together?</summary>
Theoretically yes, but not by default. You would need a custom protocol implementation or a custom Packet Parser that is doing the changes.
</details>
