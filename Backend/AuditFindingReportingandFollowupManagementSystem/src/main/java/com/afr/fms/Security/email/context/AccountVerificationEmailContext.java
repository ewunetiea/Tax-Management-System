package com.afr.fms.Security.email.context;

import org.springframework.web.util.UriComponentsBuilder;

import com.afr.fms.Admin.Entity.User;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        User user = (User) context; // we pass the customer information
        put("firstName", user.getFirst_name());
        put("password", user.getPassword());

        setTemplateLocation("verfication/email-verification");
        setSubject("Complete your registration");
        setFrom("afrfms@awashbank.com");
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", this.token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
              //  .path("/bigbackend/api/user/verify").queryParam("token", token).toUriString();
                                .path("/afrfmsbackend/api/user/verify").queryParam("token", token).toUriString();

        put("verificationURL", url);
    }
}
