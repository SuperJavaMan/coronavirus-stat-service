package com.example.coronavirus.controller;

import com.example.coronavirus.controller.exception.BadRequestException;
import com.example.coronavirus.controller.exception.DataNotFoundException;
import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.service.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 01.04.2020
 * cpabox777@gmail.com
 */
@RestController
@RequestMapping("/history")
@Slf4j
public class HistoryStatController {

    private final DataProvider dataProvider;

    @Autowired
    public HistoryStatController(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @GetMapping("/world/{date}")
    public List<DailyStatistic> getWorldStatByDate(@PathVariable String date) {
        log.debug("Input date = " + date);
        LocalDate reqDate = LocalDate.parse(date);
        if (reqDate.isAfter(LocalDate.now())) {
            String msg = "Invalid date = " + date + ". More then current date.";
            log.debug(msg);
            throw new BadRequestException(msg);
        }

        List<DailyStatistic> worldStatList;
        try {
            worldStatList = dataProvider.getWorldStatByDate(reqDate);
        } catch (NoDataException e) {
            String msg = "No worldStat data for " + date;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return worldStatList;
    }

    @GetMapping("/world/{from}/{to}")
    public List<DailyStatistic> getWorldStatFromToDate(@PathVariable String from,
                                                                       @PathVariable String to) {
        log.debug("Input date range -> " + from + " <-> " + to);

        LocalDate reqFrom = LocalDate.parse(from);
        LocalDate reqTo = LocalDate.parse(to);
        if (reqFrom.isAfter(reqTo) || reqTo.isAfter(LocalDate.now())) {
            String msg = "Invalid date range -> " + reqFrom + " <-> " + reqTo;
            log.debug(msg);
            throw new BadRequestException(msg);
        }

        List<DailyStatistic> worldStatList;
        try {
            worldStatList = dataProvider.getWorldStatFromToDate(LocalDate.parse(from), LocalDate.parse(to));
        } catch (NoDataException e) {
            String msg = "No worldStat data for range = " + reqFrom + " <-> " + reqTo;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return worldStatList;
    }

    @GetMapping("/country/{id}/{date}")
    public DailyStatistic getCountryStatByDate(@PathVariable Long id,
                                                                     @PathVariable String date) {
        log.debug("Input countryId = " + id + ", date = " + date);

        LocalDate reqDate = LocalDate.parse(date);
        if (reqDate.isAfter(LocalDate.now())) {
            String msg = "Invalid date -> " + reqDate;
            log.debug(msg);
            throw new BadRequestException(msg);
        }

        DailyStatistic countryStat;
        try {
            countryStat = dataProvider.getCountryStatByDate(id, LocalDate.parse(date));
        } catch (NoDataException e) {
            String msg = "No worldStat data for countryId = " + id + ", date = " + reqDate;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return countryStat;
    }

    @GetMapping("/country/{id}/{from}/{to}")
    public List<DailyStatistic> getCountryStatFromToDate(@PathVariable Long id,
                                                                         @PathVariable String from,
                                                                         @PathVariable String to) {
        log.debug("Input countryId = " + id + ", date range -> " + from + " <-> " + to);

        LocalDate reqFrom = LocalDate.parse(from);
        LocalDate reqTo = LocalDate.parse(to);
        if (reqFrom.isAfter(reqTo) || reqTo.isAfter(LocalDate.now())) {
            String msg = "Invalid date range = " + reqFrom + " <-> " + reqTo;
            log.debug(msg);
            throw new BadRequestException(msg);
        }

        List<DailyStatistic> countryStatList;
        try {
            countryStatList = dataProvider.getCountryStatFromToDate(id, LocalDate.parse(from), LocalDate.parse(to));
        } catch (NoDataException e) {
            String msg = "No data for countryId = " + id + ", with date range = " + reqFrom + " <-> " + reqTo;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return countryStatList;
    }

    @GetMapping("/country/{id}")
    public List<DailyStatistic> getAllCountryStat(@PathVariable Long id) {
        List<DailyStatistic> countryStatList;
        try {
            countryStatList = dataProvider.getAllCountryStat(id);
        } catch (NoDataException e) {
            String msg = "No data for countryId = " + id;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return countryStatList;
    }

    @GetMapping("/global")
    public DailyStatistic getGlobalStat() {
        DailyStatistic ds;
        try {
            ds = dataProvider.getGlobalStatistic();
        } catch (NoDataException e) {
            String msg = "No data for global statistic";
            log.error(msg, e);
            throw new DataNotFoundException(msg);
        }
        return ds;
    }
}
