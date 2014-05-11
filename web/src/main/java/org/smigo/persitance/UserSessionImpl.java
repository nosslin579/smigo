package org.smigo.persitance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.entities.PlantDb;
import org.smigo.entities.User;
import org.sourceforge.kga.Family;
import org.sourceforge.kga.Garden;
import org.sourceforge.kga.Species;
import org.sourceforge.kga.rules.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
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
  private Map<Integer, Species> species;
  private User user;
  private long signupStart = 0;
  private Garden garden;


  @PreDestroy
  public void preDestroy() {
//    log.debug("Predestroy for user:" + user == null ? "null" : user.getUsername());
  }

  @PostConstruct
  public void postConstruct() {
//    log.debug("PostConstruct for user:" + user == null ? "null" : user.getUsername());
    log.debug("Creating new userSession. Id:" + request.getRequestedSessionId());
    user = new User("", "", "", false, "", request.getLocale());
    species = databaseResource.getSpecies(user);
    garden = new Garden(species, null);
  }

  @Override
  public List<Species> getVisibleSpecies() {
    List<Species> ret = new ArrayList<Species>();
    for (Species s : species.values())
      if (s.isDisplay())
        ret.add(s);
    Collections.sort(ret);
    return ret;
  }

  @Override
  public Map<Integer, Species> getSpecies() {
    return species;
  }

  @Override
  public List<Species> getAllSpecies() {
    List<Species> ret = new ArrayList<Species>(species.values());
    Collections.sort(ret);
    return ret;
  }

  @Override
  public Species getSpecies(Integer id) {
    return species.get(id);
  }

  @Override
  public Map<Integer, Family> getFamilies() {
    return databaseResource.getFamilies();
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public void setUser(User user) {
    this.user = user;
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
    if (user.getId() != 0) {
      databaseResource.updateGarden(user, plants);
    }
  }

  @Override
  public void reloadGarden() {
    garden = new Garden(species, databaseResource.getPlants(user));
  }

  @Override
  public void reloadSpecies() {
    species = databaseResource.getSpecies(user);
  }

  @Override
  public String toString() {
    return "Session{" + species + "," + user + "," + garden + "}";
  }

  @Override
  public Rule getRule(int ruleId) {
    for (Species s : species.values())
      for (Rule r : s.getRules())
        if (r.getId() == ruleId)
          return r;
    return null;
  }
}
