package com.devscircle.journalApp.service;

import com.devscircle.journalApp.Constants.PlaceHolders;
import com.devscircle.journalApp.api.response.QuoteResponse;
import com.devscircle.journalApp.api.response.WeatherResponse;
import com.devscircle.journalApp.cache.AppCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class QuoteService {

    @Value("${quotes.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public QuoteResponse.Response getQuote(){

        QuoteResponse.Response quoteResponseResponse = redisService.get("quote_of_the_day", QuoteResponse.Response.class);
        if(quoteResponseResponse!=null){
            return quoteResponseResponse;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String API = appCache.appCache.get(AppCache.keys.QUOTES_API.toString());

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            API,
                            HttpMethod.GET,
                            entity,
                            String.class
                    );
            String responseBody = response.getBody();

            ObjectMapper mapper = new ObjectMapper();

            List<QuoteResponse.Response> quotes = mapper.readValue(
                    responseBody,
                    new TypeReference<List<QuoteResponse.Response>>() {}
            );

            QuoteResponse.Response response1 = quotes.get(0);
            if(response1!=null){
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime midnight = LocalDateTime.of(
                        now.toLocalDate().plusDays(1),
                        LocalTime.MIDNIGHT
                );

                long secondsUntilMidnight =
                        Duration.between(now, midnight).getSeconds();

                redisService.set(
                        "quote_of_the_day",
                        response1,
                        secondsUntilMidnight
                );
            }
            return response1;


        } catch (RestClientException e) {
            log.error("Exception while fetching quote", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
