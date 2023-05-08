package com.pvsrg.covidapi.model.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CaseDataEntity extends BasicEntity {
    private LocalDate date;
    private Long countryId;
    private Integer cases;
}
