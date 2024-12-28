package dev.marvin.shared;

public record ResponseDto<T>(String message, T payload) {
}
