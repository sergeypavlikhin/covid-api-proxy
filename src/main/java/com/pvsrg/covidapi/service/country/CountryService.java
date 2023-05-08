package com.pvsrg.covidapi.service.country;

import com.pvsrg.covidapi.model.entities.CountryEntity;
import com.pvsrg.covidapi.service.DaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final DaoService daoService;

    public List<CountryEntity> findAll() {
        return daoService.findAllCountries();
    }

    public void saveCountry(String name, String slug) {
        daoService.insertCountry(name, slug);
    }
}
