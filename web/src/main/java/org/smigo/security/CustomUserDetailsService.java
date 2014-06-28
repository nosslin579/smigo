package org.smigo.security;

import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DatabaseResource databaseResource;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return databaseResource.getUser(s);
    }
}
