package com.afr.fms.AD.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import com.afr.fms.Admin.Entity.SMS;
import com.afr.fms.Admin.Mapper.SMSMapper;
import com.afr.fms.Admin.Service.SMSService;

// @Configuration
public class LDAPConfig {
    @Autowired
    private SMSMapper smsMapper;

    @Autowired
    private SMSService smsService;

    private static final Logger logger = LoggerFactory.getLogger(LDAPConfig.class);

    @Bean
    public LdapContextSource ldapContextSource() {
        SMS sms = smsMapper.getActiveSMS("ad").get(0);
        String secretKey = "mNYAjiYg/Iw8OMZH"; // 16 characters key
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://awash.local");
        contextSource.setBase("dc=awash,dc=local");
        String username = "", password = "";
        try {
            username = smsService.getDecryptedText(sms.getUser_name(), secretKey);
            password = smsService.getDecryptedText(sms.getPassword(), secretKey);
            contextSource.setUserDn(username);
            contextSource.setPassword(password);
        } catch (Exception e) {
            logger.error("Error while setting up LDAP context source: ", e);
        }

        // for test server
        // contextSource.setUrl("ldap://awashtest.local");
        // contextSource.setBase("dc=awashtest,dc=local");
        // contextSource.setUserDn("abvision@awashtest.local");
        // contextSource.setPassword("Bfub@aib205");

        contextSource.afterPropertiesSet(); // required
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSource());
    }
}
