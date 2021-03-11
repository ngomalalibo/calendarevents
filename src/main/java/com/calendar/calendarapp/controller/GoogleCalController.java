package com.calendar.calendarapp.controller;

import com.calendar.calendarapp.model.CalendarObj;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class GoogleCalController
{
    
    private static final String APPLICATION_NAME = "Calendar Application 2021";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;
    
    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
    // private static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    
    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;
    
    private Set<Event> events = new HashSet<>();
    
    private static boolean isAuthorised = false;
    
    final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
    final DateTime date2 = new DateTime(new Date());
    
    private final int START_HOUR = 8;
    private final int START_MIN = 00;
    private final int END_HOUR = 20;
    private final int END_MIN = 00;
    
    public void setEvents(Set<Event> events)
    {
        this.events = events;
    }
    
    @GetMapping(value = "/calendar")
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception
    {
        return new RedirectView(authorize(redirectURI));
    }
    
    @RequestMapping(value = "/calendar", method = RequestMethod.GET, params = "code")
    public String oauth2Callback(@RequestParam(value = "code") String code, Model model, OAuth2AuthenticationToken authentication)
    {
        if (isAuthorised)
        {
            try
            {
                model.addAttribute("title", "Calendar Events");
                model.addAttribute("calendarObjs", getCalendarEventList(code, redirectURI, model, authentication));
                
            }
            catch (Exception e)
            {
                model.addAttribute("calendarObjs", new ArrayList<CalendarObj>());
            }
            
            return "calendar";
        }
        else
        {
            return "/";
        }
    }
    
    @GetMapping(value = "/error")
    public String accessDenied(Model model)
    {
        
        model.addAttribute("message", "Not authorised.");
        return "login";
        
    }
    
    public Set<Event> getEvents() throws IOException
    {
        return this.events;
    }
    
    private String authorize(String redirectURL) throws Exception
    {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null)
        {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                                                           Collections.singleton(CalendarScopes.CALENDAR)).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURL);
        
        isAuthorised = true;
        
        return authorizationUrl.build();
    }
    
    
    @GetMapping(value = {"/", "/login", "/logout"})
    public String login(Model model)
    {
        isAuthorised = false;
        
        return "login";
    }
    
    /*@GetMapping({"/home"})
    public String getHome(Model model)
    {
        return "home";
    }
    
    @GetMapping("/welcome")
    public String welcome()
    {
        return "welcome";
    }*/
    
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    
    public void getLoginInfo(Model model, OAuth2AuthenticationToken authentication)
    {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        String userInfoEndpointUri = client.getClientRegistration()
                                           .getProviderDetails().getUserInfoEndpoint().getUri();
        
        if (!StringUtils.isEmpty(userInfoEndpointUri))
        {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                                                                     .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            model.addAttribute("name", userAttributes.get("name"));
            
            // boolean b = SendMail.sendMailSSL(SendMail.getMailInstance(userAttributes.get("email").toString()));
            // System.out.println("Response " + b);
        }
    }
    
    private List<CalendarObj> getCalendarEventList(String calenderApiCode, String redirectURL, Model model, OAuth2AuthenticationToken authentication)
    {
        String message;
        
        
        com.google.api.services.calendar.model.Events eventList;
        try
        {
            //OAuth2AuthenticationToken
            TokenResponse response = flow.newTokenRequest(calenderApiCode).setRedirectUri(redirectURL).execute();
            credential = flow.createAndStoreCredential(response, "userID");
            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            Calendar.Events events = client.events();
            eventList = events.list("primary").setTimeMin(date1).setTimeMax(date2).execute();
            message = eventList.getItems().toString();
            System.out.println("My: " + eventList.getItems());
            
            getLoginInfo(model, authentication);
            
            eventList = events.list("primary").setSingleEvents(true).setTimeMin(date1).setTimeMax(date2).setOrderBy("startTime").execute();
            
            List<Event> items = eventList.getItems();
            
            CalendarObj calendarObj;
            List<CalendarObj> calendarObjs = new ArrayList<>();
            
            for (Event event : items)
            {
                
                Date startDateTime = new Date(event.getStart().getDateTime().getValue());
                Date endDateTime = new Date(event.getEnd().getDateTime().getValue());
                
                long diffInMillies = endDateTime.getTime() - startDateTime.getTime();
                int diffmin = (int) (diffInMillies / (60 * 1000));
                
                calendarObj = new CalendarObj();
                
                if (event.getSummary() != null && event.getSummary().length() > 0)
                {
                    calendarObj.setTitle(event.getSummary());
                }
                else
                {
                    calendarObj.setTitle("No Title");
                }
                
                calendarObj.setStartHour(startDateTime.getHours());
                calendarObj.setStartMin(startDateTime.getMinutes());
                calendarObj.setEndHour(endDateTime.getHours());
                calendarObj.setEndMin(endDateTime.getMinutes());
                calendarObj.setDuration(diffmin);
                
                calendarObj.setStartEnd(sdf.format(startDateTime) + " - " + sdf.format(endDateTime));
                
                calendarObjs.add(calendarObj);
            }
            System.out.println("cal message:" + message);
            return calendarObjs;
            
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }
}