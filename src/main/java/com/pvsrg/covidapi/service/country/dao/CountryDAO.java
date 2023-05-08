package com.pvsrg.covidapi.service.country.dao;

import com.pvsrg.covidapi.model.entities.CountryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CountryDAO {

    private static final String QUERY_INSERT_COUNTRY = "INSERT INTO countries (name, slug) VALUES (:name, :slug) ON CONFLICT (slug) DO NOTHING";
    private static final String QUERY_SELECT_COUNTRIES = "SELECT id, name, slug FROM countries";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public int insertCountry(String name, String slug) {
        var parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("slug", slug);
        return jdbcTemplate.update(QUERY_INSERT_COUNTRY, parameters);
    }

    public List<CountryEntity> findAllCountries() {
        return jdbcTemplate.query(QUERY_SELECT_COUNTRIES, new MapSqlParameterSource(), ((rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var slug = rs.getString("slug");

            var countryEntity = new CountryEntity();
            countryEntity.setId(id);
            countryEntity.setName(name);
            countryEntity.setSlug(slug);

            return countryEntity;
        }));
    }

}
