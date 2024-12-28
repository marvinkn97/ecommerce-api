package dev.marvin.notification.sms;

import dev.marvin.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TiaraSmsServiceImpl implements SmsService {
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${sms.api.endpoint}")
    String apiEndpoint;

    @Value("${sms.api.key}")
    String apiKey;

    @Override
    public void sendSms(SmsRequest smsRequest) {
        log.info("Inside sendSms method of TiaraConnectSmsServiceImpl");
        try {
            RestTemplate restTemplate = restTemplateBuilder
                    .setConnectTimeout(Duration.ofSeconds(10))
                    .setReadTimeout(Duration.ofSeconds(10))
                    .build();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(apiKey));

            RequestEntity<SmsRequest> requestEntity = RequestEntity.post(apiEndpoint).headers(httpHeaders).body(smsRequest);

            ResponseEntity<Map> response = restTemplate.exchange(requestEntity, Map.class);
            log.debug("Response: {}", response.getBody());
        } catch (Exception e) {
            log.error(MessageConstants.UNEXPECTED_ERROR, e.getMessage(), e);
        }
    }
}
