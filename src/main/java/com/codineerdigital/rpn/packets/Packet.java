package com.codineerdigital.rpn.packets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Packet {

    public String uniqueIdentifier;
    public List<String> arguments;

    public Packet(String uniqueIdentifier, List<String> args) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.arguments = args;
    }

    public Packet(String uniqueIdentifier) {
        this(uniqueIdentifier, Collections.emptyList());
    }

    public Packet(String uniqueIdentifier, String... args) {
        this(uniqueIdentifier, Arrays.asList(args));
    }

}
