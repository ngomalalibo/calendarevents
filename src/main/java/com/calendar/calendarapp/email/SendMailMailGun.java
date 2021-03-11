package com.calendar.calendarapp.email;

import com.calendar.calendarapp.templates.ActionableEmail;
import com.calendar.calendarapp.utils.CustomNullChecker;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class SendMailMailGun
{
    private static String MAILGUN_KEY = System.getenv().get("MAILGUN_KEY");
    private static String API_BASEURL = System.getenv().get("MAILGUN_API_BASEURL");
    private static String DOMAIN = System.getenv().get("MAILGUN_DOMAIN");
    
    @Value("${google.mail.username}")
    static String username;
    @Value("${google.mail.password}")
    static String password;
    
    private static String template;
    
    public SendMailMailGun()
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
    
    public String sendSimpleMessage(ActionableEmail actionableEmail, String template) throws UnirestException
    {
        HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
                                              .basicAuth("api", MAILGUN_KEY)
                                              .field("from", actionableEmail.getFromAddresses())
                                              .field("to", actionableEmail.getToAddresses())
                                              .field("subject", actionableEmail.getSubject())
                                              .field("html", template)
                                              .asString();
        return request.getBody();
    }
    
    public ActionableEmail getMailInstance(String sentTo, String action, String userName)
    {
        ActionableEmail mailObject = new ActionableEmail();
        mailObject.setSubject("Google Calendar Events - Access Confirmation");
        mailObject.setToAddresses(sentTo);
        mailObject.setPersonName(userName);
        mailObject.setFromAddresses(username != null ? username : "weblibrarianapp@gmail.com");
        mailObject.setLine1(
                "You have successfully logged " + action + " the google calendar events app");
        mailObject.setMessage(getTemplate(mailObject));
        
        return mailObject;
    }
    
    public String getTemplate(ActionableEmail actionableEmail)
    {
        
        return template//
                       .replaceAll("##LINE1##", CustomNullChecker.stringSafe(actionableEmail.getLine1()))//
                       .replaceAll("##companyname##", CustomNullChecker.stringSafe("Calendar Events"))//
                       .replaceAll("##messagetitle##", CustomNullChecker.stringSafe(actionableEmail.getSubject()))//
                       .replaceAll("##personname##", CustomNullChecker.stringSafe(actionableEmail.getPersonName()))//
                       .replaceAll("##emailaddress##", CustomNullChecker.stringSafe(actionableEmail.getToAddresses()));
    }
    
    public static void main(String[] args)
    {
        try
        {
            SendMailMailGun sendMail = new SendMailMailGun();
            ActionableEmail mailInstance = sendMail.getMailInstance("ngomalalibo@gmail.com", "into", "Ngo Alalibo");
            String temp = sendMail.getTemplate(mailInstance);
            
            String response = sendMail.sendSimpleMessage(mailInstance, temp);
            System.out.println("Response " + response);
        }
        catch (Exception e)
        {
            System.out.println("UnirestException " + e.getMessage());
            e.printStackTrace();
            
        }
    }
}
