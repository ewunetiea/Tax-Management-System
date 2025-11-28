package com.tms.AD.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.stereotype.Service;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import java.util.Hashtable;
import java.util.List;

@Service
public class ADService {

    private static final Logger logger = LoggerFactory.getLogger(ADService.class);

    @Autowired
    private LdapTemplate ldapTemplate; // Pooled template using service account

    // private static final String LDAP_URL = "ldap://DR-ADS-001.awash.local:389"; // commented
     private static final String LDAP_URL = "ldap://awashtest.local"; 

    
    private static final String LDAP_BASE = "DC=awash,DC=local";

    /**
     * Authenticate user against Active Directory using their username and password.
     */
    public boolean authenticateUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            logger.warn("❌ Invalid input: username or password is empty");
            return false;
        }

        String safeUsername = username.replaceAll("[\\*\\(\\)\\x00]", "");
        // logger.info("Attempting authentication for user: {}",
        // maskUsername(safeUsername));

        // 1️⃣ Use service account to find user's DN
        String userDn = findUserDn(safeUsername);
        if (userDn == null) {
            logger.error("❌ User not found in AD for username: {}", maskUsername(safeUsername));
            return false;
        }

        // 2️⃣ Attempt direct bind with user's DN + password
        try {
            Hashtable<String, Object> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, LDAP_URL);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, userDn);
            env.put(Context.SECURITY_CREDENTIALS, password);

            // ✅ Set timeouts to avoid hanging
            env.put("com.sun.jndi.ldap.connect.timeout", "30000"); // 10 sec
            env.put("com.sun.jndi.ldap.read.timeout", "30000"); // 10 sec

            DirContext ctx = new InitialLdapContext(env, null);
            ctx.close();

            // logger.info("✅ Authentication successful for user: {}",
            // maskUsername(safeUsername));
            return true;

        } catch (AuthenticationException ex) {
            handleAuthException(ex, safeUsername);
            return false;
        } catch (Exception ex) {
            logger.error("❌ Unexpected error during authentication for user {}: {}", maskUsername(username),
                    ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Find the user's DN by searching with sAMAccountName using the pooled service
     * account.
     */
    private String findUserDn(String username) {
        try {
            EqualsFilter filter = new EqualsFilter("sAMAccountName", username);

            List<String> dns = ldapTemplate.search(
                    LdapQueryBuilder.query()
                            .searchScope(SearchScope.SUBTREE)
                            .attributes("distinguishedName")
                            .filter(filter),
                    (AttributesMapper<String>) attrs -> (String) attrs.get("distinguishedName").get());

            if (!dns.isEmpty()) {
                String dn = dns.get(0);
                // logger.info("✅ Found DN for user '{}': {}", username, dn);
                return dn;
            }

            logger.error("❌ User not found in AD for username: {}", username);
            return null;

        } catch (Exception e) {
            logger.error("❌ Error searching user DN for username: {} - {}", username, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Handle LDAP authentication exceptions with detailed messages.
     */
    private void handleAuthException(AuthenticationException ex, String username) {
        String msg = ex.getMessage();
        if (msg.contains("data 52e")) {
            logger.error("❌ Invalid credentials for user {}: {}", maskUsername(username), msg);
        } else if (msg.contains("data 532")) {
            logger.error("❌ Password expired for user {}: {}", maskUsername(username), msg);
        } else if (msg.contains("data 533")) {
            logger.error("❌ Account disabled for user {}: {}", maskUsername(username), msg);
        } else if (msg.contains("data 775")) {
            logger.error("❌ Account locked out for user {}: {}", maskUsername(username), msg);
        } else {
            logger.error("❌ Authentication error for user {}: {}", maskUsername(username), msg);
        }
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
