package org.smigo.user;

import kga.PlantData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class UserSessionImpl implements UserSession {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private List<PlantData> plants = new ArrayList<PlantData>();
    private UserBean user = new UserBean();

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
