package org.smigo.species;

import kga.Family;
import kga.rules.Rule;

import java.util.List;
import java.util.Map;

interface RuleDao {

    List<Rule> getRules(Map<Integer, Family> familyMap);
}
