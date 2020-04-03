package com.example.coronavirus.service;

import com.example.coronavirus.dataParser.ForeignDataSource;
import com.example.coronavirus.dataParser.exception.ResourceNotAvailableException;
import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.repository.CountryRepository;
import com.example.coronavirus.repository.DailyStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
@Service
public class DataProvider {

    private final ForeignDataSource foreignDataSource;
    private final DailyStatRepository repository;
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    public DataProvider(ForeignDataSource foreignDataSource, DailyStatRepository repository) {
        this.foreignDataSource = foreignDataSource;
        this.repository = repository;
    }

    public List<DailyStatistic> getWorldStatByDate(LocalDate date) throws NoDataException {
        List<DailyStatistic> dailyStatisticList = repository.findAllByDate(date);
        if (dailyStatisticList == null && date.isEqual(LocalDate.now())) {
            try {
                dailyStatisticList = foreignDataSource.getCurrentDayWorldStat();
            } catch (ResourceNotAvailableException e) {
                throw new NoDataException("No data available for " + date, e);
            }
            repository.saveAll(dailyStatisticList);
        }
        return dailyStatisticList;
    }

    public List<DailyStatistic> getWorldStatFromToDate(LocalDate from, LocalDate to) throws NoDataException {
        if (from.isAfter(to) || to.isAfter(LocalDate.now())) {
            throw new NoDataException("Invalid input or date is after today");
        }
        List<DailyStatistic> dailyStatisticList = repository.findAllByDateIsBetween(from, to);
        if (dailyStatisticList == null || dailyStatisticList.isEmpty())
            throw new NoDataException("No data No data available for date from " + from + " to " + to);
        return dailyStatisticList;
    }

    public DailyStatistic getCountryStatByDate(Long countryId, LocalDate date) throws NoDataException {
        Country country = countryRepository.findById(countryId).orElseThrow(NoDataException::new);
        DailyStatistic dailyStatistic = repository.findByDateAndCountry(date, country);
        if (dailyStatistic == null) {
            try {
                List<DailyStatistic> dailyStatisticList = foreignDataSource.getStatsByCountry(country.getName());
                dailyStatistic = dailyStatisticList.stream()
                        .filter(ds -> ds.getCountry().getName().equals(country.getName()))
                        .filter(ds -> ds.getDate().isEqual(date))
                        .findFirst().orElseThrow(NoDataException::new);
                repository.save(dailyStatistic);
            } catch (ResourceNotAvailableException | NoDataException e) {
                throw new NoDataException("No data available for " + country.getName() + " for " + date, e);
            }
        }
        return dailyStatistic;
    }

    public List<DailyStatistic> getCountryStatFromToDate(Long countryId,
                                                         LocalDate from,
                                                         LocalDate to) throws NoDataException {
        Country country = countryRepository.findById(countryId).orElseThrow(NoDataException::new);
        List<DailyStatistic> dailyStatisticList = repository.findAllByCountryAndDateBetween(country, from, to);
        if (dailyStatisticList == null || dailyStatisticList.isEmpty())
            throw new NoDataException("No data available for " + country.getName() + " from " + from + " to" + to);
        return dailyStatisticList;
    }

    public List<DailyStatistic> getAllCountryStat(Long countryId) throws NoDataException {
        Country country = countryRepository.findById(countryId).orElseThrow(NoDataException::new);
        List<DailyStatistic> dailyStatisticList = repository.findAllByCountry(country);
        if (dailyStatisticList == null || dailyStatisticList.isEmpty())
            throw new NoDataException("No data available for " + country.getName());
        return dailyStatisticList;
    }
}
