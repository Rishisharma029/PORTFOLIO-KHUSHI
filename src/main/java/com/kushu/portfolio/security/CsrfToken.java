package com.kushu.portfolio.security;

import java.io.Serializable;

/**
 * Immutable CSRF token representation holding token value, parameter name, and header name.
 */
public class CsrfToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String token;
    private final String parameterName;
    private final String headerName;

    public CsrfToken(String token, String parameterName, String headerName) {
        this.token = token;
        this.parameterName = parameterName;
        this.headerName = headerName;
    }

    public String getToken() {
        return token;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
