package com.pvsrg.covidapi.es.covidapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CACasesDataDTO(
        @JsonProperty("Country")
        String country,
        @JsonProperty("Cases")
        Integer cases,
        @JsonProperty("Date")
        LocalDate date
) {
}
