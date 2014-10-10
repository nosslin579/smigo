package org.smigo.species;

import kga.Family;
import kga.IdUtil;
import kga.Species;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SpeciesHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpeciesDao speciesDao;
    @Autowired
    private RuleDao ruleDao;
    @Autowired
    private FamilyDao familyDao;

    public int addSpecies(SpeciesFormBean species, AuthenticatedUser user) {
        final int id = speciesDao.addSpecies(species, user.getId());
        speciesDao.setSpeciesTranslation(id, species.getVernacularName(), "");
        return id;
    }


    public String createIconFileName(int userId, int speciesId, CommonsMultipartFile uploadedIcon) {
        if (uploadedIcon == null || uploadedIcon.isEmpty()) {
            return null;
        } else {
            return "u" + userId + "s" + speciesId + "." + uploadedIcon.getContentType().replace("image/", "");
        }
    }

    public Map<Integer, SpeciesView> getSpeciesMap(AuthenticatedUser user, Locale locale) {
        long start = System.currentTimeMillis();
        Map<Integer, SpeciesView> ret = new HashMap<Integer, SpeciesView>();
        ret.putAll(IdUtil.convertToMap(speciesDao.getDefaultSpecies(locale)));
        if (user != null) {
            ret.putAll(IdUtil.convertToMap(speciesDao.getUserSpecies(user.getId(), locale)));
        }
        //Add rules to species
        final List<Rule> rules = ruleDao.getRules();
        for (Rule r : rules) {
            int hostId = r.getHost().getId();
            Species s = ret.get(hostId);
            if (s != null) {
                s.addRule(r);
            }
        }
        long l = System.currentTimeMillis() - start;
        return ret;
    }

    public Species getSpecies(int id) {
        return speciesDao.getSpecies(id, Locale.ENGLISH);
    }

    public List<Family> getFamilies() {
        return familyDao.getFamilies();
    }

    public List<SpeciesView> searchSpecies(String query, Locale locale) {
        if (query.length() >= 5) {
            return speciesDao.searchSpecies('%' + query + '%', locale);
        } else if (query.length() >= 3) {
            return speciesDao.searchSpecies(query + '%', locale);
        } else {
            return speciesDao.searchSpecies(query, locale);
        }
    }
}
