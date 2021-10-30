package com.dodonov.oogosu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {
    private final EmailSenderProperties emailSenderProperties;

    @Autowired
    public JavaMailSenderConfig(EmailSenderProperties emailSenderProperties) {
        this.emailSenderProperties = emailSenderProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSenderProperties.getSmtpServerName());
        mailSender.setPort(emailSenderProperties.getSmtpServerPort());

        mailSender.setUsername(emailSenderProperties.getUserName());
        mailSender.setPassword(emailSenderProperties.getUserPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
