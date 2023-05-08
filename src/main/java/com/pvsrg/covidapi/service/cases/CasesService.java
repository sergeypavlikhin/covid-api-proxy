package com.pvsrg.covidapi.service.cases;

import com.pvsrg.covidapi.model.entities.MaxMinCasesVO;
import com.pvsrg.covidapi.service.DaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CasesService {

    private final DaoService daoService;

    public MaxMinCasesVO findMaxMin(List<Long> countryIds, LocalDate from, LocalDate to) {
        return daoService.findCases(countryIds, from, to);
    }

    public void insert(Long countryId, LocalDate date, Integer newCases) {
        daoService.insertCase(countryId, date, newCases);
    }
}
