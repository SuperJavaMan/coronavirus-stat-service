package com.example.coronavirus.service;

import com.example.coronavirus.foreignDataSource.ForeignDataSource;
import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.Country;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.repository.CountryRepository;
import com.example.coronavirus.repository.DailyStatRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DataProvider {

    private final ForeignDataSource foreignDataSource;
    private final DailyStatRepository repository;
    private final CountryRepository countryRepository;

    @Autowired
    public DataProvider(ForeignDataSource foreignDataSource,
                        DailyStatRepository repository,
                        CountryRepository countryRepository) {
        this.foreignDataSource = foreignDataSource;
        this.repository = repository;
        this.countryRepository = countryRepository;
    }

    public List<DailyStatistic> getWorldStatByDate(LocalDate date) throws NoDataException {
        log.debug("Input = " + date.toString());
        List<DailyStatistic> dailyStatisticList = repository.findAllByDate(date);
        if (dailyStatisticList == null && date.isEqual(LocalDate.now())) {
            log.debug("Repository has no data");
            try {
                dailyStatisticList = foreignDataSource.getCurrentDayWorldStat();
            } catch (ResourceNotAvailableException e) {
                log.error("Foreign DS and repository have no data for " + date, e);
                throw new NoDataException("No data available for " + date, e);
            }
            repository.saveAll(dailyStatisticList);
        }
        if (dailyStatisticList == null || dailyStatisticList.isEmpty())
            throw new NoDataException("No data available for " + date);
        return dailyStatisticList;
    }

    public List<DailyStatistic> getWorldStatFromToDate(LocalDate from, LocalDate to) throws NoDataException {
        log.debug("Input from = " + from + ", to = " + to);
        if (from.isAfter(to) || to.isAfter(LocalDate.now())) {
            throw new NoDataException("Invalid input");
        }
        List<DailyStatistic> dailyStatisticList = repository.findAllByDateIsBetween(from, to);
        if (dailyStatisticList == null || dailyStatisticList.isEmpty()) {
            log.warn("No data available in repository for a range from = " + from + ", to = " + to);
            throw new NoDataException("No data available for date from " + from + " to " + to);
        }
        return dailyStatisticList;
    }

    public DailyStatistic getCountryStatByDate(Long countryId, LocalDate date) throws NoDataException {
        log.info("Test info");
        log.debug("Input countryId = " + countryId + ", date = " + date);
        if (date.isAfter(LocalDate.now())) throw new NoDataException("Invalid input for date = " + date);

        Country country = countryRepository.findById(countryId).orElseThrow(NoDataException::new);
        DailyStatistic dailyStatistic = repository.findByDateAndCountry(date, country);
        if (dailyStatistic == null) {
            log.debug("No data in repository");
            try {
                List<DailyStatistic> dailyStatisticList = foreignDataSource.getStatsByCountry(country.getName());
                dailyStatistic = dailyStatisticList.stream()
                        .filter(ds -> ds.getCountry().getName().equals(country.getName()))
                        .filter(ds -> ds.getDate().isEqual(date))
                        .findFirst().orElseThrow(NoDataException::new);
                repository.save(dailyStatistic);
            } catch (ResourceNotAvailableException | NoDataException e) {
                log.error("No data available from repository and foreignDS for countryId = " + countryId + ", date = " + date);
                throw new NoDataException("No data available for " + country.getName() + " for " + date, e);
            }
        }
        return dailyStatistic;
    }

    public List<DailyStatistic> getCountryStatFromToDate(Long countryId,
                                                         LocalDate from,
                                                         LocalDate to) throws NoDataException {
        log.debug("Input countryId = " + countryId + ", from = " + from + ", to = " + to);
        if (from.isAfter(to) || to.isAfter(LocalDate.now())) {
            throw new NoDataException("Invalid input");
        }
        Country country = countryRepository.findById(countryId).orElseThrow(NoDataException::new);
        List<DailyStatistic> dailyStatisticList = repository.findAllByCountryAndDateBetween(country, from, to);

        if (dailyStatisticList == null || dailyStatisticList.isEmpty()) {
            log.error("No data in repository for countryId = " + countryId + ", from = " + from + ", to = " + to);
            throw new NoDataException("No data available for " + country.getName() + " from " + from + " to" + to);
        }
        return dailyStatisticList;
    }

    public List<DailyStatistic> getAllCountryStat(Long countryId) throws NoDataException {
        log.debug("Input countryId = " + countryId);
        Country country = countryRepository.findById(countryId).orElseThrow(NoDataException::new);
        List<DailyStatistic> dailyStatisticList = repository.findAllByCountry(country);
        if (dailyStatisticList == null || dailyStatisticList.isEmpty()){
            log.error("No data in repository for countryId = " + countryId);
            throw new NoDataException("No data available for " + country.getName());
        }
        return dailyStatisticList;
    }
}
