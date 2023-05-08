package com.pvsrg.covidapi.es.covidapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvsrg.covidapi.es.covidapi.dto.CACasesDataDTO;
import com.pvsrg.covidapi.es.covidapi.dto.CACountryDTO;
import com.pvsrg.covidapi.es.covidapi.exception.CAException;
import com.pvsrg.covidapi.es.covidapi.exception.CATooManyRequestsException;
import com.pvsrg.covidapi.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CovidAPIClientImpl implements CovidAPIClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;

    @Override
    public List<CACountryDTO> fetchCountries() throws CAException {
        try {
            var url = "%s/countries".formatted(appProperties.externalSourceURL());

            ResponseEntity<String> response = this.callAPI(url);

            if (response.getStatusCode().is2xxSuccessful()) {
                return objectMapper.readValue(response.getBody(), new TypeReference<List<CACountryDTO>>() {
                });
            } else {
                log.error("External source responsed with error: {}, {}", response.getStatusCode(), response.getBody());
            }

        } catch (Exception e) {
            throw new CAException(e);
        }

        return Collections.emptyList();
    }

    @Override
    public List<CACasesDataDTO> fetchCases(String countrySlug, LocalDate from, LocalDate to) throws CAException {
        try {
            var url = "%s/total/dayone/country/%s/status/confirmed".formatted(appProperties.externalSourceURL(), countrySlug);

            ResponseEntity<String> response = this.callAPI(url);

            if (response.getStatusCode().is2xxSuccessful()) {
                return objectMapper.readValue(response.getBody(), new TypeReference<List<CACasesDataDTO>>() {
                });
            } else if (response.getStatusCode().value() == 429) {
                throw new CATooManyRequestsException();
            } else {
                log.error("External source responsed with error: {}, {}", response.getStatusCode(), response.getBody());
            }

        } catch (CAException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                throw new CATooManyRequestsException();
            }
            throw new CAException(e);
        }  catch (Exception e) {
            throw new CAException(e);
        }

        return Collections.emptyList();
    }

    private ResponseEntity<String> callAPI(String url) {
        return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }

}
