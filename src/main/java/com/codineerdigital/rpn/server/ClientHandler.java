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

    private final Socket clientSocket;
    private final PacketServer parentServer;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket, PacketServer parentServer) {
        this.clientSocket = clientSocket;
        this.parentServer = parentServer;
    }

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

    public void sendPacket(Packet packet) {
        out.println(packet.uniqueIdentifier + "\u3000" + String.join("\u3000", packet.arguments));
    }

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
