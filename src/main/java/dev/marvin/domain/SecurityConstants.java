package dev.marvin.domain;

import java.util.Date;

public class SecurityConstants {
    public static final String TOKEN_ISSUER = "dev.marvin";
    public static final Date  ISSUED_AT = new Date();
    public static final Date EXPIRES_AT = new Date(ISSUED_AT.getTime() + (1000 * 60 * 60));
}
