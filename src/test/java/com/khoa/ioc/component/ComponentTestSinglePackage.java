package com.khoa.ioc.component;

import com.khoa.ioc.IoC;
import com.khoa.ioc.annotation.ComponentScan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@ComponentScan("com.khoa.ioc.component")
public class ComponentTestSinglePackage {
    static IoC ioC;

    @Test
    void testIocConfiguration() {
        ioC = IoC.initBeans(ComponentTest.class);
        AccountService bean = ioC.getBean(AccountService.class);

        Assertions.assertEquals(bean.success(), "success");
    }
}
