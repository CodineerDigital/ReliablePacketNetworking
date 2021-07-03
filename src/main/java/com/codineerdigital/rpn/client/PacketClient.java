package com.codineerdigital.rpn.client;

import com.codineerdigital.rpn.packets.Packet;
import com.codineerdigital.rpn.packets.PacketListener;
import com.codineerdigital.rpn.packets.PacketParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketClient extends Thread{

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final List<PacketListener> listeners;
    private PacketParser parser;

    public PacketClient() {
        listeners = new ArrayList<>();
    }

    public void connect(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            this.start();

        } catch (IOException e) {
            System.out.println("Failed to connect to PacketServer at " + host + ":" + port);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String input;
            while ((input = in.readLine()) != null) {
                String[] parts = input.split("\u3000");
                if (parser != null) {
                    for (PacketListener listener : listeners) {
                        listener.packetReceived(parser.parsePacket(new Packet(parts[0], Arrays.copyOfRange(parts, 1, parts.length)),
                                clientSocket.getRemoteSocketAddress().toString().split("/")[1]),
                                clientSocket.getRemoteSocketAddress().toString().split("/")[1],
                                null);
                    }
                } else {
                    for (PacketListener listener : listeners) {
                        listener.packetReceived(new Packet(parts[0], Arrays.copyOfRange(parts, 1, parts.length)),
                                clientSocket.getRemoteSocketAddress().toString().split("/")[1],
                                null);
                    }
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Connection to PacketServer lost.");
            e.printStackTrace();
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

    public void registerListener(PacketListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(PacketListener listener) {
        listeners.remove(listener);
    }

    public List<PacketListener> getListeners() {
        return listeners;
    }
    public void registerParser(PacketParser parser) {
        this.parser = parser;
    }

    public void unregisterParser() {
        this.parser = null;
    }

    public PacketParser getParser() {
        return parser;
    }

}
