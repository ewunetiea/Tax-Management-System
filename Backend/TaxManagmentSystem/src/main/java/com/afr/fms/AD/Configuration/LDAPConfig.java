package com.afr.fms.AD.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;
import org.springframework.ldap.pool.validation.DefaultDirContextValidator;
import com.afr.fms.Admin.Mapper.SMSMapper;
import com.afr.fms.Admin.Service.SMSService;

@Configuration
public class LDAPConfig {

    private static final Logger logger = LoggerFactory.getLogger(LDAPConfig.class);

    // @Autowired
    // private SMSMapper smsMapper;

    // @Autowired
    // private SMSService smsService;

    /**
     * ContextSource bound with a service account.
     */
    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();

        // Your domain controller
        // String ldapUrl = "ldap://DR-ADS-001.awash.local:389";
        // contextSource.setUrl(ldapUrl);
        // contextSource.setBase("DC=awash,DC=local");

        // SMS sms = smsMapper.getActiveSMS("ad").get(0);
        // String secretKey = "mNYAjiYg/Iw8OMZH"; // 16 characters key
        // // Service account credentials
        // String serviceAccount = "username@awash.local"; // 👈 use UPN format
        // String servicePassword = "password";

        // serviceAccount = smsService.getDecryptedText(sms.getUser_name(), secretKey);
        // servicePassword = smsService.getDecryptedText(sms.getPassword(), secretKey);

        // serviceAccount = serviceAccount.contains("@") ? serviceAccount : serviceAccount + "@awash.local";




        //  for test server
        contextSource.setUrl("ldap://awashtest.local");
        contextSource.setBase("dc=awashtest,dc=local");
        contextSource.setUserDn("abvision@awashtest.local");
        contextSource.setPassword("Bfub@aib205");


        // contextSource.setUserDn(serviceAccount);
        // contextSource.setPassword(servicePassword);

        contextSource.setPooled(false);
        contextSource.setReferral("follow"); // follow referrals in AD
        contextSource.setAnonymousReadOnly(false);

        try {
            contextSource.afterPropertiesSet();
            // logger.info("✅ LDAP service account configured successfully: {}",
            // maskUsername(serviceAccount));
        } catch (Exception e) {
            logger.error("❌ Error initializing LDAP context source: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize LDAP context source", e);
        }

        return contextSource;
    }

    /**
     * Wrap ContextSource with pooling for concurrency.
     */

    @Bean
    public PoolingContextSource poolingContextSource(LdapContextSource contextSource) {
        PoolingContextSource poolingContextSource = new PoolingContextSource();
        poolingContextSource.setContextSource(contextSource);

        // Validate connections with a cheap "ping"
        poolingContextSource.setDirContextValidator(new DefaultDirContextValidator());
        poolingContextSource.setTestOnBorrow(true); // Validate when borrowed
        poolingContextSource.setTestWhileIdle(true); // Validate idle connections

        // --- Pool tuning ---
        poolingContextSource.setMaxTotal(200); // Max connections in the pool
        poolingContextSource.setMaxIdle(50); // Keep idle connections ready
        poolingContextSource.setMinIdle(10); // Warm pool with minimum connections
        poolingContextSource.setMaxWait(10000); // Wait up to 10s if pool exhausted

        // Optional: eviction policy (idle connections cleanup)
        poolingContextSource.setTimeBetweenEvictionRunsMillis(60000); // every 1 min
        poolingContextSource.setMinEvictableIdleTimeMillis(300000); // 5 min

        return poolingContextSource;
    }

    /**
     * Thread-safe template for LDAP operations.
     */
    @Bean
    public LdapTemplate ldapTemplate(PoolingContextSource poolingContextSource) {
        LdapTemplate template = new LdapTemplate(poolingContextSource);
        template.setIgnorePartialResultException(true); // helps in AD with referrals
        return template;
    }

    /**
     * Masks sensitive username information for logging.
     */
    private String maskUsername(String username) {
        if (username == null)
            return "null";
        return username.replaceAll("(?<=.{3}).(?=.*@)", "*");
    }
}
