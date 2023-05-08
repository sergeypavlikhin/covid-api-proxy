package com.pvsrg.covidapi.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryEntity extends BasicEntity {
    private String name;
    private String slug;
}
