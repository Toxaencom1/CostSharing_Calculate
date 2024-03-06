package com.taxah.diplom_calculate.model.database.dataTranferObjects;

import lombok.Data;

@Data
public class PayFactDTO {
    Long tempUserId;
    Double amount;
    Long sessionId;
}
