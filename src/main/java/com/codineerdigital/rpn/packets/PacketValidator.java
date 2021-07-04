package com.codineerdigital.rpn.packets;

public interface PacketValidator {

    /**
     * Validate a packet using custom code.
     * @param packet The packet that has to be validated.
     * @return Whether or not the packet is valid.
     */
    boolean validatePacket(Packet packet);

}
