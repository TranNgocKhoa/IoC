package com.khoa.ioc.configuration;

import com.khoa.ioc.annotation.Bean;
import com.khoa.ioc.annotation.Configuration;

@Configuration
public class ConfigBean {

    private final Wire wire;

    public ConfigBean(Wire wire) {
        this.wire = wire;
    }

    @Bean
    public Connector connector() {
        return new Connector(wire);
    }

    @Bean
    public SmartPhone smartPhone(Screen screen, Battery battery, SoC soC, Connector connector) {
        return new SmartPhone(screen, battery, soC, connector);
    }

    @Bean
    public SmartPhoneWithAdapter smartPhoneWithAdapter(SmartPhone smartPhone, Adapter adapter) {
        return new SmartPhoneWithAdapter(smartPhone, adapter);
    }


}
