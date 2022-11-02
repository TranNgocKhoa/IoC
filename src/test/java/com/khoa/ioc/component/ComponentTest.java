package com.khoa.ioc.component;

import com.khoa.ioc.IoC;
import com.khoa.ioc.annotation.ComponentScan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@ComponentScan("com.khoa.ioc")
public class ComponentTest {

    static IoC ioC;

    @Test
    void testIocConfiguration() {
        ioC = IoC.initBeans(ComponentTest.class);
        AccountService bean = ioC.getBean(AccountService.class);

        Assertions.assertEquals(bean.success(), "success");
    }

    @Test
    void testIoCInjectBeanFromBeanAndComponent() {
        ioC = IoC.initBeans(ComponentTest.class);
        PersonWithPhone bean = ioC.getBean(PersonWithPhone.class);

        Assertions.assertEquals(bean.success(), "success");
    }
}
