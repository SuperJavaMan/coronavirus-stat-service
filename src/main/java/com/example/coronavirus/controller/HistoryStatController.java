package com.example.coronavirus.controller;

import com.example.coronavirus.exception.NoDataException;
import com.example.coronavirus.model.DailyStatistic;
import com.example.coronavirus.service.DataProvider;
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
public class HistoryStatController {

    private final DataProvider dataProvider;

    @Autowired
    public HistoryStatController(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @GetMapping("/world/{date}")
    public ResponseEntity<List<DailyStatistic>> getWorldStatByDate(@PathVariable String date) {
        List<DailyStatistic> worldStatList;
        try {
            worldStatList = dataProvider.getWorldStatByDate(LocalDate.parse(date));
        } catch (NoDataException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(worldStatList);
    }

    @GetMapping("/world/{from}/{to}")
    public ResponseEntity<List<DailyStatistic>> getWorldStatFromToDate(@PathVariable String from,
                                                                       @PathVariable String to) {
        List<DailyStatistic> worldStatList;
        try {
            worldStatList = dataProvider.getWorldStatFromToDate(LocalDate.parse(from), LocalDate.parse(to));
        } catch (NoDataException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(worldStatList);
    }

    @GetMapping("/country/{id}/{date}")
    public ResponseEntity<DailyStatistic> getCountryStatByDate(@PathVariable Long id,
                                                                     @PathVariable String date) {
        DailyStatistic countryStat;
        try {
            countryStat = dataProvider.getCountryStatByDate(id, LocalDate.parse(date));
        } catch (NoDataException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(countryStat);
    }

    @GetMapping("/country/{id}/{from}/{to}")
    public ResponseEntity<List<DailyStatistic>> getCountryStatFromToDate(@PathVariable Long id,
                                                                         @PathVariable String from,
                                                                         @PathVariable String to) {
        List<DailyStatistic> countryStatList;
        try {
            countryStatList = dataProvider.getCountryStatFromToDate(id, LocalDate.parse(from), LocalDate.parse(to));
        } catch (NoDataException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(countryStatList);
    }

    @GetMapping("/country/{id}")
    public ResponseEntity<List<DailyStatistic>> getAllCountryStat(@PathVariable Long id) {
        List<DailyStatistic> countryStatList;
        try {
            countryStatList = dataProvider.getAllCountryStat(id);
        } catch (NoDataException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(countryStatList);
    }
}
