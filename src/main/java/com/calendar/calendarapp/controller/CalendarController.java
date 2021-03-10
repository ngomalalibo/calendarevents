package com.calendar.calendarapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalendarController
{
    
    @GetMapping({"/"})
    public String getHome()
    {
        
        return "Home Page";
    }
    
    @GetMapping("/welcome")
    public String welcome()
    {
        return "Welcome! Login successful";
    }
    /*private OAuth2AuthorizedClientService authorizedClientService;
    
    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication)
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
        }
        return "loginSuccess";
    }*/
    
    
}
