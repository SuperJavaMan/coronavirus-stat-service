package com.example.coronavirus.foreignDataSource.lmao;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import com.example.coronavirus.foreignDataSource.lmao.model.johnHopkins.JohnHopkinsDto;
import com.example.coronavirus.model.DailyStatistic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 30.03.2020
 * cpabox777@gmail.com
 */
@Slf4j
public class JohnHopkinsApiDS extends AbstractJohnHopkinsDS {
    private static final String URL = "https://corona.lmao.ninja/v2/jhucsse";
    @Override
    public List<DailyStatistic> getCurrentDayWorldStat() throws ResourceNotAvailableException {
        LocalDate jhCurrentDate = LocalDate.now().minusDays(1); // последним днем считается предидущее число
        List<JohnHopkinsDto> johnHopkinsDtoList;
        try {
            johnHopkinsDtoList = Arrays.asList(new ObjectMapper().readValue(Objects.requireNonNull(doRequest().getBody()),
                                                                            JohnHopkinsDto[].class));
        } catch (JsonProcessingException e) {
            log.error("Error during processing Lmao response for JH data", e);
            throw new ResourceNotAvailableException("Response of Lmao \"JH\" parse error", e);
        }

        return new ArrayList<>(johnHopkinsDtoList.stream()
                .map(this::convertToDailyStatistic)
                .collect(Collectors.toMap(DailyStatistic::getCountry, dailyStatistic -> dailyStatistic,
                        (oldDailyStat, newDailyStat) -> {
                            oldDailyStat.setDate(jhCurrentDate);
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

    private ResponseEntity<String> doRequest() throws ResourceNotAvailableException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(JohnHopkinsApiDS.URL, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            log.error("Resource Lmao URL=" + JohnHopkinsApiDS.URL + " is not available");
            throw new ResourceNotAvailableException("JohnHopkins API is not available", e);
        }
        return responseEntity;
    }
}
