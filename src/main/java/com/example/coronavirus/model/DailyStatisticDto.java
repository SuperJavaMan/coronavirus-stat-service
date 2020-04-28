package com.example.coronavirus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Oleg Pavlyukov
 * on 12.04.2020
 * cpabox777@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatisticDto {

    private Long id;
    private long date;
    private Country country;
    private int cases;
    private int deaths;
    private int recovered;
    private int tested;

    public LocalDate retrieveDateInLocalDate() {
        return Instant.ofEpochSecond(this.date).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setDateInUnixSec(LocalDate date) {
        this.date = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    public static DailyStatistic convertToDailyStatistic(DailyStatisticDto dto) {
        DailyStatistic ds = new DailyStatistic();
        ds.setId(dto.getId());
        ds.setDate(dto.retrieveDateInLocalDate());
        ds.setCountry(dto.getCountry());
        ds.setCases(dto.getCases());
        ds.setRecovered(dto.getRecovered());
        ds.setDeaths(dto.getDeaths());
        ds.setTested(dto.getTested());
        return ds;
    }

    public static DailyStatisticDto convertToDto(DailyStatistic ds) {
        DailyStatisticDto dto = new DailyStatisticDto();
        dto.setId(ds.getId());
        dto.setDateInUnixSec(ds.getDate());
        dto.setCountry(ds.getCountry());
        dto.setCases(ds.getCases());
        dto.setRecovered(ds.getRecovered());
        dto.setDeaths(ds.getDeaths());
        dto.setTested(ds.getTested());
        return dto;
    }
}
