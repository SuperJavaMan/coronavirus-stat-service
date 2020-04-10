package com.example.coronavirus.foreignDataSource.twoGis;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.foreignDataSource.twoGis.model.TwoGisDto;
import com.example.coronavirus.model.DailyStatistic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Oleg Pavlyukov
 * on 10.04.2020
 * cpabox777@gmail.com
 */
@Slf4j
public class TwoGisDS extends AbstractTwoGis {
    private static final String URL = "https://covid.2gis.ru/covid19-global.json";

    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        String json = doRequest(URL).getBody();
        TwoGisDto dto;
        try {
            dto = new ObjectMapper().readValue(Objects.requireNonNull(json), TwoGisDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error during processing TwoGisDS response ", e);
            throw new ResourceNotAvailableException(e);
        }
        return convertToDailyStatisticList(dto);
    }

    @Override
    public List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("Not supported");
    }

    @Override
    public List<DailyStatistic> getStatsByCountry(String country) throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("Not supported");
    }

    private ResponseEntity<String> doRequest(String url) throws ResourceNotAvailableException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            log.error("Resource 2GisDs URL=" + url + " is not available");
            throw new ResourceNotAvailableException("2GisDs is not available", e);
        }
        return responseEntity;
    }
}
