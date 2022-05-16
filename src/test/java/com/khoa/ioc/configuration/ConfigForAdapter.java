package com.khoa.ioc.configuration;

import com.khoa.ioc.annotation.Bean;
import com.khoa.ioc.annotation.Configuration;

@Configuration
public class ConfigForAdapter {
    @Bean
    public Adapter adapter() {
        return new Adapter();
    }
}
