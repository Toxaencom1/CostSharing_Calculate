package com.taxah.diplom_calculate.model;

import com.taxah.diplom_calculate.model.database.PayFact;
import com.taxah.diplom_calculate.model.database.ProductUsing;
import com.taxah.diplom_calculate.model.database.TempUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateCheck {
    private Map<TempUser, Double> debtMap = new HashMap<>();
    private List<TempUser> sessionMembers;
    private PayFact payFact;
    private List<ProductUsing> productUsingList;

    public CalculateCheck(List<TempUser> sessionMembers, PayFact payFact, List<ProductUsing> productUsingList) {
        this.sessionMembers = sessionMembers;
        this.payFact = payFact;
        this.productUsingList = productUsingList;
        for (TempUser tempUser : sessionMembers) {
            debtMap.put(tempUser, 0.0);
        }
    }

    public Debt execute() {
        TempUser toWhom = payFact.getTempUser();
        debtMap.remove(toWhom);
        for (ProductUsing pu : productUsingList) {
            for (TempUser tu : pu.getUsers()) {
                if (!tu.equals(toWhom)) {
                    Double existingValue = debtMap.get(tu);
                    debtMap.put(tu, existingValue + (pu.getCost() / pu.getUsers().size()));
                }
            }
        }
        debtMap.entrySet().removeIf(entry -> entry.getValue().equals(0.0));
        return new Debt(toWhom, debtMap);
    }
}
