package com.example.coronavirus.dataParser.johnHopkins;

import com.example.coronavirus.dataParser.exception.ResourceNotAvailableException;
import com.example.coronavirus.dataParser.model.johnHopkins.JohnHopkinsDto;
import com.example.coronavirus.model.DailyStatistic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
public class JohnHopkinsApiDS extends AbstractJohnHopkinsDS {
    private static final String URL = "https://corona.lmao.ninja/v2/jhucsse";
    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        List<JohnHopkinsDto> johnHopkinsDtoList = new ArrayList<>();
        try {
            johnHopkinsDtoList = Arrays.asList(new ObjectMapper().readValue(doRequest(URL).getBody(), JohnHopkinsDto[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(johnHopkinsDtoList.stream()
                .map(this::convertToDailyStatistic)
                .collect(Collectors.toMap(DailyStatistic::getCountry, dailyStatistic -> dailyStatistic,
                        (oldDailyStat, newDailyStat) -> {
                            oldDailyStat.setCases(newDailyStat.getCases() + oldDailyStat.getCases());
                            oldDailyStat.setRecovered(newDailyStat.getRecovered() + oldDailyStat.getRecovered());
                            oldDailyStat.setDeaths(newDailyStat.getDeaths() + oldDailyStat.getDeaths());
                            return oldDailyStat;
                        })).values());
    }

    @Override
    public List<DailyStatistic> getStatsByAllCountries() throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("JohnHopkins is not supporting this API");
    }

    @Override
    public List<DailyStatistic> getStatsByCountry(String country) throws ResourceNotAvailableException {
        throw new ResourceNotAvailableException("JohnHopkins is not supporting this API");
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
            throw new ResourceNotAvailableException("JohnHopkins API is not available", e);
        }
        return responseEntity;
    }
}
