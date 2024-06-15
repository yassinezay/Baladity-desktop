package edu.esprit.api;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {

    public static void sendEmail(String sourceEmail, String sourcePwd, String desEmail, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a mail session
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sourceEmail, sourcePwd);
            }
        });

        // Create a new email message
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sourceEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(desEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close(); // Closing the transport manually

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            // Handle specific exceptions
            if (e instanceof AuthenticationFailedException) {
                System.out.println("Authentication failed. Please check your credentials.");
            } else {
                e.printStackTrace();
            }
        }
    }

}
