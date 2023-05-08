package com.pvsrg.covidapi.service.cases;

import com.pvsrg.covidapi.es.covidapi.dto.CACasesDataDTO;
import com.pvsrg.covidapi.model.entities.CountryEntity;
import com.pvsrg.covidapi.service.common.CovidApiService;
import com.pvsrg.covidapi.service.country.CountryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CasesPullingServiceTest {

    public static final long COUNTRY_ID = 1L;
    public static final LocalDate DATE = LocalDate.now();
    public static final LocalDate DATE3 = DATE.plusDays(4);
    public static final LocalDate DATE2 = DATE.plusDays(3);
    public static final LocalDate DATE1 = DATE.plusDays(1);
    public static final String SLUG = "rus";


    @Mock
    CovidApiService covidApiService;
    @Mock
    CasesService casesService;
    @Mock
    CountryService countryService;

    @InjectMocks
    CasesPullingService sut;

    @Test
    public void testPulling() throws Exception {
        // given
        CountryEntity country = new CountryEntity();
        country.setId(COUNTRY_ID);
        country.setName("Russia");
        country.setSlug(SLUG);
        doReturn(List.of(country)).when(countryService).findAll();


        doReturn(List.of(
                new CACasesDataDTO("COUNTRY_ID", 1, DATE),
                new CACasesDataDTO("COUNTRY_ID", 10, DATE1),
                new CACasesDataDTO("COUNTRY_ID", 8, DATE2), // sometimes API returns amount that less than previous day
                new CACasesDataDTO("COUNTRY_ID", 20, DATE3)
        )).when(covidApiService).fetchCases(any());

        // when
        sut.pullCases();

        // then
        verify(countryService).findAll();
        verify(covidApiService).fetchCases(SLUG);
        verify(casesService).insert(1L, DATE, 1);
        verify(casesService).insert(1L, DATE1, 9);
        verify(casesService).insert(1L, DATE2, 0);
        verify(casesService).insert(1L, DATE3, 12);

        verifyNoInteractions(casesService, covidApiService, countryService);


    }

}