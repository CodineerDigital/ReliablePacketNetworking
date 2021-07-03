package com.codineerdigital.rpn.packets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Packet {

    /**
     * Unique Identifier to know which package it is.
     * Should be the same on each version to ensure high compatibility.
     */
    public String uniqueIdentifier;
    /**
     * A list containing all the arguments that are passed with the packet.
     */
    public List<String> arguments;

    /**
     * Default Packet constructor.
     * @param uniqueIdentifier The packets unique ID.
     * @param args The packet content.
     */
    public Packet(final String uniqueIdentifier, final List<String> args) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.arguments = args;
    }

    /**
     * Packet constructor for empty packets.
     * @param uniqueIdentifier The packets unique ID.
     */
    public Packet(final String uniqueIdentifier) {
        this(uniqueIdentifier, Collections.emptyList());
    }

    /**
     * Super convenient packet constructor.
     * @param uniqueIdentifier The packets unique ID.
     * @param args The packet content.
     */
    public Packet(final String uniqueIdentifier, final String... args) {
        this(uniqueIdentifier, Arrays.asList(args));
    }

}
