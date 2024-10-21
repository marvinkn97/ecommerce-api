package dev.marvin.serviceImpl;

import dev.marvin.dto.SmsRequest;
import dev.marvin.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TiaraConnectSmsServiceImpl implements SmsService {
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${sms.api.endpoint}")
    String apiEndpoint;

    @Value("${sms.api.key}")
    String apiKey;

    @Override
    public void sendSms(SmsRequest smsRequest) {
        log.info("Inside sendSms method of TiaraConnectSmsServiceImpl");
        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(apiKey));

        RequestEntity<SmsRequest> requestEntity = RequestEntity.post(apiEndpoint).headers(httpHeaders).body(smsRequest);

        ResponseEntity<Map> response = restTemplate.exchange(requestEntity, Map.class);

        System.out.println(response.getBody());
    }
}
