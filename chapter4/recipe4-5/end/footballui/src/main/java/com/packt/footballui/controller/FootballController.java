package com.packt.footballui.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class FootballController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/myself")
    public String user(Model model,
            @AuthenticationPrincipal OidcUser oidcUser) {
        model.addAttribute("userName", oidcUser.getName());
        model.addAttribute("audience", oidcUser.getAudience());
        model.addAttribute("expiresAt", oidcUser.getExpiresAt());
        model.addAttribute("claims", oidcUser.getClaims());
        return "myself";
    }

    // @GetMapping("/teams")
    // public String teams() {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // String accessToken = null;
    // if (authentication.getClass()
    // .isAssignableFrom(OAuth2AuthenticationToken.class)) {
    // OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken)
    // authentication;
    // String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
    // if (clientRegistrationId.equals("B2C_1_SUSI")) {

    // // OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
    // // clientRegistrationId, oauthToken.getName());
    // // accessToken = client.getAccessToken().getTokenValue();
    // }
    // }
    // return "teams";
    // }

    // @GetMapping("/teams")
    // public String teams(OAuth2AuthorizedClient auth2AuthorizedClient, Model model) {
    //     RestTemplate restTemplate = new RestTemplate();
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + auth2AuthorizedClient.getAccessToken().getTokenValue());

    //     HttpEntity<String> entity = new HttpEntity<>(null, headers);

    //     ResponseEntity<String> response = restTemplate.exchange(
    //             "http://localhost:8080/football/teams", HttpMethod.GET, entity, String.class);
    //     model.addAttribute("teams", response.getBody());
    //     return "teams";
    // }

    @GetMapping("/teams")
    public String teams(@RegisteredOAuth2AuthorizedClient("football-api") OAuth2AuthorizedClient authorizedClient,
            Model model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " +
                authorizedClient.getAccessToken().getTokenValue());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/football/teams", HttpMethod.GET, entity,
                String.class);
        model.addAttribute("teams", response.getBody());
        return "teams";
    }
}
