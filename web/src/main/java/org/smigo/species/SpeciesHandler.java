package org.smigo.species;

import kga.Family;
import kga.Garden;
import kga.PlantData;
import kga.Square;
import kga.rules.Rule;
import org.smigo.JspFunctions;
import org.smigo.SpeciesView;
import org.smigo.persitance.DatabaseResource;
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
    private User user;
    @Autowired
    private UserSession userSession;
    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private PlantDao plantDao;
    @Autowired
    private RuleDao ruleDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private Comparator<org.smigo.SpeciesView> speciesComparator;

    public int addSpecies(SpeciesFormBean speciesFormBean) {
        int id = databaseResource.addSpecies(speciesFormBean, user.getId());
        userSession.getTranslation().put(JspFunctions.speciesMessageKey(id), speciesFormBean.getVernacularName());
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
        for (SpeciesView s : speciesDao.getSpecies())
            for (Rule r : s.getRules())
                if (r.getId() == ruleId)
                    return r;
        return null;
    }

    public void updateGarden(List<? extends PlantData> plants) {
        int year = plants.get(0).getYear();
        if (user.isAuthenticated()) {
            databaseResource.deleteYear(user.getId(), year);
            databaseResource.saveGarden(user.getId(), plants);
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

    public Map<Integer, SpeciesView> getSpeciesMap() {
        Map<Integer, SpeciesView> ret = new HashMap<Integer, SpeciesView>();
        for (SpeciesView s : speciesDao.getSpecies()) {
            ret.put(s.getId(), s);
        }
        final List<Rule> rules = ruleDao.getRules();
        for (Rule r : rules) {
            ret.get(r.getHost().getId()).addRule(r);
        }
        return ret;
    }

    public Collection<SpeciesView> getSpecies() {
        return getSpeciesMap().values();
    }

    public SpeciesView getSpecies(Integer id) {
        return getSpeciesMap().get(id);
    }

    public List<Family> getFamilies() {
        return familyDao.getFamilies();
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
        return new Garden(getSpeciesMap(), getPlants());
    }

    private List<PlantData> getPlants() {
        if (user.isAuthenticated()) {
            return plantDao.getPlants(user.getId());
        } else {
            return userSession.getPlants();
        }
    }

}
