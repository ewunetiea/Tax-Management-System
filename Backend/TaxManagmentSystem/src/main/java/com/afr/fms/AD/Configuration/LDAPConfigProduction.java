package com.afr.fms.AD.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;

import com.afr.fms.Admin.Entity.SMS;
import com.afr.fms.Admin.Mapper.SMSMapper;
import com.afr.fms.Admin.Service.SMSService;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;
import org.springframework.ldap.pool.validation.DefaultDirContextValidator;

import com.afr.fms.Admin.Entity.SMS;
import com.afr.fms.Admin.Mapper.SMSMapper;
import com.afr.fms.Admin.Service.SMSService;

// @Configuration

public class LDAPConfigProduction {

    
    private static final Logger logger = LoggerFactory.getLogger(LDAPConfig.class);

    @Value("${ldap.urls}")
    private String ldapUrls;

    @Value("${ldap.base}")
    private String ldapBase;

    @Value("${ldap.timeout.connect}")
    private String ldapTimeoutConnect;

    @Value("${ldap.timeout.read}")
    private String ldapTimeoutRead;

    private final SMSMapper smsMapper;
    private final SMSService smsService;

    public LDAPConfigProduction(SMSMapper smsMapper, SMSService smsService) {
        this.smsMapper = smsMapper;
        this.smsService = smsService;
    }

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrls(ldapUrls.split(",")); // multiple LDAP URLs
        contextSource.setBase(ldapBase);

        SMS sms = smsMapper.getActiveSMS("ad").get(0);
        String secretKey = "mNYAjiYg/Iw8OMZH"; // 16-char key

    
        String serviceAccount = smsService.getDecryptedText(sms.getUser_name(), secretKey);
        String servicePassword = smsService.getDecryptedText(sms.getPassword(), secretKey);

        

        //         String serviceAccount = "Fras@awash.local";
        // String servicePassword = "Audit##@@11";


        serviceAccount = serviceAccount.contains("@") ? serviceAccount : serviceAccount + "@awash.local";

        contextSource.setUserDn(serviceAccount);
        contextSource.setPassword(servicePassword);

        contextSource.setPooled(false);
        contextSource.setReferral("follow");
        contextSource.setAnonymousReadOnly(false);
        contextSource.setBaseEnvironmentProperties(Map.of(
                "com.sun.jndi.ldap.connect.timeout", ldapTimeoutConnect,
                "com.sun.jndi.ldap.read.timeout", ldapTimeoutRead
        ));

        try {
            contextSource.afterPropertiesSet();
            // logger.info("✅ LDAP service account initialized successfully");
        } catch (Exception e) {
            logger.error("❌ Error initializing LDAP context source: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize LDAP context source", e);
        }

        return contextSource;
    }

    @Bean
    public PoolingContextSource poolingContextSource(LdapContextSource contextSource) {
        PoolingContextSource poolingContextSource = new PoolingContextSource();
        poolingContextSource.setContextSource(contextSource);

        poolingContextSource.setDirContextValidator(new DefaultDirContextValidator());
        poolingContextSource.setTestOnBorrow(true);
        poolingContextSource.setTestWhileIdle(true);

        poolingContextSource.setMaxTotal(300); // max concurrent connections
        poolingContextSource.setMaxIdle(100);
        poolingContextSource.setMinIdle(20);
        poolingContextSource.setMaxWait(15000); // wait for available connection

        poolingContextSource.setTimeBetweenEvictionRunsMillis(60000);
        poolingContextSource.setMinEvictableIdleTimeMillis(300000);

        return poolingContextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(PoolingContextSource poolingContextSource) {
        LdapTemplate template = new LdapTemplate(poolingContextSource);
        template.setIgnorePartialResultException(true);
        return template;
    }
    
}
