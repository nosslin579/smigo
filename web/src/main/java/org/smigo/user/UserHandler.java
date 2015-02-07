package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.plants.PlantData;
import org.smigo.plants.PlantHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserSession userSession;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender javaMailSender;
    @Autowired
    private PersistentTokenRepository tokenRepository;
    @Autowired
    private PlantHandler plantHandler;
    @Autowired
    private UserDao userDao;
    @Value("${resetPasswordUrl}")
    private String resetPasswordUrl;

    private final Map<String, ResetKeyItem> resetKeyMap = new ConcurrentHashMap<String, ResetKeyItem>();

    public void createUser(RegisterFormBean user, String identityUrl, Locale locale) {
        final int userId = createUser(user, locale);
        userDao.addOpenId(userId, identityUrl);
    }

    public int createUser(RegisterFormBean user, Locale locale) {
        long decideTime = System.currentTimeMillis() - request.getSession().getCreationTime();
        final String rawPassword = user.getPassword();
        final String encoded = rawPassword.isEmpty() ? "" : passwordEncoder.encode(rawPassword);
        user.setLocale(locale);
        final int userId = userDao.addUser(user, encoded, decideTime);

        //save plants
        List<PlantData> plants = userSession.getPlants();
        plantHandler.addPlants(plants, userId);
        return userId;
    }

    public void updatePassword(AuthenticatedUser username, String newPassword) {
        final String encodedPassword = passwordEncoder.encode(newPassword);
        userDao.updatePassword(username.getId(), encodedPassword);
        tokenRepository.removeUserTokens(username.getUsername());
    }

    public void sendResetPasswordEmail(String email) {
        log.info("Sending reset email to: " + email);
        log.info("Size of resetKeyMap:" + resetKeyMap.size());
        final String id = UUID.randomUUID().toString();

        for (String s : resetKeyMap.keySet()) {
            ResetKeyItem resetKeyItem = resetKeyMap.get(s);
            if (resetKeyItem != null && email.equals(resetKeyItem.getEmail()) && resetKeyItem.isValid()) {
                log.warn("Multiple occurrence of email in resetPasswordMap" + resetKeyItem);
                resetKeyItem.invalidate();
            }
        }

        resetKeyMap.put(id, new ResetKeyItem(id, email));

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Smigo reset password");
        simpleMailMessage.setText("Click link to reset password. " + resetPasswordUrl + id);
        javaMailSender.send(simpleMailMessage);
    }

    public void acceptTermsOfService(AuthenticatedUser principal) {
        UserBean user = userDao.getUser(principal.getUsername());
        user.setTermsOfService(true);
        userDao.updateUser(principal.getId(), user);
    }

    public PublicInfoUserBean getUserPublicInfo(String username) {
        final UserBean user = userDao.getUser(username);
        return new PublicInfoUserBean(user);
    }

    public UserBean getUser(AuthenticatedUser user) {
        if (user == null) {
            return null;
        }
        return userDao.getUser(user.getUsername());
    }

    public AuthenticatedUser getCurrentUser() {
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        }
        return null;
    }

    public void updateUser(UserBean userBean, AuthenticatedUser user) {
        userSession.setUser(userBean);
        userDao.updateUser(user.getId(), userBean);
    }

    public void updateUser(int userId, UserBean userBean) {
        userDao.updateUser(userId, userBean);
    }

    public boolean setPassword(ResetKeyPasswordFormBean resetFormBean) {
        String resetKey = resetFormBean.getResetKey();
        ResetKeyItem resetKeyItem = resetKeyMap.get(resetKey);

        if (resetKeyItem == null) {
            log.warn("No valid resetPasswordKey found" + resetFormBean);
            return false;
        }

        if (!resetKeyItem.isValid()) {
            log.info("Reset key has expired." + resetFormBean);
            return false;
        }

        String email = resetKeyItem.getEmail();
        List<UserDetails> users = userDao.getUserByEmail(email);
        if (users.isEmpty()) {
            log.warn("No such email:" + email);
            return false;
        }

        AuthenticatedUser user = (AuthenticatedUser) users.get(0);
        String password = resetFormBean.getPassword();
        updatePassword(user, password);
        resetKeyItem.invalidate();
        return true;
    }
}
