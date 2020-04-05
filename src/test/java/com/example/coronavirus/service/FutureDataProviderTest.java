package com.example.coronavirus.service;

import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleg Pavlyukov
 * on 03.04.2020
 * cpabox777@gmail.com
 */
@SpringBootTest
class FutureDataProviderTest {

    @Autowired
    FutureDataProvider futureDataProvider;
    @Autowired
    CountryRepository countryRepository;

    @Test
    void getForecastByPreviousDays() throws NoDataException {
        int dayCount = 5;
        Country russia = countryRepository.findCountryByName("USA");
        List<DailyStatistic> resultList = futureDataProvider.getForecastByPreviousDays(russia.getId(), dayCount);

        assertNotNull(resultList);
        assertEquals(dayCount, resultList.size());
    }
}
