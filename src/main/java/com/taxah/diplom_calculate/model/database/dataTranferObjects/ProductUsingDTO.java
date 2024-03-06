package com.taxah.diplom_calculate.model.database.dataTranferObjects;


import com.taxah.diplom_calculate.model.database.TempUser;
import lombok.Data;

import java.util.List;


@Data
public class ProductUsingDTO {
    private Long checkId;
    private String productName;
    private Double cost;
    private List<TempUser> tempUsers;
}
