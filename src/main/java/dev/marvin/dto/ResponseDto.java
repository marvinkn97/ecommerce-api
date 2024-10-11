package dev.marvin.dto;

public record ResponseDto<T>(String status, T payload) {}
