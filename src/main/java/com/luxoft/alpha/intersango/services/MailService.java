package com.luxoft.alpha.intersango.services;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Component
public class MailService {
    private static final Logger logger = Logger.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${mail.subject.prefix}")
    private String prefix;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.url}")
    private String url;

    public void sendMail(String to, String subject, String template, Map<String, Object> map) {
        sendMail(from,
                new String[]{to},
                null,
                null,
                subject,
                template,
                map);
    }

    public void sendMail(String from, String[] to, String[] copyTo, String[] blindCopyTo,
                         String subject, String templateLocation, Map<String, Object> model) {
        model.put("url", url);

        final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);

        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(from);
            helper.setTo(to);

            if (copyTo != null) {
                helper.setCc(copyTo);
            }

            if (blindCopyTo != null) {
                helper.setBcc(blindCopyTo);
            }

            helper.setSubject(prefix + " " + subject);
            helper.setText(text, true);
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            this.mailSender.send(message);
        } catch (MailSendException sendMailException) {
            for (Exception e : sendMailException.getMessageExceptions()) {
                logger.warn(e.getMessage(), e);
            }
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }
    }
}