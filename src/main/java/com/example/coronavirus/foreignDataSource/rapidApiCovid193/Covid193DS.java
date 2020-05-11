package com.example.coronavirus.foreignDataSource.rapidApiCovid193;

import com.example.coronavirus.foreignDataSource.BrowserEmitter;
import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.foreignDataSource.rapidApiCovid193.model.Covid193Dto;
import com.example.coronavirus.model.DailyStatistic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Covid193DS extends AbstractCovid193 {
    private static final String URL = "https://covid-193.p.rapidapi.com/statistics/";
    private final HttpHeaders headers;

    {
        headers = new HttpHeaders();
        headers.add("x-rapidapi-host", "covid-193.p.rapidapi.com");
        headers.add("x-rapidapi-key", "b940f5216fmsh0c661d3bfe1b130p1fd3d8jsnd28c27f1346e");
    }

    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        String json = BrowserEmitter.doRequest(URL, headers).getBody();

        if (json == null || json.isEmpty()) throw new ResourceNotAvailableException("Response is empty");

        Covid193Dto covid193DSDto;
        try {
            covid193DSDto = new ObjectMapper().readValue(json, Covid193Dto.class);
        } catch (JsonProcessingException e) {
            String msg = "Error parsing json Covid193Dto";
            log.error(msg, e);
            throw new ResourceNotAvailableException(msg, e);
        }
        // некоторые страны не обновляли статистику долгое время.
        // Чтобы предотвратить конфликты при сохранении,
        // сеттим текущую дату вместо даты последнего обновления,
        // как последние данные на текущий момент времени
        return convertToDailyStatistic(covid193DSDto).stream()
                .peek(ds -> ds.setDate(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("Covid193DS not support this api");
    }

    @Override
    public List<DailyStatistic> getStatsByCountry(String country) throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("Covid193DS not support this api");
    }
}
