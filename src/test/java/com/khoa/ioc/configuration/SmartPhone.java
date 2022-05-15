package com.khoa.ioc.configuration;

public class SmartPhone {
    private final Screen screen;
    private final Battery battery;
    private final SoC soC;
    private final Connector connector;

    public SmartPhone(Screen screen, Battery battery, SoC soC, Connector connector) {
        this.screen = screen;
        this.battery = battery;
        this.soC = soC;
        this.connector = connector;
    }

    public void check() {
        System.out.println("Screen: " + screen);
        System.out.println("Battery: " + battery);
        System.out.println("SoC: " + soC);
        System.out.println("Connector: " + connector);
    }
}
