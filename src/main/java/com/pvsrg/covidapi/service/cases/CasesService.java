package com.pvsrg.covidapi.service.cases;

import com.pvsrg.covidapi.model.vo.MaxMinCasesVO;
import com.pvsrg.covidapi.service.cases.dao.CasesDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CasesService {

    private final CasesDAO casesDAO;

    public MaxMinCasesVO findMaxMin(List<Long> countryIds, LocalDate from, LocalDate to) {
        return casesDAO.findCases(countryIds, from, to);
    }

    public void insert(Long countryId, LocalDate date, Integer newCases) {
        casesDAO.insertCase(countryId, date, newCases);
    }
}
