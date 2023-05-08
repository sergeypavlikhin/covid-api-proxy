package com.pvsrg.covidapi.service.common;

import com.pvsrg.covidapi.es.covidapi.CovidAPIClient;
import com.pvsrg.covidapi.es.covidapi.dto.CACasesDataDTO;
import com.pvsrg.covidapi.es.covidapi.dto.CACountryDTO;
import com.pvsrg.covidapi.es.covidapi.exception.CATooManyRequestsException;
import com.pvsrg.covidapi.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CovidApiService {

    private final CovidAPIClient apiClient;
    private final AppProperties appProperties;

    public List<CACountryDTO> fetchCountries() throws Exception {
        return wrapApiCalling(apiClient::fetchCountries);
    }

    public List<CACasesDataDTO> fetchCases(String countrySlug) throws Exception {
        return wrapApiCalling(() -> apiClient.fetchCases(countrySlug));
    }

    /**
     * Wrap method execution with logic how to deal with service limits
     *
     * @param callable method execution
     * @param <T>      result type
     * @return T
     * @throws Exception
     */
    protected <T> T wrapApiCalling(Callable<T> callable) throws Exception {
        // before each request make some delay
        safeSleepMs(appProperties.requestDelayMs());

        long nextSleep = appProperties.coolingTimeSec();
        while (nextSleep <= appProperties.coolingTimeMax()) {
            try {
                return callable.call();
            } catch (CATooManyRequestsException e) {
                log.error("Too many requests");
                safeSleepMs(TimeUnit.SECONDS.toMillis(nextSleep));
                nextSleep = Math.round(nextSleep * appProperties.coolingTimeMultiplier());
            } catch (Exception e) {
                throw e;
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
