package com.codineerdigital.rpn.packets;

import com.codineerdigital.rpn.server.ClientHandler;

public interface PacketListener {

    void packetReceived(Packet packet, String host, ClientHandler handler);

}
