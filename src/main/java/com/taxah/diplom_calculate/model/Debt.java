package com.taxah.diplom_calculate.model;

import com.taxah.diplom_calculate.model.database.TempUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Debt {
    private TempUser toWhom;
    private Map<TempUser,Double> debtors;

    public void pileUp(Debt debt){
        if (debt.getToWhom().equals(toWhom)){
            for (Map.Entry<TempUser,Double> entry : debt.getDebtors().entrySet()){
                TempUser user = entry.getKey();
                Double value = entry.getValue();
                if (debt.getDebtors().containsKey(user)){
                    Double existingValue = debt.getDebtors().get(user);
                    debt.getDebtors().put(user, existingValue + value);
                }
            }
        }
    }
}
