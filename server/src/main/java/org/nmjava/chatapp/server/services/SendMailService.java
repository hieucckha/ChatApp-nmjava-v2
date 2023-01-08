package org.nmjava.chatapp.server.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class SendMailService {
    private static final String FROM_EMAIL = "service.nmjava@gmail.com";
    private static final String PASSWORD = "wktrsmcbwnjecsax";
    private static Session session = null;

    public static Boolean sendMail(String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        if (session == null) {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
            props.put("mail.smtp.port", "587"); // TLS Port
            props.put("mail.smtp.auth", "true"); // Enable authentication
            props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
                }
            };
            SendMailService.session = Session.getInstance(props, auth);
        }

        MimeMessage msg = new MimeMessage(session);
        // Set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress(SendMailService.FROM_EMAIL, "No Reply@nmjava"));
        msg.setReplyTo(InternetAddress.parse(SendMailService.FROM_EMAIL, false));
        msg.setSubject(subject, "UTF-8");
        msg.setContent(body, "text/html;charset=UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

        Transport.send(msg);
        System.out.println("Gui mail thanh cong");

        return true;
    }
}
