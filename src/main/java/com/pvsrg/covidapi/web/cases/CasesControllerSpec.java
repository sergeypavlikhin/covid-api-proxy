package com.pvsrg.covidapi.web.cases;

import com.pvsrg.covidapi.web.cases.dto.MaxMinCasesResponseDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/cases")
public interface CasesControllerSpec {

    @GetMapping("/max-min-stats")
    ResponseEntity<MaxMinCasesResponseDTO> findMaxMinCasesByPeriod(
            @RequestParam(name = "countries") List<Long> countries,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    );
}