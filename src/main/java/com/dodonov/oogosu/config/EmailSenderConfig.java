package com.dodonov.oogosu.config;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EmailSenderConfig {

    private final EmailSenderProperties emailSenderProperties;

    @Autowired
    public EmailSenderConfig(EmailSenderProperties emailSenderProperties) {
        this.emailSenderProperties = emailSenderProperties;
    }

    @Bean
    public Session configure() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", emailSenderProperties.getSmtpServerName());
        prop.put("mail.smtp.port", emailSenderProperties.getSmtpServerPort());
        prop.put("mail.smtp.ssl.trust", emailSenderProperties.getSmtpServerName());
        prop.put("mail.imaps.port", emailSenderProperties.getImapServerPort());
        prop.put("mail.imaps.host", emailSenderProperties.getImapServerName());

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSenderProperties.getUserName(), emailSenderProperties.getUserPassword());
            }
        });
    }
}
