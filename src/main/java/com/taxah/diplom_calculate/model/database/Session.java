package com.taxah.diplom_calculate.model.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for working with Rest API session
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    private Long id;
    private Long adminId;
    private List<TempUser> membersList = new ArrayList<>();
    private List<PayFact> payFact = new ArrayList<>();
    private List<Check> checkList = new ArrayList<>();
    private boolean isClosed;
}
