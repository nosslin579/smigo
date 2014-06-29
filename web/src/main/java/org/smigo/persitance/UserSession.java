package org.smigo.persitance;

import org.smigo.entities.PlantDb;
import org.smigo.entities.User;
import kga.Family;
import kga.Garden;
import org.smigo.SpeciesView;
import kga.rules.Rule;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface UserSession extends Serializable {
  List<SpeciesView> getVisibleSpecies();

  Map<Integer, SpeciesView> getSpecies();

  List<SpeciesView> getAllSpecies();

  SpeciesView getSpecies(Integer id);

  Map<Integer, Family> getFamilies();

    void registerSignupStart();

  long getSignupTime();

  Garden getGarden();

  void updateGarden(List<PlantDb> plants);

  void reloadGarden();

  void reloadSpecies();

	Rule getRule(int ruleId);

    Map<String,String> getTranslation();
}
