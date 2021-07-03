package com.codineerdigital.rpn.server;

import com.codineerdigital.rpn.packets.Packet;
import com.codineerdigital.rpn.packets.PacketListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler extends Thread {

    /**
     * The socket handling the connection to the client.
     */
    private final Socket clientSocket;
    /**
     * The PacketServer controlling this handler.
     */
    private final PacketServer parentServer;
    /**
     * The OutputStream used to send packets to the client.
     */
    private PrintWriter out;
    /**
     * The InputStream used to receive packets from the client.
     */
    private BufferedReader in;

    /**
     * Initialisation of the ClientHandler.
     * @param clientSocket the Socket used for the client communication.
     * @param parentServer the PacketServer controlling this handler.
     */
    public ClientHandler(final Socket clientSocket, final PacketServer parentServer) {
        this.clientSocket = clientSocket;
        this.parentServer = parentServer;
    }

    /**
     * The method containing all the packet handling.
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String input;
            while ((input = in.readLine()) != null) {
                String[] parts = input.split("\u3000");
                if (parentServer.getParser() != null) {
                    for (PacketListener listener : parentServer.getListeners()) {
                        listener.packetReceived(parentServer.getParser().parsePacket(new Packet(parts[0], Arrays.copyOfRange(parts, 1, parts.length)),
                                clientSocket.getRemoteSocketAddress().toString().split("/")[1]),
                                clientSocket.getRemoteSocketAddress().toString().split("/")[1],
                                this);
                    }
                } else {
                    for (PacketListener listener : parentServer.getListeners()) {
                        listener.packetReceived(new Packet(parts[0], Arrays.copyOfRange(parts, 1, parts.length)),
                                clientSocket.getRemoteSocketAddress().toString().split("/")[1],
                                this);
                    }
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Send a packet to the Client.
     * @param packet the packet to be send.
     */
    public void sendPacket(final Packet packet) {
        out.println(packet.uniqueIdentifier + "\u3000" + String.join("\u3000", packet.arguments));
    }

    /**
     * Close the connection to the client.
     */
    public void close() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
