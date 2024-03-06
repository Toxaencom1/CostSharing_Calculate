package com.taxah.diplom_calculate.service;

import com.taxah.diplom_calculate.model.CalculateCheck;
import com.taxah.diplom_calculate.model.database.Check;
import com.taxah.diplom_calculate.model.Debt;
import com.taxah.diplom_calculate.model.database.Session;
import com.taxah.diplom_calculate.model.database.TempUser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalculationService {
    public String calculate(Session mySession) {
        Map<TempUser,Double> sessionMap = new HashMap<>();
        for (TempUser tempUser:mySession.getMembersList()){
            sessionMap.put(tempUser,0.0);
        }

        List<Debt> debtList = new ArrayList<>();
        for (Check check : mySession.getCheckList()){
            debtList.add(calculateCheck(mySession, check));
        }
        System.out.println(debtList);

        return "OK!!!";
    }

    public Debt calculateCheck(Session session, Check check){
        return new CalculateCheck(session.getMembersList(),check.getPayFact(),check.getProductUsingList())
                .execute();
    }








//    private List<Long> uniqProductsId(List<TempUser> members){
//        Set<Long> uniqProductsId = new HashSet<>();
//        for (TempUser tu:members){
//            uniqProductsId.addAll(tu.getProductUsingList());
//        }
//        return uniqProductsId.stream().toList();
//    }
}
