package com.codineerdigital.rpn.server;

import com.codineerdigital.rpn.packets.PacketListener;
import com.codineerdigital.rpn.packets.PacketParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class PacketServer {

    private ServerSocket serverSocket;
    private final Thread shutdownThread;
    private boolean shutdown;
    private final List<PacketListener> listeners;
    private PacketParser parser;

    public PacketServer() {
        this.shutdownThread = new Thread(() -> {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.shutdown = false;
        listeners = new ArrayList<>();
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Failed to start PacketServer at port " + port + ".");
            e.printStackTrace();
            return;
        }
        Runtime.getRuntime().addShutdownHook(shutdownThread);
        while (!shutdown) {
            try {
                new ClientHandler(serverSocket.accept(), this).start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to accept connection to PacketServer running at port " + port + ".");
            }
        }
    }

    public void stop() {
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
        shutdown = true;
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
