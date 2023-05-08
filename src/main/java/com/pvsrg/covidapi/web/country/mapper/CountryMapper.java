package com.pvsrg.covidapi.web.country.mapper;

import com.pvsrg.covidapi.model.entities.CountryEntity;
import com.pvsrg.covidapi.web.country.dto.CountryDTO;
import org.mapstruct.Mapper;

@Mapper
public abstract class CountryMapper {
    public abstract CountryDTO entityToDTO(CountryEntity entity);
}
