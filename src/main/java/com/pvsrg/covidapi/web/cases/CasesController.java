package com.pvsrg.covidapi.web.cases;

import com.pvsrg.covidapi.service.cases.CasesService;
import com.pvsrg.covidapi.web.cases.dto.MaxMinCasesResponseDTO;
import com.pvsrg.covidapi.web.cases.mapper.CasesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CasesController implements CasesControllerSpec {

    private final CasesMapper casesMapper;
    private final CasesService casesService;

    @Override
    public ResponseEntity<MaxMinCasesResponseDTO> findMaxMinCasesByPeriod(List<Long> countries, LocalDate fromDate, LocalDate toDate) {
        var resultVO = casesService.findMaxMin(countries, fromDate, toDate);
        return ResponseEntity.ok(this.casesMapper.voToDTO(resultVO));
    }
}
