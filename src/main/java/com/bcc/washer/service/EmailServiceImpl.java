package com.bcc.washer.service;

import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.TemplateTYPE;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Qualifier("emailNotificationService")
@Service
public class EmailServiceImpl implements NotificationServiceI {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void notifyReservation(String destination, String subject, String text, BookableUnit bookableUnit) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("date", bookableUnit.getTimeSlot().getTimeInterval().getDate().toString());
        placeholders.put("time", bookableUnit.getTimeSlot().getTimeInterval().getStartTime().toString());
        placeholders.put("location", "Cămin C1 - Etaj 2 Cluj Napoca");
        placeholders.put("text", text);

        try {
            String body = loadEmailTemplate(TemplateTYPE.REZERVATION_CREATED.getPath(), placeholders);
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            message.setFrom("bbcwasher@gmail.com");
            helper.setTo(destination);
            helper.setSubject(subject);
            helper.setText(body, true);

            emailSender.send(message);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    private String loadEmailTemplate(String path, Map<String, String> placeholders) throws IOException {
        String template = new String(
                Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8
        );

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return template;
    }
}
