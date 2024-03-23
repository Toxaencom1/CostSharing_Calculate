package com.taxah.diplom_calculate.model.database;

import com.taxah.diplom_calculate.model.database.abstracts.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TempUser extends Account {

    private Long sessionId;

    private List<Long> productUsingList;

    public TempUser(Long sessionId, String firstName, String lastName) {
        super(firstName,lastName);
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return super.getFirstname()+" "+super.getLastname();
    }
}
