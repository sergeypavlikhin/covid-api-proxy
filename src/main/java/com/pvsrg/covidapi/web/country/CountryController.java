package com.pvsrg.covidapi.web.country;

import com.pvsrg.covidapi.service.country.CountryService;
import com.pvsrg.covidapi.web.country.dto.CountryDTO;
import com.pvsrg.covidapi.web.country.mapper.CountryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CountryController implements CountryControllerSpec {

    private final CountryMapper countryMapper;
    private final CountryService countryService;

    @Override
    public ResponseEntity<List<CountryDTO>> findAll() {
        var countriesList = countryService.findAll()
                .stream().map(this.countryMapper::entityToDTO)
                .toList();
        return ResponseEntity.ok(countriesList);
    }
}
