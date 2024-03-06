package com.taxah.diplom_calculate.model.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Check {
    private Long id;
    private Long checkId;
    private PayFact payFact;
    private String name;
    private List<ProductUsing> productUsingList;

}
