package org.smigo.species;

import kga.Family;
import kga.Garden;
import kga.PlantData;
import kga.Square;
import kga.rules.Rule;
import org.smigo.JspMessageFunctions;
import org.smigo.SpeciesView;
import org.smigo.persitance.DatabaseResource;
import org.smigo.user.CurrentUser;
import org.smigo.user.User;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;

@Component
public class SpeciesHandler {

    private static final String DEFAULTICONNAME = "defaulticon.png";

    @Autowired
    private DatabaseResource databaseResource;
    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private UserSession userSession;
    @Autowired
    private SpeciesComparator speciesComparator;

    public int addSpecies(SpeciesFormBean speciesFormBean) {
        int id = databaseResource.addSpecies(speciesFormBean, currentUser.getId());
        userSession.getTranslation().put(JspMessageFunctions.species(id), speciesFormBean.getVernacularName());
        return id;
    }


    public String getIconFileName(String iconFileName) {
        return iconFileName == null ? DEFAULTICONNAME : iconFileName;
    }

    public String createIconFileName(int userId, int speciesId, CommonsMultipartFile uploadedIcon) {
        if (uploadedIcon == null || uploadedIcon.isEmpty()) {
            return null;
        } else {
            return "u" + userId + "s" + speciesId + "." + uploadedIcon.getContentType().replace("image/", "");
        }
    }

    public Rule getRule(int ruleId) {
        for (SpeciesView s : getSpecies().values())
            for (Rule r : s.getRules())
                if (r.getId() == ruleId)
                    return r;
        return null;
    }

    public void updateGarden(List<? extends PlantData> plants) {
        int year = plants.get(0).getYear();
        if (currentUser.isAuthenticated()) {
            databaseResource.deleteYear(currentUser.getUser().getId(), year);
            databaseResource.saveGarden(currentUser.getUser(), plants);
        } else {
            final List<PlantData> userSessionPlants = userSession.getPlants();
            for (Iterator<PlantData> iterator = userSessionPlants.iterator(); iterator.hasNext(); ) {
                PlantData plantDb = iterator.next();
                if (plantDb.getYear() == year) {
                    iterator.remove();
                }
            }
            userSessionPlants.addAll(plants);
        }
    }

    public List<SpeciesView> getVisibleSpecies() {
        final Map<Integer, SpeciesView> species = getSpecies();
        List<SpeciesView> ret = new ArrayList<SpeciesView>();
        for (SpeciesView s : species.values())
            if (s.isDisplay())
                ret.add(s);
        Collections.sort(ret, speciesComparator);
        return ret;
    }

    private Map<Integer, SpeciesView> getSpecies() {
        final User user = currentUser.isAuthenticated() ? currentUser.getUser() : new User();
        return databaseResource.getSpecies(user);
    }

    public List<SpeciesView> getAllSpecies() {
        List<SpeciesView> ret = new ArrayList<SpeciesView>(getSpecies().values());
        Collections.sort(ret, speciesComparator);
        return ret;
    }

    public SpeciesView getSpecies(Integer id) {
        return getSpecies().get(id);
    }

    public Map<Integer, Family> getFamilies() {
        return databaseResource.getFamilies();
    }

    public void addYear(int year) {
        Garden g = getGarden();
        g.addYear(year);
        List<PlantData> plants = new ArrayList<PlantData>();
        for (Square s : g.getSquaresFor(year)) {
            plants.addAll(s.getPlants());
        }
        updateGarden(plants);
    }

    public Garden getGarden() {
        return new Garden(getSpecies(), getPlants());
    }

    private List<PlantData> getPlants() {
        if (currentUser.isAuthenticated()) {
            return databaseResource.getPlants(currentUser.getUser());
        } else {
            return userSession.getPlants();
        }
    }

}
