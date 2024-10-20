package dev.marvin.service;

import dev.marvin.dto.SmsRequest;

public interface SmsService {
    void sendSms(SmsRequest smsRequest);
}
