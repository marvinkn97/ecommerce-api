package dev.marvin.service;

import dev.marvin.dto.SmsRequest;

public interface ISmsService {
    void sendSms(SmsRequest smsRequest);
}
