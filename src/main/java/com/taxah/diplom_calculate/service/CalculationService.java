package com.taxah.diplom_calculate.service;

import com.taxah.diplom_calculate.model.CalculateCheck;
import com.taxah.diplom_calculate.model.database.Check;
import com.taxah.diplom_calculate.model.Debt;
import com.taxah.diplom_calculate.model.database.ProductUsing;
import com.taxah.diplom_calculate.model.database.Session;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The service class for calculating the debts of the session participants
 */
@Service
public class CalculationService {
    /**
     * The method calculates the debts of the session participants
     *
     * @param mySession - session for which you need to calculate the debts
     * @return list of debts
     */
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

    /**
     * This is auxiliary method for calculating the debts of the session participants
     *
     * @param session - session for which you need to calculate the debts
     * @param check   - check for which you need to calculate the debts
     * @return debt
     */
    private Debt calculateCheck(Session session, Check check) {
        return new CalculateCheck(session.getMembersList(), check.getPayFact(), check.getProductUsingList())
                .execute();
    }

    /**
     * The method checks the correctness of the session.
     * Validating products sum and payFact.
     * Validating if the checks are empty.
     *
     * @param session - session for which you need to check the correctness
     * @return response entity with the result of the check
     */
    public ResponseEntity<String> validateSession(Session session) {
        String errorMessage = "";
        if (session.getCheckList().isEmpty()) {
            errorMessage = "Checks are empty";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        for (Check check : session.getCheckList()) {
            if (check.getPayFact() != null) {
                double payFactTotal = check.getPayFact().getAmount();
                double sumOfAllProducts = 0.0;
                for (ProductUsing productUsing : check.getProductUsingList()) {
                    sumOfAllProducts += productUsing.getCost();
                }
                if (payFactTotal != sumOfAllProducts) {
                    errorMessage = String.format("Check(%s) payFact(amount = %.0f) is not equals of products sum(%.0f)",
                            check.getName(), payFactTotal, sumOfAllProducts);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
                }
            } else {
                errorMessage = String.format("Check(%s) payFact is empty",
                        check.getName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
        }
        return ResponseEntity.ok("Ok");
    }
}
