package com.calendar.calendarapp.email;

import com.calendar.calendarapp.templates.ActionableEmail;
import com.calendar.calendarapp.utils.CustomNullChecker;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

public class SendMail
{
    private static String template;
    
    @Value("${google.mail.username}")
    static String username;
    @Value("${google.mail.password}")
    static String password;
    
    public static String host = "smtp.gmail.com";
    public static String port = "465";
    
    
    public SendMail()
    {
        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("templates/accessemail.html")).getFile());
        
        try
        {
            template = new String(Files.readAllBytes(file.toPath()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static ActionableEmail getMailInstance(String sentTo)
    {
        ActionableEmail mailObject = new ActionableEmail();
        mailObject.setSubject("Google Calendar Events - Login Confirmation");
        mailObject.setToAddresses(sentTo);
        mailObject.setFromAddresses(username != null ? username : "weblibrarianapp@gmail.com");
        mailObject.setLine1(
                "You have been logged in/out of the google calendar events app");
        mailObject.setMessage(getTemplate(mailObject));
        
        return mailObject;
    }
    
    
    public static boolean sendMailSSL(ActionableEmail actionableEmail)
    {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        
        Session session = Session.getInstance(properties, new Authenticator()
        {
            
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username != null ? username : "weblibrarianapp@gmail.com", password != null ? password : "Web1234567890");
            }
            
        });
        
        // Used to debug SMTP issues
        session.setDebug(true);
        
        try
        {
            MimeMessage message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(username != null ? username : "weblibrarianapp@gmail.com"));
            // message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", List.of(username != null ? username : "weblibrarianapp@gmail.com", actionableEmail.getToAddresses()))));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", Arrays.asList(username != null ? username : "weblibrarianapp@gmail.com", actionableEmail.getToAddresses()))));
            message.setSubject(actionableEmail.getSubject());
            message.setSentDate(new Date());
            message.setContent(actionableEmail.getMessage(), "text/html");
            
            Transport.send(message);
            
            System.out.println("Login Successful....");
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }
        return true;
    }
    
    
    public static String getTemplate(ActionableEmail actionableEmail)
    {
        
        return template//
                       .replaceAll("##LINE1##", CustomNullChecker.stringSafe(actionableEmail.getLine1()))//
                       .replaceAll("##companyname##", CustomNullChecker.stringSafe("Calendar Events"))//
                       .replaceAll("##messagetitle##", CustomNullChecker.stringSafe(actionableEmail.getSubject()))//
                       .replaceAll("##personname##", CustomNullChecker.stringSafe(actionableEmail.getPersonName()))//
                       .replaceAll("##emailaddress##", CustomNullChecker.stringSafe(actionableEmail.getToAddresses()));
    }
    
    /*public static void main(String[] args)
    {
        SendMail sendMail = new SendMail();
        boolean b = sendMailSSL(sendMail.getMailInstance());
        System.out.println("Response " + b);
    }*/
}
