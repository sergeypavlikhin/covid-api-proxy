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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CovidAPIClientImpl implements CovidAPIClient {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    @Override
    public List<CACountryDTO> fetchCountries() throws CAException {
        var url = "%s/countries".formatted(appProperties.externalSourceURL());
        return this.callAPI(url, new ParameterizedTypeReference<List<CACountryDTO>>() {
        });
    }

    @Override
    public List<CACasesDataDTO> fetchCases(String countrySlug) throws CAException {
        var url = "%s/total/dayone/country/%s/status/confirmed".formatted(appProperties.externalSourceURL(), countrySlug);
        return this.callAPI(url, new ParameterizedTypeReference<List<CACasesDataDTO>>() {
        });
    }

    private <T> T callAPI(String url, ParameterizedTypeReference<T> typeReference) throws CAException {
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, null, typeReference);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                throw new CATooManyRequestsException();
            }
            throw new CAException(e);
        }  catch (Exception e) {
            throw new CAException(e);
        }
    }

}
