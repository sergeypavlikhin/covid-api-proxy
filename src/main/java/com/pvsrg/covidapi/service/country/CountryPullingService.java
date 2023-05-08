package com.pvsrg.covidapi.service.country;

import com.pvsrg.covidapi.es.covidapi.exception.CAException;
import com.pvsrg.covidapi.service.common.CovidApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryPullingService {

    private final CovidApiService covidApiService;
    private final CountryService countryService;

    public void pullCountries() {
        try {
            var countriesResponseData = covidApiService.fetchCountries();
            countriesResponseData.forEach(it -> countryService.saveCountry(it.name(), it.slug()));
        } catch (CAException e) {
            log.error("Couldn't fetch countries from external source", e);
        } catch (Exception e) {
            log.error("Couldn't pull countries", e);
        }
    }
}
