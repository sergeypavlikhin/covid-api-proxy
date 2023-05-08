package com.pvsrg.covidapi.web.country;

import com.pvsrg.covidapi.web.country.dto.CountryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/countries")
public interface CountryControllerSpec {

    @GetMapping("")
    ResponseEntity<List<CountryDTO>> findAll();
}
