package org.smigo.handler;

import kga.PlantData;
import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@Lazy
public class UserHandler {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserSession userSession;
    @Autowired
    private DatabaseResource databaseResource;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PersistentTokenRepository tokenRepository;

    public void updateUser(User user) {
        databaseResource.updateUserDetails(user);
    }

    public void createUser(User user, String identityUrl) {
        createUser(user);
        final User createadUser = databaseResource.getUser(user.getUsername());
        databaseResource.addOpenid(createadUser.getId(), identityUrl);

    }

    public void createUser(User user) {
        long decideTime = System.currentTimeMillis() - request.getSession().getCreationTime();
        long signupTime = userSession.getSignupTime();
        final String rawPassword = user.getPassword();
        if (!rawPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
        databaseResource.addUser(user, signupTime, decideTime);

        //save plants
        List<PlantData> plants = userSession.getPlants();
        if (!plants.isEmpty()) {
            final User user1 = databaseResource.getUser(user.getUsername());
            databaseResource.updateGarden(user1.getId(), plants);
        }
    }

    public void authenticateUser(String username, String password) {
        //set authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    public void updatePassword(String username, String newPassword) {
        final String encodedPassword = passwordEncoder.encode(newPassword);
        databaseResource.updatePassword(username, encodedPassword);
        tokenRepository.removeUserTokens(username);
    }

}
