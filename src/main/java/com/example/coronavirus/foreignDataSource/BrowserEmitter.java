package com.example.coronavirus.foreignDataSource;

import com.example.coronavirus.foreignDataSource.exception.ResourceNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
public class BrowserEmitter {

    public static ResponseEntity<String> doRequest(String url) throws ResourceNotAvailableException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            String msg = "Resource " + url + " is not available";
            log.error(msg);
            throw new ResourceNotAvailableException(msg, e);
        }
        return responseEntity;
    }

    public static ResponseEntity<String> doRequest(String url, HttpHeaders httpHeaders) throws ResourceNotAvailableException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            String msg = "Resource " + url + " is not available";
            log.error(msg);
            throw new ResourceNotAvailableException(msg, e);
        }

        return responseEntity;
    }
}
