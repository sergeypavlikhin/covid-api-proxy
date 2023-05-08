package com.pvsrg.covidapi.web.cases.dto;

import java.time.LocalDate;


public record CasesDataDTO(
        String countryName,
        LocalDate date,
        Integer cases
) {
}
