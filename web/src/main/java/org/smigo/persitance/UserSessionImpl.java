package org.smigo.persitance;

import kga.Family;
import kga.Garden;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.CurrentUser;
import org.smigo.SpeciesView;
import org.smigo.entities.PlantDb;
import org.smigo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class UserSessionImpl implements UserSession {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private DatabaseResource databaseResource;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SpeciesComparator speciesComparator;
    @Autowired
    private CurrentUser currentUser;


    private Map<Integer, SpeciesView> species;
    private long signupStart = 0;
    private Garden garden;

    @PostConstruct
    public void postConstruct() {
        log.debug("Creating new userSession. Id:" + request.getRequestedSessionId());
        species = databaseResource.getSpecies(new User());
        garden = new Garden(species, null);
    }

    @Override
    public List<SpeciesView> getVisibleSpecies() {
        List<SpeciesView> ret = new ArrayList<SpeciesView>();
        for (SpeciesView s : species.values())
            if (s.isDisplay())
                ret.add(s);
        Collections.sort(ret, speciesComparator);
        return ret;
    }

    @Override
    public Map<Integer, SpeciesView> getSpecies() {
        return species;
    }

    @Override
    public List<SpeciesView> getAllSpecies() {
        List<SpeciesView> ret = new ArrayList<SpeciesView>(species.values());
        Collections.sort(ret, speciesComparator);
        return ret;
    }

    @Override
    public SpeciesView getSpecies(Integer id) {
        return species.get(id);
    }

    @Override
    public Map<Integer, Family> getFamilies() {
        return databaseResource.getFamilies();
    }

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
    public Garden getGarden() {
        return garden;
    }

    @Override
    public void updateGarden(List<PlantDb> plants) {
        Set<Integer> yearsToUpdate = new HashSet<Integer>();
        for (PlantDb p : plants)
            yearsToUpdate.add(p.getYear());
        garden.deleteYears(yearsToUpdate);
        for (PlantDb p : plants) {
            garden.addOrGetSquare(p.getYear(), p.getX(), p.getY()).addSpecies(species.get(p.getSpeciesId()));
        }
        if (currentUser.isAuthenticated()) {
            databaseResource.updateGarden(currentUser.getUser(), plants);
        }
    }

    @Override
    public void reloadGarden() {
        garden = new Garden(species, databaseResource.getPlants(currentUser.getUser()));
    }

    @Override
    public void reloadSpecies() {
        species = databaseResource.getSpecies(currentUser.getUser());
    }

    @Override
    public Rule getRule(int ruleId) {
        for (SpeciesView s : species.values())
            for (Rule r : s.getRules())
                if (r.getId() == ruleId)
                    return r;
        return null;
    }
}
