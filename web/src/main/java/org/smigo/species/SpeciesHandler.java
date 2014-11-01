package org.smigo.species;

import kga.Family;
import kga.Species;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.User;
import org.smigo.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SpeciesHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserSession userSession;
    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private RuleDao ruleDao;
    @Autowired
    private FamilyDao familyDao;

    public int addSpecies(SpeciesFormBean species, AuthenticatedUser user, Locale locale) {
        final int id = speciesDao.addSpecies(species, user.getId());
        speciesDao.setSpeciesTranslation(id, species.getVernacularName(), null);
        speciesDao.setSpeciesTranslation(id, species.getVernacularName(), locale);
        return id;
    }


    public String createIconFileName(int userId, int speciesId, CommonsMultipartFile uploadedIcon) {
        if (uploadedIcon == null || uploadedIcon.isEmpty()) {
            return null;
        } else {
            return "u" + userId + "s" + speciesId + "." + uploadedIcon.getContentType().replace("image/", "");
        }
    }

    public List<Species> getSpeciesMap(User user) {
        return speciesDao.getDefaultSpecies();
    }

    public Species getSpecies(int id) {
        return speciesDao.getSpecies(id);
    }

    public List<Family> getFamilies() {
        return familyDao.getFamilies();
    }

    public List<Species> searchSpecies(String query, Locale locale) {
        if (query.length() >= 5) {
            return speciesDao.searchSpecies('%' + query + '%', locale);
        } else if (query.length() >= 3) {
            return speciesDao.searchSpecies(query + '%', locale);
        } else {
            return speciesDao.searchSpecies(query, locale);
        }
    }

    public Map<String, String> getSpeciesTranslation(Locale locale) {
        return speciesDao.getSpeciesTranslation(locale);
    }

    public List<RuleBean> getRules() {
        return ruleDao.getRules();
    }
}
