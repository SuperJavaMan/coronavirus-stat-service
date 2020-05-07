package com.example.coronavirus.controller;

import com.example.coronavirus.controller.exception.BadRequestException;
import com.example.coronavirus.controller.exception.DataNotFoundException;
import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.model.DailyStatisticDto;
import com.example.coronavirus.service.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 01.04.2020
 * cpabox777@gmail.com
 */
@CrossOrigin
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
    public List<DailyStatisticDto> getWorldStatByDate(@PathVariable Long date) {
        log.debug("Input date = " + date);
        LocalDate reqDate = Instant.ofEpochSecond(date).atZone(ZoneId.systemDefault()).toLocalDate();
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
        return worldStatList.stream()
                .map(DailyStatisticDto::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/world/{from}/{to}")
    public List<DailyStatisticDto> getWorldStatFromToDate(@PathVariable Long from,
                                                                       @PathVariable Long to) {
        log.debug("Input date range -> " + from + " <-> " + to);

        LocalDate reqFrom = Instant.ofEpochSecond(from).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate reqTo = Instant.ofEpochSecond(to).atZone(ZoneId.systemDefault()).toLocalDate();
        if (reqFrom.isAfter(reqTo) || reqTo.isAfter(LocalDate.now())) {
            String msg = "Invalid date range -> " + reqFrom + " <-> " + reqTo;
            log.debug(msg);
            throw new BadRequestException(msg);
        }

        List<DailyStatistic> worldStatList;
        try {
            worldStatList = dataProvider.getWorldStatFromToDate(reqFrom, reqTo);
        } catch (NoDataException e) {
            String msg = "No worldStat data for range = " + reqFrom + " <-> " + reqTo;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return worldStatList.stream()
                .map(DailyStatisticDto::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/country/{id}/{date}")
    public DailyStatisticDto getCountryStatByDate(@PathVariable Long id,
                                                    @PathVariable Long date) {
        log.debug("Input countryId = " + id + ", date = " + date);

        LocalDate reqDate = Instant.ofEpochSecond(date).atZone(ZoneId.systemDefault()).toLocalDate();
        if (reqDate.isAfter(LocalDate.now())) {
            String msg = "Invalid date -> " + reqDate;
            log.debug(msg);
            throw new BadRequestException(msg);
        }

        DailyStatistic countryStat;
        try {
            countryStat = dataProvider.getCountryStatByDate(id, reqDate);
        } catch (NoDataException e) {
            String msg = "No worldStat data for countryId = " + id + ", date = " + reqDate;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return DailyStatisticDto.convertToDto(countryStat);
    }

    @GetMapping("/country/{id}/{from}/{to}")
    public List<DailyStatisticDto> getCountryStatFromToDate(@PathVariable Long id,
                                                                         @PathVariable Long from,
                                                                         @PathVariable Long to) {
        log.debug("Input countryId = " + id + ", date range -> " + from + " <-> " + to);

        LocalDate reqFrom = Instant.ofEpochSecond(from).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate reqTo = Instant.ofEpochSecond(to).atZone(ZoneId.systemDefault()).toLocalDate();
        if (reqFrom.isAfter(reqTo) || reqTo.isAfter(LocalDate.now())) {
            String msg = "Invalid date range = " + reqFrom + " <-> " + reqTo;
            log.debug(msg);
            throw new BadRequestException(msg);
        }

        List<DailyStatistic> countryStatList;
        try {
            countryStatList = dataProvider.getCountryStatFromToDate(id, reqFrom, reqTo);
        } catch (NoDataException e) {
            String msg = "No data for countryId = " + id + ", with date range = " + reqFrom + " <-> " + reqTo;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return countryStatList.stream()
                .map(DailyStatisticDto::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/country/{id}")
    public List<DailyStatisticDto> getAllCountryStat(@PathVariable Long id) {
        List<DailyStatistic> countryStatList;
        try {
            countryStatList = dataProvider.getAllCountryStat(id);
        } catch (NoDataException e) {
            String msg = "No data for countryId = " + id;
            log.warn(msg, e);
            throw new DataNotFoundException(msg);
        }
        return countryStatList.stream()
                .map(DailyStatisticDto::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/global")
    public DailyStatisticDto getGlobalStat() {
        DailyStatistic ds;
        try {
            ds = dataProvider.getGlobalStatistic();
        } catch (NoDataException e) {
            String msg = "No data for global statistic";
            log.error(msg, e);
            throw new DataNotFoundException(msg);
        }
        return DailyStatisticDto.convertToDto(ds);
    }
}
