package com.khoa.ioc.configuration;

import com.khoa.ioc.annotation.Component;

@Component
public class Connector {
    private final Wire wire;

    public Connector(Wire wire) {
        this.wire = wire;
    }
}
