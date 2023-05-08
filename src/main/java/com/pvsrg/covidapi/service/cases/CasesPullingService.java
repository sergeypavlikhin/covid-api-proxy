package com.pvsrg.covidapi.service.cases;

import com.pvsrg.covidapi.es.covidapi.CovidAPIClient;
import com.pvsrg.covidapi.es.covidapi.dto.CACasesDataDTO;
import com.pvsrg.covidapi.es.covidapi.exception.CAException;
import com.pvsrg.covidapi.es.covidapi.exception.CATooManyRequestsException;
import com.pvsrg.covidapi.model.entities.CountryEntity;
import com.pvsrg.covidapi.properties.AppProperties;
import com.pvsrg.covidapi.service.country.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CasesPullingService {
    private final AppProperties appProperties;
    private final CovidAPIClient apiClient;
    private final CasesService casesService;
    private final CountryService countryService;

    public void pullCases() {
        log.info("Begin pulling cases...");
        for (CountryEntity country : countryService.findAll()) {

            try {
                log.info("Try to pull cases for {}", country.getName());
                var casesData = getForCountry(country.getSlug())
                        .stream()
                        .sorted(Comparator.comparing(CACasesDataDTO::date))
                        .toList();

                for (int i = 0; i < casesData.size(); i++) {

                    var it = casesData.get(i);

                    // API doesn't provide exactly number of new cases, but provides total amount of cases per day.
                    // So we can calculate new cases like: current day cases - previous day cases.
                    // Don't forget that first element in list - first day of COVID pandemic
                    var newCases = i == 0 ? it.cases() : it.cases() - casesData.get(i - 1).cases();

                    casesService.insert(country.getId(), it.date(), Math.max(0, newCases));
                }

                log.info("Finished pulling cases for {}", country.getName());
            } catch (CAException e) {
                log.error("Couldn't fetch cases from external source", e);
            } catch (Exception e) {
                log.error("Couldn't pull countries", e);
            }
        }

        log.info("Finished pulling cases...");
    }

    private List<CACasesDataDTO> getForCountry(String countrySlug) throws CAException {
        // before each request make some delay
        safeSleepMs(appProperties.requestDelayMs());

        long nextSleep = appProperties.coolingTimeSec();
        while (nextSleep <= appProperties.coolingTimeMax()) {
            try {
                return apiClient.fetchCases(countrySlug, null, null);
            } catch (CATooManyRequestsException e) {
                log.error("Too many requests for {}", countrySlug);
                safeSleepMs(TimeUnit.SECONDS.toMillis(nextSleep));
                nextSleep = Math.round(nextSleep * appProperties.coolingTimeMultiplier());
                continue;
            }
        }

        throw new RuntimeException("Couldn't pull data for country");
    }

    private void safeSleepMs(long timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException ex) {
            log.error("Couldn't sleep", ex);
        }
    }
}
