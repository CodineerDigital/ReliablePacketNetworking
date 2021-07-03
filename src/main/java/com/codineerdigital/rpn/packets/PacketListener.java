package com.codineerdigital.rpn.packets;

import com.codineerdigital.rpn.server.ClientHandler;

public interface PacketListener {

    /**
     * This method is being triggered if the Client or Server is receiving a packet.
     * @param packet The packet the listener is receiving.
     * @param host The host that send the packet.
     * @param handler the ClientHandler that triggered the listener. ONLY SERVERSIDE!
     */
    void packetReceived(Packet packet, String host, ClientHandler handler);

}
