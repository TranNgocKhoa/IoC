package com.khoa.ioc.configuration;

import com.khoa.ioc.IoC;
import com.khoa.ioc.annotation.ComponentScan;
import org.junit.jupiter.api.Test;

@ComponentScan("com.khoa.ioc")
public class IoCTest {
    static IoC ioC;

    @Test
    void testIocConfiguration() {
        ioC = IoC.initBeans(IoCTest.class);
        SmartPhone bean = ioC.getBean(SmartPhone.class);

        bean.check();
    }

    @Test
    void testMember() {
        ioC = IoC.initBeans(IoCTest.class);

        Connector bean = ioC.getBean(Connector.class);

        bean.check();
    }

    @Test
    void testMultipleSourceBean() {
        ioC = IoC.initBeans(IoCTest.class);

        SmartPhoneWithAdapter bean = ioC.getBean(SmartPhoneWithAdapter.class);

        bean.check();
    }
}
