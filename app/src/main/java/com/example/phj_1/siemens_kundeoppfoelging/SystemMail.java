package com.example.phj_1.siemens_kundeoppfoelging;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SystemMail {

    private Session session;

    public SystemMail() { }


    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {


        //information neeed for gmail.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //start off a session to send email
        session = Session.getDefaultInstance(props,
                new Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailSetup.EMAIL, emailSetup.PASSWORD);
                    }
                });

        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);
        message.setText(body);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        Transport.send(message);

    }


}
