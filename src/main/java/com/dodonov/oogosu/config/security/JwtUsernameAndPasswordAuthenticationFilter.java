package com.dodonov.oogosu.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;
    // We use auth manager to validate the user credentials
    private final AuthenticationManager authManager;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;

        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
        // In our case, we use "/auth". So, we need to override the defaults.
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {

            // 1. Get credentials from request
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            // 2. Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), creds.getPassword(), Collections.emptyList());

            // 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Upon successful authentication, generate a token.
    // The 'auth' passed to successfulAuthentication() is the current authenticated user.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Long now = System.currentTimeMillis();
        var expiration = new Date(now + jwtConfig.getExpiration() * 1000);
        String token = Jwts.builder()
                .setSubject(auth.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(expiration)  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
        var resBody = new UsernameAndAuthorities();
        resBody.setToken("Bearer " + token);
        resBody.setExpirationDate(expiration);
        resBody.setAuthorities(auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        resBody.setUsername(auth.getName());
        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
        response.getOutputStream().print(new ObjectMapper().writeValueAsString(resBody));
        response.flushBuffer();
    }

    // A (temporary) class just to represent the user credentials
    private static class UserCredentials {
        private String username, password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private static class UsernameAndAuthorities {
        private String username;
        private List<String> authorities = new ArrayList<>();
        private Date expirationDate;
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Date getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<String> authorities) {
            this.authorities = authorities;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
