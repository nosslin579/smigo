package org.smigo.user;

import kga.PlantData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class UserSessionImpl implements UserSession {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private long signupStart = 0;
    private final Map<String, String> translation = new HashMap<String, String>();
    private List<PlantData> plants = new ArrayList<PlantData>();
    private UserBean user = null;

    @Override
    public void registerSignupStart() {
        this.signupStart = System.nanoTime();
    }

    @Override
    public long getSignupTime() {
        if (signupStart == 0)
            return 0;
        return (System.nanoTime() - signupStart) / 1000000000;
    }

    @Override
    public Map<String, String> getTranslation() {
        return translation;
    }

    @Override
    public List<PlantData> getPlants() {
        return plants;
    }

    @Override
    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSessionImpl";
    }

    @Override
    public UserBean getUser() {
        return user;
    }
}
