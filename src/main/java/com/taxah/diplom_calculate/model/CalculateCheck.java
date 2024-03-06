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
    private Map<TempUser, Double> allMembers = new HashMap<>();
    private List<TempUser> sessionMembers;
    private PayFact payFact;
    private List<ProductUsing> productUsingList;

    public CalculateCheck(List<TempUser> sessionMembers, PayFact payFact, List<ProductUsing> productUsingList) {
        this.sessionMembers = sessionMembers;
        this.payFact = payFact;
        this.productUsingList = productUsingList;
        for (TempUser tempUser : sessionMembers) {
            allMembers.put(tempUser, 0.0);
        }
    }

    public Debt execute() {
        TempUser toWhom = payFact.getTempUser();
        for (ProductUsing pu : productUsingList) {
            for (TempUser tu : pu.getUsers()) {
                allMembers.remove(toWhom);
                if (allMembers.containsKey(tu)) {
                    if (!payFact.getTempUser().equals(tu)) {
                        Double existingValue = allMembers.get(tu);
                        allMembers.put(tu, existingValue + (pu.getCost() / pu.getUsers().size()));
                    }
                }
            }
        }
        allMembers.entrySet().removeIf(entry -> entry.getValue().equals(0.0));
        return new Debt(toWhom, allMembers);
    }
}
