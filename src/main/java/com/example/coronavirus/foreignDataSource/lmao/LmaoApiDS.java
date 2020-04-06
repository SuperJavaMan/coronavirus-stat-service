package com.example.coronavirus.foreignDataSource.lmao;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.foreignDataSource.model.lmao.LmaoDto;
import com.example.coronavirus.model.DailyStatistic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 29.03.2020
 * cpabox777@gmail.com
 */
@Slf4j
public class LmaoApiDS extends AbstractLmaoDS {
    private static final String URL = "https://corona.lmao.ninja/v2";

    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("Lmao api not supported");
    }

    @Override
    public List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException {
        String json = doRequest(URL + "/historical").getBody();
        List<LmaoDto> lmaoDtoList;
        try {
            lmaoDtoList = Arrays.asList(new ObjectMapper().readValue(json, LmaoDto[].class));
        } catch (JsonProcessingException e) {
            log.error("Error during processing Lmao response ", e);
            throw new ResourceNotAvailableException(e);
        }
        return new ArrayList<>(lmaoDtoList.stream().flatMap(lmaoDto -> convertToDailyStatistic(lmaoDto).stream())
                .collect(Collectors.toMap(
                        dailyStatistic -> dailyStatistic.getCountry().getName() + dailyStatistic.getDate(),
                        dailyStatistic -> dailyStatistic,
                        (oldDailyStat, newDailyStat) -> {
                            oldDailyStat.setCases(newDailyStat.getCases() + oldDailyStat.getCases());
                            oldDailyStat.setRecovered(newDailyStat.getRecovered() + oldDailyStat.getRecovered());
                            oldDailyStat.setDeaths(newDailyStat.getDeaths() + oldDailyStat.getDeaths());
                            return oldDailyStat;
                    })).values());
    }

    @Override
    public List<DailyStatistic> getStatsByCountry(String country) throws ResourceNotAvailableException {
        String json = doRequest(URL + "/historical/" + country).getBody();
        LmaoDto lmaoDtoList = null;
        try {
            lmaoDtoList = new ObjectMapper().readValue(Objects.requireNonNull(json), LmaoDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return convertToDailyStatistic(Objects.requireNonNull(lmaoDtoList));
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
            log.error("Resource Lmao URL=" + url + " is not available");
            throw new ResourceNotAvailableException("LmaoApiDS is not available", e);
        }
        return responseEntity;
    }
}
