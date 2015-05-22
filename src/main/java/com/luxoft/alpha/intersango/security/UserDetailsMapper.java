package com.luxoft.alpha.intersango.security;

import com.luxoft.alpha.intersango.domain.Role;
import com.luxoft.alpha.intersango.domain.User;
import com.luxoft.alpha.intersango.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class UserDetailsMapper extends LdapUserDetailsMapper {
    @Autowired
    UserRepository userRepository;

    private static final String LOCATION = "luxoftlocationname";
    private static final String FIRST_NAME = "givenname";
    private static final String LAST_NAME = "sn";
    private static final String MOBILE = "mobile";
    private static final String MAIL = "mail";
    private static final String OFFICE = "physicaldeliveryofficename";

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        User user = userRepository.getUserByUsername(username);
        Collection<GrantedAuthority> auths = new HashSet<>(authorities);

        if(user == null) {
            user = new User(username);
            user.setRole(Role.ROLE_USER);
            setUserAttributes(user, ctx);
        } else {
            setUserAttributes(user, ctx);
            auths.add(new LdapAuthority(username, user.getRole().name()));
        }

        userRepository.saveAndFlush(user);

        return super.mapUserFromContext(ctx, username, auths);
    }

    private void setUserAttributes(User user, DirContextOperations ctx) {
        user.setFirstName(ctx.getStringAttribute(FIRST_NAME));
        user.setLastName(ctx.getStringAttribute(LAST_NAME));
        user.setLocation(ctx.getStringAttribute(LOCATION));
        user.setOffice(ctx.getStringAttribute(OFFICE));
        user.setMobile(ctx.getStringAttribute(MOBILE));
        user.setEmail(ctx.getStringAttribute(MAIL));
    }


}
