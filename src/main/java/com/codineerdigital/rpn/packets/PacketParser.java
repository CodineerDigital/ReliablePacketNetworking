package com.codineerdigital.rpn.packets;

public interface PacketParser {

    /**
     * Abstract method used to parse the incoming packet to a custom class extending packet.
     * @param packet the incoming packet.
     * @param host the host that sent the packet.
     * @return The custom or default packet that will be passed to the listener.
     */
    Packet parsePacket(Packet packet, String host);
}
