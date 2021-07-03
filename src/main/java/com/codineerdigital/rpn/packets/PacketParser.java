package com.codineerdigital.rpn.packets;

public abstract class PacketParser {

    public abstract Packet parsePacket(Packet packet, String host);
}
