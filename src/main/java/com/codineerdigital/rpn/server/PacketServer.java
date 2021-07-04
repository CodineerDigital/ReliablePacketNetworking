package com.codineerdigital.rpn.server;

import com.codineerdigital.rpn.packets.PacketListener;
import com.codineerdigital.rpn.packets.PacketParser;
import com.codineerdigital.rpn.packets.PacketValidator;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class PacketServer {

    /**
     * The socket of the PacketServer.
     */
    private ServerSocket serverSocket;
    /**
     * The thread that is being executed on application shutdown.
     */
    private final Thread shutdownThread;
    /**
     * Defines if the server is supposed to be shut down.
     */
    private boolean shutdown;
    /**
     * The listeners that are being triggered when packets are coming in.
     */
    private final List<PacketListener> listeners;
    /**
     * A list of all PacketValidators that have been registered for this server.
     */
    private final List<PacketValidator> validators;
    /**
     * The parser that is parsing the incoming packets.
     */
    private PacketParser parser;

    /**
     * The constructor of the Packet Server.
     */
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
        validators = new ArrayList<>();
    }

    /**
     * Start the Packet Server.
     * @param port The port the server should run on.
     */
    public void start(final int port) {
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

    /**
     * Stop the Packet Server.
     */
    public void stop() {
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
        shutdown = true;
    }

    /**
     * Register a new packet listener.
     * @param listener The listener to be registered.
     */
    public void registerListener(final PacketListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregisters an already registered listener.
     * @param listener The listener to be unregistered.
     */
    public void unregisterListener(PacketListener listener) {
        listeners.remove(listener);
    }

    /**
     * Get the currently registered PacketListeners.
     * @return A list of all currently registered Listeners.
     */
    public List<PacketListener> getListeners() {
        return listeners;
    }

    /**
     * Register the PacketParser to enable custom parsed packets.
     * @param parser The parser to be registered.
     */
    public void registerParser(PacketParser parser) {
        this.parser = parser;
    }

    /**
     * Unregister the PacketParser to fallback to default packet handling.
     */
    public void unregisterParser() {
        this.parser = null;
    }

    /**
     * Get the currently active parser.
     * @return The parser or null if not registered.
     */
    public PacketParser getParser() {
        return parser;
    }

    /**
     * Register a PacketValidator to the server.
     * @param validator the Validator to be registered.
     */
    public void registerPacketValidator(final PacketValidator validator) {
        this.validators.add(validator);
    }

    /**
     * Unregister a PacketValidator to the server.
     * @param validator the Validator to be unregistered.
     */
    public void unregisterPacketValidator(final PacketValidator validator) {
        this.validators.remove(validator);
    }

    /**
     * Get the currently registered Packet Validators.
     * @return The list of all registered Packet Validators.
     */
    public List<PacketValidator> getValidators() {
        return validators;
    }
}
