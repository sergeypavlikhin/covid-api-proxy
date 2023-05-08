package com.pvsrg.covidapi.service.country;

import com.pvsrg.covidapi.model.entities.CountryEntity;
import com.pvsrg.covidapi.service.country.dao.CountryDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryDAO countryDAO;

    public List<CountryEntity> findAll() {
        return countryDAO.findAllCountries();
    }

    public void saveCountry(String name, String slug) {
        countryDAO.insertCountry(name, slug);
    }
}
