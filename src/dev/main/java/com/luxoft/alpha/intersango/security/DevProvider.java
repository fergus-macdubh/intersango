package com.luxoft.alpha.intersango.security;

import com.luxoft.alpha.intersango.domain.Role;
import com.luxoft.alpha.intersango.domain.User;
import com.luxoft.alpha.intersango.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by ocherniaiev on 22.05.2015.
 */
public class DevProvider implements AuthenticationProvider {
    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();

        Properties prop = new Properties();

        try {
            prop.load(getClass().getResourceAsStream("/users/" + name + ".properties"));
        } catch (Exception e) {
            return null;
        }

        User user = userRepository.getUserByUsername(name);

        if(user == null) {
            user = new User(name);
            user.setRole(Role.ROLE_USER);
            user.setFirstName(prop.getProperty("firstName"));
            user.setLastName(prop.getProperty("lastName"));
            user.setLocation(prop.getProperty("location"));
            user.setOffice(prop.getProperty("office"));
            user.setMobile(prop.getProperty("mobile"));
            user.setEmail(prop.getProperty("email"));

            userRepository.saveAndFlush(user);
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
