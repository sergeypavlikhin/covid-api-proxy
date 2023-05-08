package com.pvsrg.covidapi.web.cases;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

// TODO Swagger here
@RequestMapping("/api/v1/cases")
public interface CasesControllerSpec {

    @GetMapping("/max-min-stats")
    ResponseEntity<?> findMaxMinCasesByPeriod(
            @RequestParam(name = "countries") List<Long> countries,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    );
}
