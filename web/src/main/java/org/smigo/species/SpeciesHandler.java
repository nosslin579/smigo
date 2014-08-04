package org.smigo.species;

import kga.*;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.JspFunctions;
import org.smigo.SpeciesView;
import org.smigo.garden.UpdateGardenBean;
import org.smigo.persitance.DatabaseResource;
import org.smigo.user.User;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class SpeciesHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
        //Add rules to species
        final List<Rule> rules = ruleDao.getRules();
        for (Rule r : rules) {
            int hostId = r.getHost().getId();
            Species s = ret.get(hostId);
            s.addRule(r);
        }
        return ret;
    }

    public Species getSpecies(Integer id) {
        return getGarden().getSpecies().get(id);
    }

    public List<Family> getFamilies() {
        return familyDao.getFamilies();
    }

    public void addYear(int year) {
        Garden g = getGarden();
        g.addYear(year);
        updateGarden(g.getPlants(year));
    }

    public Garden getGarden() {
        return new Garden(getSpeciesMap(), getPlants());
    }

    public List<PlantData> getPlants() {
        if (user.isAuthenticated()) {
            return plantDao.getPlants(user.getId());
        } else {
            return userSession.getPlants();
        }
    }

    public List<Hint> getHints() {
        return getGarden().getHints();
    }

    public void updateGarden(UpdateGardenBean updateGardenBean) {
        if (user.isAuthenticated()) {
            plantDao.addPlants(updateGardenBean.getAddList(), user.getId());
            plantDao.deletePlants(updateGardenBean.getRemoveList(), user.getId());
        } else {
            userSession.getPlants().removeAll(updateGardenBean.getRemoveList());
            userSession.getPlants().addAll(updateGardenBean.getAddList());
        }
    }
}
