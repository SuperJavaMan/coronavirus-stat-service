package com.example.coronavirus.service;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.repository.CountryRepository;
import com.example.coronavirus.repository.DailyStatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Oleg Pavlyukov
 * on 31.03.2020
 * cpabox777@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class DataProviderTest {
    @InjectMocks
    DataProvider dataProvider;
    @Mock
    DailyStatRepository dsRepository;
    @Mock
    CountryRepository countryRepository;
    @Mock
    ForeignDataSource foreignDataSource;

    @Test
    void getWorldStatByDateRetrieveDataFromRepository() throws ResourceNotAvailableException, NoDataException {
        List<DailyStatistic> worldStat = getTestDsList();
        when(dsRepository.findAllByDate(any(LocalDate.class))).thenReturn(worldStat);

        List<DailyStatistic> resWorldStat = dataProvider.getWorldStatByDate(LocalDate.now());

        assertNotNull(resWorldStat);
        assertIterableEquals(worldStat, resWorldStat);

        verify(dsRepository, times(1)).findAllByDate(any(LocalDate.class));
        verify(foreignDataSource, times(0)).getCurrentDayWorldStat();
        verify(dsRepository, times(0)).saveAll(anyIterable());
    }

    @Test
    void getWorldStatByDateRetrieveDataFromForeignDS() throws NoDataException, ResourceNotAvailableException {
        List<DailyStatistic> worldStat = getTestDsList();

        when(dsRepository.findAllByDate(any(LocalDate.class))).thenReturn(null);
        when(foreignDataSource.getCurrentDayWorldStat()).thenReturn(worldStat);
        when(dsRepository.saveAll(anyIterable())).thenReturn(anyList());

        List<DailyStatistic> resWorldStat = dataProvider.getWorldStatByDate(LocalDate.now());

        assertNotNull(resWorldStat);
        assertIterableEquals(worldStat, resWorldStat);

        verify(dsRepository, times(1)).findAllByDate(any(LocalDate.class));
        verify(foreignDataSource, times(1)).getCurrentDayWorldStat();
        verify(dsRepository, times(1)).saveAll(anyIterable());
    }

    @Test
    void getWorldStatByDateNoData() throws ResourceNotAvailableException {
        when(dsRepository.findAllByDate(any(LocalDate.class))).thenReturn(null);
        when(foreignDataSource.getCurrentDayWorldStat()).thenThrow(ResourceNotAvailableException.class);

        assertThrows(NoDataException.class, () -> dataProvider.getWorldStatByDate(LocalDate.now()));

        verify(dsRepository, times(1)).findAllByDate(any(LocalDate.class));
        verify(foreignDataSource, times(1)).getCurrentDayWorldStat();
        verify(dsRepository, times(0)).saveAll(anyIterable());
    }

    @Test
    void getWorldStatFromToDateOK() throws NoDataException {
        List<DailyStatistic> worldStat = getTestDsList();

        when(dsRepository.findAllByDateIsBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(worldStat);

        List<DailyStatistic> resWorldStat = dataProvider.getWorldStatFromToDate(LocalDate.now(), LocalDate.now());

        assertNotNull(resWorldStat);
        assertIterableEquals(worldStat, resWorldStat);

        verify(dsRepository, times(1))
                .findAllByDateIsBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getWorldStatFromToDateNoData() {
        when(dsRepository.findAllByDateIsBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(null);

        assertThrows(NoDataException.class,
                () -> dataProvider.getWorldStatFromToDate(LocalDate.now(), LocalDate.now()));

        verify(dsRepository, times(1))
                .findAllByDateIsBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getCountryStatByDateFromForeignDS() throws ResourceNotAvailableException, NoDataException {
        List<DailyStatistic> worldStat = getTestDsList();
        String countryName = worldStat.get(0).getCountry().getName();

        when(countryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(worldStat.get(0).getCountry()));
        when(dsRepository.findByDateAndCountry(any(LocalDate.class), any(Country.class))).thenReturn(null);
        when(foreignDataSource.getStatsByCountry(countryName)).thenReturn(worldStat);
        when(dsRepository.save(any(DailyStatistic.class))).thenReturn(null);

        DailyStatistic resCountryStat = dataProvider.getCountryStatByDate(worldStat.get(0).getCountry().getId(),
                                                                            worldStat.get(0).getDate());

        assertNotNull(resCountryStat);
        assertEquals(worldStat.get(0), resCountryStat);

        verify(dsRepository, times(1))
                .findByDateAndCountry(any(LocalDate.class), any(Country.class));
        verify(foreignDataSource, times(1)).getStatsByCountry(countryName);
        verify(dsRepository, times(1)).save(any(DailyStatistic.class));
    }

    @Test
    void getCountryStatFromToDate() throws NoDataException {
        List<DailyStatistic> countryStat = getTestDsList();

        when(countryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(countryStat.get(0).getCountry()));
        when(dsRepository.findAllByCountryAndDateBetween(any(Country.class),
                                                        any(LocalDate.class),
                                                        any(LocalDate.class))).thenReturn(countryStat);

        List<DailyStatistic> resCountryStat = dataProvider.getCountryStatFromToDate(countryStat.get(0).getCountry().getId(),
                                                                                    LocalDate.now(), LocalDate.now());

        assertNotNull(resCountryStat);
        assertIterableEquals(countryStat, resCountryStat);

        verify(dsRepository, times(1))
                .findAllByCountryAndDateBetween(any(Country.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getAllCountryStat() throws NoDataException {
        List<DailyStatistic> countryStat = getTestDsList();
        Country country = countryStat.get(0).getCountry();

        when(countryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(country));
        when(dsRepository.findAllByCountry(country)).thenReturn(countryStat);

        List<DailyStatistic> resCountryStat = dataProvider.getAllCountryStat(country.getId());

        assertNotNull(resCountryStat);
        assertIterableEquals(countryStat, resCountryStat);

        verify(dsRepository, times(1)).findAllByCountry(country);
    }

    private List<DailyStatistic> getTestDsList() {
        List<DailyStatistic> dailyStatisticList = new ArrayList<>();
        Country russia = new Country(1L, "Russia");
        Country usa = new Country(2L, "USA");
        LocalDate date = LocalDate.of(2020, 2, 10);

        DailyStatistic russiaDs = new DailyStatistic();
        russiaDs.setId(1L);
        russiaDs.setDate(date);
        russiaDs.setCountry(russia);
        russiaDs.setCases(10);
        russiaDs.setDeaths(2);
        russiaDs.setRecovered(2);

        DailyStatistic usaDs = new DailyStatistic();
        usaDs.setId(2L);
        usaDs.setDate(date);
        usaDs.setCountry(usa);
        usaDs.setCases(10);
        usaDs.setDeaths(2);
        usaDs.setRecovered(2);

        dailyStatisticList.add(russiaDs);
        dailyStatisticList.add(usaDs);
        return dailyStatisticList;
    }
}
