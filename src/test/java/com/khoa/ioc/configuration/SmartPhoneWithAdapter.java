package com.khoa.ioc.configuration;

public class SmartPhoneWithAdapter {
    private final SmartPhone smartPhone;
    public final Adapter adapter;

    public SmartPhoneWithAdapter(SmartPhone smartPhone, Adapter adapter) {
        this.smartPhone = smartPhone;
        this.adapter = adapter;
    }

    public void check() {
        System.out.println("SmartPhone: " + smartPhone);
        System.out.println("Adapter: " + adapter);
    }
}
