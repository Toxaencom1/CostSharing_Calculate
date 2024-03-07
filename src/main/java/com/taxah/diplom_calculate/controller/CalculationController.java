package com.taxah.diplom_calculate.controller;

import com.taxah.diplom_calculate.model.Debt;
import com.taxah.diplom_calculate.model.database.Session;
import com.taxah.diplom_calculate.service.CalculationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calc")
@AllArgsConstructor
public class CalculationController {
    private CalculationService service;

    @PostMapping("/execute")
    public List<Debt> calculate(@RequestBody Session mySession){
        return service.calculateSession(mySession);
    }
}
