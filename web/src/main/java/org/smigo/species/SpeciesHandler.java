package org.smigo.species;

import kga.Family;
import kga.Id;
import kga.Species;
import kga.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.SpeciesView;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;

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

    public Map<Integer, SpeciesView> getSpeciesMap() {
        long start = System.currentTimeMillis();
        Map<Integer, Family> familyMap = convertToMap(familyDao.getFamilies());
        Map<Integer, SpeciesView> ret = convertToMap(speciesDao.getSpecies(familyMap, Locale.ENGLISH));
        //Add rules to species
        final List<Rule> rules = ruleDao.getRules(familyMap);
        for (Rule r : rules) {
            int hostId = r.getHost().getId();
            Species s = ret.get(hostId);
            s.addRule(r);
        }
        long l = System.currentTimeMillis() - start;
        return ret;
    }

    private static <V extends Id> Map<Integer, V> convertToMap(Collection<V> collection) {
        final Map<Integer, V> ret = new HashMap<Integer, V>();
        for (V v : collection) {
            ret.put(v.getId(), v);
        }
        return ret;
    }

    public Species getSpecies(int id) {
        return speciesDao.getSpecies(id, Locale.ENGLISH);
    }

    public List<Family> getFamilies() {
        return familyDao.getFamilies();
    }

}
