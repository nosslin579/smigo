package org.smigo.persitance;

import org.smigo.entities.PlantDb;
import org.smigo.entities.User;
import org.sourceforge.kga.Family;
import org.sourceforge.kga.Garden;
import org.sourceforge.kga.Species;
import org.sourceforge.kga.rules.Rule;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface UserSession extends Serializable {
  List<Species> getVisibleSpecies();

  Map<Integer, Species> getSpecies();

  List<Species> getAllSpecies();

  Species getSpecies(Integer id);

  Map<Integer, Family> getFamilies();

  User getUser();

  void setUser(User user);

  void registerSignupStart();

  long getSignupTime();

  Garden getGarden();

  void updateGarden(List<PlantDb> plants);

  void reloadGarden();

  void reloadSpecies();

	Rule getRule(int ruleId);
}
