package com.pvsrg.covidapi.service.scheduling;

import com.pvsrg.covidapi.service.cases.CasesPullingService;
import com.pvsrg.covidapi.service.country.CountryPullingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final CasesPullingService casesPullingService;
    private final CountryPullingService countryPullingService;

    @Scheduled(cron = "${app.pulling-cron}")
    public void pullEverything() {
        countryPullingService.pullCountries();
        casesPullingService.pullCases();
    }
}
