package com.taxah.diplom_calculate.service;

import com.taxah.diplom_calculate.model.CalculateCheck;
import com.taxah.diplom_calculate.model.database.Check;
import com.taxah.diplom_calculate.model.Debt;
import com.taxah.diplom_calculate.model.database.Session;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalculationService {
    public List<Debt> calculateSession(Session mySession) {
        List<Debt> debtList = new ArrayList<>();
        for (Check check : mySession.getCheckList()) {
            debtList.add(calculateCheck(mySession, check));
        }
//        System.out.println(debtList);
        for (int i = 0; i < debtList.size(); i++) {
            for (int j = i; j < debtList.size(); j++) {
                if (i != j && debtList.get(i).getToWhom().equals(debtList.get(j).getToWhom())) {
                    debtList.get(i).pileUp(debtList.get(j));
                    debtList.get(j).getDebtors().replaceAll((k, v) -> 0.0);
                }
            }
        }
        debtList.removeIf(d -> d.getDebtors().values().stream().allMatch(v -> v.equals(0.0)));
        System.out.println(debtList);
        return debtList;
    }

    private Debt calculateCheck(Session session, Check check) {
        return new CalculateCheck(session.getMembersList(), check.getPayFact(), check.getProductUsingList())
                .execute();
    }
}
