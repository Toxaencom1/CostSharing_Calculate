package com.taxah.diplom_calculate.model.database;

import com.taxah.diplom_calculate.model.database.abstracts.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class for working with Rest API tempUser
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TempUser extends Account {
    private Long sessionId;
    private List<Long> productUsingList;

    @Override
    public String toString() {
        return super.getFirstname()+" "+super.getLastname();
    }
}
