package dev.marvin.domain;

public enum Status {
    Active,
    Inactive,
    PENDING_OTP_VERIFICATION,  // Initial state when OTP is sent
    OTP_VERIFIED,  // After the OTP has been successfully verified
    PASSWORD_CREATED,  // After the password is created
    PROFILE_COMPLETED,  // After the user fills out profile info
    REGISTERED  // After all steps of registration are complete
}
