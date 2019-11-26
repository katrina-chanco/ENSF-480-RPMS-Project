package com.RPMS.controller.contact_strategy;

import com.RPMS.model.entity.Property;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailStrategy implements ContactStrategy {
    /**
     * SMTP server URL and port
     */
    private String SMTPHostServer = "smtp.gmail.com";
    private String SMTPServerPort = "587";
    /**
     * RPMS email
     */
    private String RPMSEmail = "RPMS.System@gmail.com";
    /**
     * Password for RPMS email
     */
    private String RPMSPassword = "rUfpej-gehzav-9vorsi";
    /**
     * Mailing properties
     */
    private Properties prop;

    /**
     * Email strategy constructor
     */
    public EmailStrategy() {
        prop = new Properties();
        setServerProperties();
    }

    /**
     * Email contact strategy
     * @param message
     * @param property
     */
    @Override
    public void contactLandlord(String emailAddress, String message, Property property) {
        performContact(emailAddress, message, getLandlordEmail(property), "With regards to property at " + property.getAddress());
    }

    /**
     * Sends email from logged in user to landlord of selected property
     *
     * @param emailContents
     * @param recepient
     * @param subject
     */
    public void performContact(String emailAddress, String emailContents, String recepient, String subject) {
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(RPMSEmail, RPMSPassword);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setReplyTo(InternetAddress.parse(emailAddress));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recepient)
            );
            message.setSubject(subject);
            message.setContent(emailContents, "text/html");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets environment variables
     */
    private void setServerProperties() {
        prop.put("mail.smtp.host", SMTPHostServer);
        prop.put("mail.smtp.port", SMTPServerPort);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
    }

    /**
     * Gets the email of the landlord of the property
     *
     * @param property
     * @return
     */
    public String getLandlordEmail(Property property) {
        return property.getLandlord().getEmail().getEmailAddress();
    }
}
