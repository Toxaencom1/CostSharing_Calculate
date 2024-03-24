package com.taxah.diplom_calculate.model.database;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class for working with Rest API payFact
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayFact {
    private Long id;
    private Long checkId;
    private TempUser tempUser;
    private double amount;
}
