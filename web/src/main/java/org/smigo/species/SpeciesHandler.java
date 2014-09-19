package org.smigo.species;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
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

import java.util.List;
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
        return speciesDao.addSpecies(species, user.getId());
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
        Map<Integer, Family> familyMap = Maps.uniqueIndex(familyDao.getFamilies(), new IdMapper());
        Map<Integer, SpeciesView> ret = Maps.uniqueIndex(speciesDao.getSpecies(familyMap), new IdMapper());
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

    private static class IdMapper implements Function<Id, Integer> {
        @Override
        public Integer apply(Id input) {
            return input.getId();
        }
    }

    public Species getSpecies(int id) {
        return speciesDao.getSpecies(id);
    }

    public List<Family> getFamilies() {
        return familyDao.getFamilies();
    }
}
