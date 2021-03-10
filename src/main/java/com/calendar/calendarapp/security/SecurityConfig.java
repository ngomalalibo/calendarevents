package com.calendar.calendarapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    
    private static List<String> clients = Arrays.asList("google");
    private static String CLIENT_PROPERTY_KEY
            = "spring.security.oauth2.client.registration.";
    
    @Autowired
    private Environment env;
    
    
    /*@Override
    public void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf().disable().antMatcher("/**").authorizeRequests()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login().loginPage("https://googleeventsapp.herokuapp.com")
                    .defaultSuccessUrl("http://googleeventsapp.herokuapp.com/welcome")
                    .and().logout().logoutSuccessUrl("https://googleeventsapp.herokuapp.com");
    }*/
    
    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
    accessTokenResponseClient()
    {
        
        return new NimbusAuthorizationCodeTokenResponseClient();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
            .antMatchers("/home")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login().loginPage("/home")
            .defaultSuccessUrl("https://googleeventsapp.herokuapp.com/welcome")
            .failureUrl("https://googleeventsapp.herokuapp.com/welcome/")
            .tokenEndpoint()
            .accessTokenResponseClient(accessTokenResponseClient())
            .and()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorization")
            .authorizationRequestRepository(authorizationRequestRepository())
            .and()
            .redirectionEndpoint()
            .baseUri("login/oauth2/code");
            // .baseUri("/oauth2/redirect");
    }
    
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest>
    authorizationRequestRepository()
    {
        
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
    
    @Bean
    public OAuth2AuthorizedClientService authorizedClientService()
    {
        
        return new InMemoryOAuth2AuthorizedClientService(
                clientRegistrationRepository());
    }
    
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository()
    {
        List<ClientRegistration> registrations = clients.stream()
                                                        .map(c -> getRegistration(c))
                                                        .filter(registration -> registration != null)
                                                        .collect(Collectors.toList());
        
        return new InMemoryClientRegistrationRepository(registrations);
    }
    
    private ClientRegistration getRegistration(String client)
    {
        String clientId = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-id");
        
        if (clientId == null)
        {
            return null;
        }
        
        String clientSecret = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-secret");
        return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                                          .clientId(clientId).clientSecret(clientSecret).build();
        /*if (client.equals("google"))
        {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                                              .clientId(clientId).clientSecret(clientSecret).build();
        }
        if (client.equals("facebook"))
        {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                                                .clientId(clientId).clientSecret(clientSecret).build();
        }
        return null;*/
    }
}
