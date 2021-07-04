package com.codineerdigital.rpn.client;

import com.codineerdigital.rpn.packets.Packet;
import com.codineerdigital.rpn.packets.PacketListener;
import com.codineerdigital.rpn.packets.PacketParser;
import com.codineerdigital.rpn.packets.PacketValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketClient extends Thread {

    /**
     * The clients socket.
     */
    private Socket clientSocket;
    /**
     * Writer used to send messages to the server.
     */
    private PrintWriter out;
    /**
     * Reader reading the input stream from the server.
     */
    private BufferedReader in;
    /**
     * A list of all the listeners that have been registered for this client.
     */
    private final List<PacketListener> listeners;
    /**
     * A list of all PacketValidators that have been registered for this client.
     */
    private final List<PacketValidator> validators;
    /**
     * The PacketParser that is used to parse custom packet definitions.
     */
    private PacketParser parser;

    /**
     * The constructor is initializing the listener list.
     */
    public PacketClient() {
        listeners = new ArrayList<>();
        validators = new ArrayList<>();
    }

    /**
     * Connect to a server.
     * @param host The remote servers host address.
     * @param port The remote servers port.
     * @throws IOException exception thrown if connection fails.
     */
    public void connect(final String host, final int port) throws IOException {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            this.start();
    }

    /**
     * The method containing all the incoming packet handling.
     */
    @Override
    public void run() {
        try {
            String input;
            boolean valid = true;
            while ((input = in.readLine()) != null) {
                String[] parts = input.split("\u0001");
                Packet packet;
                if (parser != null) {
                    packet = parser.parsePacket(new Packet(parts[0], Arrays.copyOfRange(parts, 1, parts.length)),
                            clientSocket.getRemoteSocketAddress().toString().split("/")[1]);
                } else {
                    packet = new Packet(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
                }
                for (PacketValidator validator : validators) {
                    valid = validator.validatePacket(packet);
                    if (!valid) {
                        System.out.println("Received invalid packet from " + clientSocket.getRemoteSocketAddress().toString() + "! RAW: " + input);
                    }
                }
                if (!valid) {
                    continue;
                }
                for (PacketListener listener : listeners) {
                    listener.packetReceived(packet,
                            clientSocket.getRemoteSocketAddress().toString().split("/")[1], null);
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

    /**
     * Send a packet to the remote server (if connected).
     * @param packet The packet to be send.
     */
    public void sendPacket(final Packet packet) {
        out.println(packet.uniqueIdentifier + "\u0001" + String.join("\u0001", packet.arguments));
    }

    /**
     * Close the connection to the remote server.
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
    public void unregisterListener(final PacketListener listener) {
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
    public void registerParser(final PacketParser parser) {
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
     * Register a PacketValidator to the client.
     * @param validator the Validator to be registered.
     */
    public void registerPacketValidator(final PacketValidator validator) {
        this.validators.add(validator);
    }

    /**
     * Unregister a PacketValidator to the client.
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
