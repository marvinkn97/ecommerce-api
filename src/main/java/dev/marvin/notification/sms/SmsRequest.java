package dev.marvin.notification.sms;

public record SmsRequest(String to, String from, String message) {
}
