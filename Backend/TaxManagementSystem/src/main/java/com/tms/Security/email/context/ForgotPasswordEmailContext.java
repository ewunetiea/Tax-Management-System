package com.tms.Security.email.context;

import com.tms.Admin.Entity.User;
import org.springframework.web.util.UriComponentsBuilder;
public class ForgotPasswordEmailContext extends AbstractEmailContext {

    private String token;


    @Override
    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        User user = (User) context; // we pass the customer informati
        put("firstName", user.getFirst_name());
        put("lastName", user.getMiddle_name());
        setTemplateLocation("password/reset");
        setSubject("Reset your password");
        setFrom("afrfmsadmin@awashbank.com");
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", this.token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/user/changePassword").queryParam("token", token).toUriString();
        put("passwordResetURL", url);
    }
}
