package com.afr.fms.AD.Service;

import java.util.List;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;
import org.springframework.ldap.core.AttributesMapper;

@Service
public class ADService {

    private static final Logger logger = LoggerFactory.getLogger(ADService.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<Attributes> getADUserDetails(String username, String password) {
        try {
            // String ldapServer = "ldap://awash.local:389/dc=awash,dc=local";
            // Configure LDAP context source
            ldapTemplate.setContextSource(contextSource(username, password));

            // Define search controls
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            List<Attributes> results = ldapTemplate.search("", "(samaccountname=" + username + ")",
                    searchControls, (AttributesMapper<Attributes>) attrs -> attrs);
            return results;
        } catch (Exception ex) {
            logger.error("Error while validating AD user: ", ex.getMessage());
            return null;
        }
    }

    private LdapContextSource contextSource(String username, String password) {
        LdapContextSource contextSource = new LdapContextSource();
        // for production
        contextSource.setUrl("ldap://awash.local");
        contextSource.setBase("dc=awash,dc=local");
        contextSource.setUserDn(username + "@awash.local");
        // for test

        // contextSource.setUrl("ldap://awashtest.local");
        // contextSource.setBase("dc=awashtest,dc=local");
        // contextSource.setUserDn(username + "@awashtest.local");
        contextSource.setPassword(password);
        contextSource.setReferral("follow"); // Enable referral following
        contextSource.afterPropertiesSet(); // required
        return contextSource;
    }

}
