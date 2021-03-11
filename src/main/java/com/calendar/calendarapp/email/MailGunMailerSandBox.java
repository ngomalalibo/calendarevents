package com.calendar.calendarapp.email;

import com.calendar.calendarapp.templates.ActionableEmail;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.MailBuilder;
import net.sargue.mailgun.Response;

public class MailGunMailerSandBox
{
    private static String MAILGUN_KEY = "874b5961af1222a0ae154e9365ab5572-aa4b0867-50c48450"; // mine
    private static String API_BASEURL = "https://api.mailgun.net/v3/sandbox146ee31de80d4d56a0920467c2b5ea20.mailgun.org"; // mine
    private static String DOMAIN = "sandbox146ee31de80d4d56a0920467c2b5ea20.mailgun.org";
    
    
    // private static String MAILGUN_KEY = "6f15a3126ce8b2a97a5ad5c739166d3e-059e099e-648cc821"; // Wales
    // private static String DOMAIN = "sandbox146ee31de80d4d56a0920467c2b5ea20.mailgun.org";
    
    
    public static boolean sendMail()
    {
        
        // String addr = emailAddress.stream().collect(Collectors.joining(", "));
        
        SendMail sendMail = new SendMail();
        ActionableEmail mailInstance = sendMail.getMailInstance("ngomalalibo@gmail.com", "into", "Ngo Alalibo");
        String temp = sendMail.getTemplate(mailInstance);
        Configuration c = new Configuration().domain(DOMAIN).apiUrl("https://api.mailgun.net/v3/" + DOMAIN + "/messages").apiKey(MAILGUN_KEY).from("Google Calendar App", mailInstance.getFromAddresses());
        MailBuilder mgb = Mail.using(c);
        mgb.to(mailInstance.getToAddresses());
        mgb.subject(mailInstance.getSubject());
        mgb.from(mailInstance.getFromAddresses());
        mgb.html(temp);
        
        Response res = mgb.build().send();
        
        System.out.println("Response: " + res.responseMessage());
        
        return res.isOk();
    }
    
    /*public JsonNode sendSimpleMessage(ActionableEmail actionableEmail) throws UnirestException {
        HttpRequestWithBody request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
			.basicAuth("api", MAILGUN_KEY)
                .field("from",actionableEmail.getFromAddresses())
                .field("to", actionableEmail.getToAddresses())
                .field("subject", actionableEmail.getSubject())
                .field("text", sendMail.getTemplate(actionableEmail))
                .asJson();
        return request.getBody();
    }*/
    public static String sendSimpleMessage(ActionableEmail actionableEmail, String template) throws UnirestException
    {
        HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
                                              .basicAuth("api", MAILGUN_KEY)
                                              .field("from", actionableEmail.getFromAddresses())
                                              .field("to", actionableEmail.getToAddresses())
                                              .field("subject", actionableEmail.getSubject())
                                              .field("text", template)
                                              .asString();
        return request.getBody();
    }
    
    /*public static JsonNode sendSimpleMessageTemplate() throws UnirestException
    {
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
                                                .basicAuth("api", MAILGUN_KEY)
                                                .queryString("from", "weblibrarianapp@gmail.com")
                                                .queryString("to", "ngomalalibo@gmail.com")
                                                .queryString("subject", "hello")
                                                .queryString("text", "testing")
                                                .asJson();
        return request.getBody();
    }*/
    
    public static void main(String[] args)
    {
        try
        {
            SendMail sendMail = new SendMail();
            ActionableEmail mailInstance = sendMail.getMailInstance("ngomalalibo@gmail.com", "into", "Ngo Alalibo");
            String temp = sendMail.getTemplate(mailInstance);
            
            
            // boolean b = sendMail();
            String response = sendSimpleMessage(mailInstance, temp);
            System.out.println("Response " + response);
        }
        catch (Exception e)
        {
            System.out.println("UnirestException " + e.getMessage());
            e.printStackTrace();
            
        }
    }
}
