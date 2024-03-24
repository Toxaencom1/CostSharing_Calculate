package com.taxah.diplom_calculate.controller;

import com.taxah.diplom_calculate.model.Debt;
import com.taxah.diplom_calculate.model.database.Session;
import com.taxah.diplom_calculate.service.CalculationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for calculating debts and validating sessions.
 */
@RestController
@RequestMapping("/calc")
@AllArgsConstructor
public class CalculationController {
    private CalculationService service;

    /**
     * Calculate debts based on the session data.
     *
     * @param mySession the session data
     * @return the list of debts
     */

    @PostMapping("/execute")
    public List<Debt> calculate(@RequestBody Session mySession) {
        return service.calculateSession(mySession);
    }

    /**
     * Validate the session data.
     * Validating products sum and payFact.
     * Validating if the checks are empty.
     *
     * @param mySession the session data
     * @return the response entity
     */
    @PostMapping("/validate")
    public ResponseEntity<String> validateSession(@RequestBody Session mySession) {
        return service.validateSession(mySession);
    }
}
