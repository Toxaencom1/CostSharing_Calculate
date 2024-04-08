package com.taxah.diplom_calculate.service;

import com.taxah.diplom_calculate.model.CalculateCheck;
import com.taxah.diplom_calculate.model.database.Check;
import com.taxah.diplom_calculate.model.Debt;
import com.taxah.diplom_calculate.model.database.ProductUsing;
import com.taxah.diplom_calculate.model.database.Session;
import com.taxah.diplom_calculate.model.database.TempUser;
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
        return crossDebtRecalculate(debtList);
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

    /**
     * Method: crossDebtRecalculate
     * Description: Recalculates debts among a list of Debt objects by comparing each debt with every other debt.
     *
     * @param debtList A list of Debt objects representing debts to be recalculated.
     * @return The input list of Debt objects after recalculation.
     */
    public List<Debt> crossDebtRecalculate(List<Debt> debtList) {
        if (!debtList.isEmpty()) {
            for (int i = 0; i < debtList.size(); i++) {
                for (int j = i + 1; j < debtList.size(); j++) {
                    recalculateDebts(debtList.get(i), debtList.get(j));
                }
            }
        }
        return debtList;

    }

    /**
     * Private Method: recalculateDebts
     * Description: Recalculates debts between two Debt objects and updates their debtor information accordingly.
     * takes into account different circumstances when comparing debt
     *
     * @param thisDebt  The first Debt object.
     * @param otherDebt The second Debt object.
     */
    private void recalculateDebts(Debt thisDebt, Debt otherDebt) {
        TempUser thisDebtor = thisDebt.getToWhom();
        TempUser otherDebtor = otherDebt.getToWhom();
        if (otherDebt.getDebtors().containsKey(thisDebt.getToWhom()) &&
                thisDebt.getDebtors().containsKey(otherDebt.getToWhom())) {
            Double thisExistingDebtValue = otherDebt.getDebtors().get(thisDebtor);
            Double otherExistingDebtValue = thisDebt.getDebtors().get(otherDebtor);
            if (thisExistingDebtValue.equals(otherExistingDebtValue)) {
                thisDebt.getDebtors().remove(otherDebtor);
                otherDebt.getDebtors().remove(thisDebtor);
            } else if (thisExistingDebtValue > otherExistingDebtValue) {
                thisDebt.getDebtors().remove(otherDebtor);
                otherDebt.getDebtors().put(thisDebtor, thisExistingDebtValue - otherExistingDebtValue);
            } else if (thisExistingDebtValue < otherExistingDebtValue) {
                otherDebt.getDebtors().remove(thisDebtor);
                thisDebt.getDebtors().put(otherDebtor, otherExistingDebtValue - thisExistingDebtValue);
            }
        }
    }
}
