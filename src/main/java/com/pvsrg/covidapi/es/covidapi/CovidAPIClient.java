package com.pvsrg.covidapi.es.covidapi;

import com.pvsrg.covidapi.es.covidapi.dto.CACasesDataDTO;
import com.pvsrg.covidapi.es.covidapi.dto.CACountryDTO;
import com.pvsrg.covidapi.es.covidapi.exception.CAException;

import java.time.LocalDate;
import java.util.List;

/**
 * Provide data about cases from external source
 */
public interface CovidAPIClient {

    /**
     * Fetch available countries
     *
     * @return list of available countries, not null
     * @throws CAException if anything went wrong
     */
    List<CACountryDTO> fetchCountries() throws CAException;

    /**
     * Fetch cases by country and date period
     *
     * @param countryId country ID
     * @param from      first date of period
     * @param to        last date of period
     * @return list of cases data per day
     * @throws CAException if anything went wrong
     */
    List<CACasesDataDTO> fetchCases(String countryId, LocalDate from, LocalDate to) throws CAException;
}

