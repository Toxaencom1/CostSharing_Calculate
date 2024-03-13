package com.taxah.diplom_calculate.service;

import com.taxah.diplom_calculate.model.CalculateCheck;
import com.taxah.diplom_calculate.model.database.Check;
import com.taxah.diplom_calculate.model.Debt;
import com.taxah.diplom_calculate.model.database.ProductUsing;
import com.taxah.diplom_calculate.model.database.Session;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalculationService {
    public List<Debt> calculateSession(Session mySession) {
        List<Debt> debtList = new ArrayList<>();
        for (Check check : mySession.getCheckList()) {
            debtList.add(calculateCheck(mySession, check));
        }
        for (int i = 0; i < debtList.size(); i++) {
            for (int j = i; j < debtList.size(); j++) {
                if (i != j && debtList.get(i).getToWhom().equals(debtList.get(j).getToWhom())) {
                    debtList.get(i).pileUp(debtList.get(j));
                    debtList.get(j).getDebtors().replaceAll((k, v) -> 0.0);
                }
            }
        }
        debtList.removeIf(d -> d.getDebtors().values().stream().allMatch(v -> v.equals(0.0)));
        return debtList;
    }

    private Debt calculateCheck(Session session, Check check) {
        return new CalculateCheck(session.getMembersList(), check.getPayFact(), check.getProductUsingList())
                .execute();
    }

    public ResponseEntity<String> validateSession(Session session){
        String errorMessage = "";
        if (session.getCheckList().isEmpty()){
            errorMessage = "Checks are empty";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        for (Check c : session.getCheckList()){
            if (c.getPayFact() != null){
                double payFactTotal = c.getPayFact().getAmount();
                double sumOfAllProducts = 0.0;
                for (ProductUsing pu:c.getProductUsingList()){
                    sumOfAllProducts+=pu.getCost();
                }
                if (payFactTotal!=sumOfAllProducts){
                    errorMessage = String.format("Check(%s) payFact(amount = %.0f) is not equals of products sum(%.0f)",
                            c.getName(),payFactTotal,sumOfAllProducts);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
                }
            }else {
                errorMessage = String.format("Check(%s) payFact is empty",
                        c.getName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

        }
        return ResponseEntity.ok("Ok");
    }
}
