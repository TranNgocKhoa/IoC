package com.khoa.ioc.component;

import com.khoa.ioc.annotation.Component;

@Component
public class AccountService {
    private final PersonService personService;

    public AccountService(PersonService personService) {
        this.personService = personService;
    }

    public String success() {
        if (personService != null) {
            return "success";
        }

        return "failed";
    }
}
