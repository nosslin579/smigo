package org.smigo.handler;

import kga.PlantData;
import org.smigo.entities.User;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class UserHandler {

    @Autowired
    private UserSession userSession;
    @Autowired
    private DatabaseResource databaseResource;
    @Autowired
    protected AuthenticationManager authenticationManager;

    public void updateUser(User user) {
        databaseResource.updateUserDetails(user);
    }

    public void createUser(User user, HttpServletRequest request) {
        long decideTime = System.currentTimeMillis() - request.getSession().getCreationTime();
        long signupTime = userSession.getSignupTime();
        databaseResource.addUser(user, signupTime, decideTime);

        //save plants
        List<PlantData> plants = userSession.getPlants();
        if (!plants.isEmpty()) {
            final User user1 = databaseResource.getUser(user.getUsername());
            databaseResource.updateGarden(user1.getId(), plants);
        }

        //set authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    public void updatePassword(Integer userId, String newPassword) {
        databaseResource.updatePassword(userId, newPassword);
        databaseResource.removeRememberMeToken(userId);
    }
}
