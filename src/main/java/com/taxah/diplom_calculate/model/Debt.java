package com.taxah.diplom_calculate.model;

import com.taxah.diplom_calculate.model.database.TempUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Class for working with debts
 */
@Data
@AllArgsConstructor
public class Debt {

    private TempUser toWhom;
    private Map<TempUser, Double> debtors;

    /**
     * Method for adding other debt to this debt
     *
     * @param debt - debt to add
     */
    public void pileUp(Debt debt) {
        for (Map.Entry<TempUser, Double> entry : debt.getDebtors().entrySet()) {
            TempUser user = entry.getKey();
            Double value = entry.getValue();
            if (this.getDebtors().containsKey(user)) {
                Double existingValue = this.getDebtors().get(user);
                this.getDebtors().put(user, existingValue + value);
            } else {
                this.getDebtors().put(user, value);
            }
        }
    }
}
