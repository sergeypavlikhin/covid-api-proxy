package com.pvsrg.covidapi.service;

import com.pvsrg.covidapi.model.entities.CountryEntity;
import com.pvsrg.covidapi.model.entities.MaxMinCasesVO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DaoService {
    private static final String QUERY_SELECT_MIN_MAX = """
            SELECT COALESCE(MIN(new_cases), 0) AS min,
                   COALESCE(MAX(new_cases), 0) AS max
            FROM cases_data cd
            JOIN countries c on c.id = cd.country_id
            WHERE c.id IN (:countryIds) AND cd.date BETWEEN :from AND :to
            """;

    private static final String QUERY_INSERT_COUNTRY = "INSERT INTO countries (name, slug) VALUES (:name, :slug) ON CONFLICT (slug) DO NOTHING";
    private static final String QUERY_SELECT_COUNTRIES = "SELECT id, name, slug FROM countries";
    private static final String QUERY_INSERT_CASE = "INSERT INTO cases_data (country_id, date, new_cases) VALUES (:country_id, :date, :new_cases) ON CONFLICT (country_id, date) DO NOTHING";

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

    public int insertCase(Long countryId, LocalDate date, Integer newCases) {
        var parameters = new MapSqlParameterSource()
                .addValue("country_id", countryId)
                .addValue("new_cases", newCases)
                .addValue("date", date);
        return jdbcTemplate.update(QUERY_INSERT_CASE, parameters);
    }

    public MaxMinCasesVO findCases(List<Long> countryIds, LocalDate from, LocalDate to) {
        var parameters = new MapSqlParameterSource()
                .addValue("countryIds", countryIds)
                .addValue("from", from)
                .addValue("to", to);

        return jdbcTemplate.queryForObject(QUERY_SELECT_MIN_MAX, parameters, ((rs, rowNum) -> {
            int min = rs.getInt("min");
            int max = rs.getInt("max");
            return new MaxMinCasesVO(min, max);
        }));
    }

}
