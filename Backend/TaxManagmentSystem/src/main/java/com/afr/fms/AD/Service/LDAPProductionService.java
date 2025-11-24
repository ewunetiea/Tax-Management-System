package com.afr.fms.AD.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LDAPProductionService {
     private static final Logger logger = LoggerFactory.getLogger(ADService.class);

    private final LdapTemplate ldapTemplate;
    private final LdapContextSource contextSource;

    public LDAPProductionService(LdapTemplate ldapTemplate, LdapContextSource contextSource) {
        this.ldapTemplate = ldapTemplate;
        this.contextSource = contextSource;
    }

    /**
     * Authenticate a user against LDAP.
     */
    public boolean authenticateUser(String username, String password) throws Exception {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new Exception("Username or password cannot be empty.");
        }


        System.out.println("_____inside____service______________");
        String safeUsername = sanitizeUsername(username);
        String userDn = findUserDn(safeUsername);

        if (userDn == null) {
            throw new Exception("We could not find your account. Please check your username.");
        }

        try {
            // Pooled connection from contextSource
            contextSource.getContext(userDn, password).close();
            return true;

        } catch (org.springframework.ldap.AuthenticationException ex) {
            throw new Exception(mapAuthExceptionMessage(ex, safeUsername));
        } catch (Exception ex) {
            logger.error("LDAP connection error for {}: {}", maskUsername(safeUsername), ex.getMessage(), ex);
            throw new Exception(
                    "We are unable to connect to the authentication service. Please try again later or check your credentials."
            );
        }
    }

    /**
     * Search for user's DN in LDAP.
     */
    private String findUserDn(String username) {

        System.out.println("_inside userDn_____________________");
        try {
            EqualsFilter filter = new EqualsFilter("sAMAccountName", username);


            System.out.println("__________Equals filter_______________");
            System.out.println(filter );

            List<String> dns = ldapTemplate.search(
                    LdapQueryBuilder.query()
                            .searchScope(SearchScope.SUBTREE)
                            .attributes("distinguishedName")
                            .filter(filter),
                    (AttributesMapper<String>) attrs -> (String) attrs.get("distinguishedName").get()
            );

              

            System.out.println("__________dns_______");
            System.out.println(dns);

            return dns.isEmpty() ? null : dns.get(0);

        } catch (Exception e) {
            logger.error("❌ Error searching user DN for {}: {}", username, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Map LDAP authentication errors to user-friendly messages.
     */
    private String mapAuthExceptionMessage(org.springframework.ldap.AuthenticationException ex, String username) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "";

        if (msg.contains("data 52e")) return "The username or password you entered is incorrect.";
        if (msg.contains("data 532")) return "Your password has expired. Please reset your password.";
        if (msg.contains("data 533")) return "Your account is currently disabled. Contact support for assistance.";
        if (msg.contains("data 775")) return "Your account is locked. Please contact support to unlock it.";

        return "Authentication failed. Please check your credentials and try again.";
    }

    /**
     * Sanitize the username to prevent LDAP injection.
     */
    private String sanitizeUsername(String username) {
        return username.replaceAll("[\\*\\(\\)\\x00]", "");
    }

    /**
     * Mask the username for logs.
     */
    private String maskUsername(String username) {
        if (username == null) return "null";
        return username.replaceAll("(?<=.{3}).(?=.*@)", "*");
    }
}
