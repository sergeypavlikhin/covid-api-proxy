package com.pvsrg.covidapi.es.covidapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CACountryDTO(
        @JsonProperty("Country")
        String name,
        @JsonProperty("Slug")
        String slug
) {
}
