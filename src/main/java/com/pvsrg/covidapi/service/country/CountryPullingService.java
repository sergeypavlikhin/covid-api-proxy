package com.pvsrg.covidapi.service.country;

import com.pvsrg.covidapi.es.covidapi.exception.CAException;
import com.pvsrg.covidapi.service.common.CovidApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryPullingService {

    private final ReentrantLock lock = new ReentrantLock();

    private final CovidApiService covidApiService;
    private final CountryService countryService;

    public void pullCountries() {
        if (lock.tryLock()) {
            try {
                var countriesResponseData = covidApiService.fetchCountries();
                countriesResponseData.forEach(it -> countryService.saveCountry(it.name(), it.slug()));
            } catch (CAException e) {
                log.error("Couldn't fetch countries from external source", e);
            } catch (Exception e) {
                log.error("Couldn't pull countries", e);
            } finally {
                lock.unlock();
            }
        } else {
            log.info("Pulling in progress, try again later");
        }

    }
}
