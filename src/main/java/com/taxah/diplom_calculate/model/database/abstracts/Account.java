package com.taxah.diplom_calculate.model.database.abstracts;

import lombok.Data;

@Data
public abstract class Account {
    private Long id;
    private String firstname;
    private String lastname;
    public Account() {
    }
    protected Account(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
