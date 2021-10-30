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
    private String imapServerName;
    private Integer smtpServerPort;
    private Integer imapServerPort;
    private String userName;
    private String userPassword;
}
