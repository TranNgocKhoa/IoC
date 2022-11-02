package com.khoa.ioc.component;

import com.khoa.ioc.annotation.Component;
import com.khoa.ioc.configuration.SmartPhone;

@Component
public class PersonWithPhone {
    private final SmartPhone smartPhone;

    public PersonWithPhone(SmartPhone smartPhone) {
        this.smartPhone = smartPhone;
    }

    public String success() {
        if (smartPhone != null) {
            return "success";
        }

        return "failed";
    }
}
