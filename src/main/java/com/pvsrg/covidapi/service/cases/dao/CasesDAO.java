package com.pvsrg.covidapi.service.cases.dao;

import com.pvsrg.covidapi.model.vo.MaxMinCasesVO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CasesDAO {
    private static final String QUERY_SELECT_MIN_MAX = """
            SELECT COALESCE(MIN(new_cases), 0) AS min,
                   COALESCE(MAX(new_cases), 0) AS max
            FROM cases_data cd
            JOIN countries c on c.id = cd.country_id
            WHERE c.id IN (:countryIds) AND cd.date BETWEEN :from AND :to
            """;

    private static final String QUERY_INSERT_CASE = "INSERT INTO cases_data (country_id, date, new_cases) VALUES (:country_id, :date, :new_cases) ON CONFLICT (country_id, date) DO NOTHING";

    private final NamedParameterJdbcTemplate jdbcTemplate;

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
