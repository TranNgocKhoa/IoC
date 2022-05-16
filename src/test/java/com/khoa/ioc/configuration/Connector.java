package com.khoa.ioc.configuration;

public class Connector {
    private final Wire wire;

    public Connector(Wire wire) {
        this.wire = wire;
    }

    public void check() {
        System.out.println("Wire: " + wire);
    }
}
