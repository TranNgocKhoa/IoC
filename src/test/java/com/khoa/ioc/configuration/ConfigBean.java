package com.khoa.ioc.configuration;

import com.khoa.ioc.annotation.Bean;
import com.khoa.ioc.annotation.Configuration;

@Configuration
public class ConfigBean {
    @Bean
    public SmartPhone smartPhone(Screen screen, Battery battery, SoC soC, Connector connector) {
        return new SmartPhone(screen, battery, soC, connector);
    }
}
