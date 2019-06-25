package pl.tscript3r.photogram.configs;

class SecurityConstants {

    static final String SECRET = "^[a-zA-Z0-9._]+$\r\nGuidelines89797987forAlphabeticalArraNumeralsandOtherSymbo$";
    static final Long EXPIRATION_TIME = 432_000_000L; // 5 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_TYPE = "Authorization";
    static final String CLIENT_DOMAIN_URL = "*";

    private SecurityConstants() {
    }

}
