package com.dodonov.oogosu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "emailservice.smtp")
@Getter
@Setter
public class EmailSenderProperties {
    private String smtpServerName;
    private Integer smtpServerPort;
    private String userName;
    private String userPassword;
}
