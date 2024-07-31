package com.smart.Services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    public boolean sendEmail(String subject, String message, String to) {

        boolean flag = false;

        //variable for gmail
        String host = "smtp.gmail.com";

        String from = "sonupaul213@gmail.com";
        //get System properties
        Properties properties = System.getProperties();
        System.out.println(properties);

        //host set-setting important information
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        //get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sonupaul213@gmail.com", "kgytbygatvhaclxf");
            }
        });

        session.setDebug(true);
        //compose message
        MimeMessage message1 = new MimeMessage(session);

        try {

            //from email
            message1.setFrom(from);

            //adding recipient
            message1.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            //adding subject to message
            message1.setSubject(subject);

            //adding text to message
//            message1.setText(message);
            message1.setContent(message, "text/html");

            //send message using transport class
            Transport.send(message1);

            System.out.println("Successfully send");
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }
}
