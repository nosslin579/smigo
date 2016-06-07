package org.smigo.species;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sourceforge.kga.Plant;
import org.sourceforge.kga.PlantList;
import org.sourceforge.kga.Taxon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Install kga to local maven repo:
 * ./mvn install:install-file -Dfile=KitchenGardenAid.1.8.1.jar -DgroupId=kga -DartifactId=kga -Dversion=1.8.1 -Dpackaging=jar
 */
@ContextConfiguration(classes = {SpeciesTestConfiguration.class})
public class KitchenGardenAidDataTest extends AbstractTestNGSpringContextTests {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private SpeciesDao speciesDao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(javax.sql.DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test(enabled = true)
    public void testDataIsComplete() throws Exception {
        List<String> sqlStatement = new ArrayList<>();

        String speciesPath = "resources/species.xml";
        InputStream stream = PlantList.class.getResourceAsStream("/" + speciesPath);
        PlantList.initialize(stream);

        final Collection<Plant> plants = PlantList.getResources().getPlants();
        for (Plant plant : plants) {
            if (plant.getType() == Taxon.Type.FAMILY && getFamily(plant) == null) {
                sqlStatement.add(0, "INSERT INTO FAMILIES(ID,NAME)VALUES (" + plant.getId() + ",'" + plant.getName() + "');");
            }
            if (plant.getImage() == null) {
                log.info("No image, not processing " + plant.getType() + plant);
                continue;
            }
            try {
                final Species species = getSpecies(plant);
                if (species == null) {
                    sqlStatement.add("INSERT INTO SPECIES(ID,SCIENTIFIC_NAME,ITEM,ANNUAL,FAMILY_ID,ICONFILENAME) VALUES (" + plant.getId() + ",'" + plant.getName() + "'," + plant.isItem() + ",true" + "," + plant.getFamily().getId() + ",'" + plant.getId() + ".png');");
                } else if (species.isAnnual() != (plant.lifetime.getRepetitionYears() == 1)) {
                    log.error("Annual differ:" + plant);
                }
                final Map<String, String> translations = plant.getTranslations();
                for (Map.Entry<String, String> kgaLangAndTranslation : translations.entrySet()) {
                    final String kgaLang = kgaLangAndTranslation.getKey();
                    final String kgaTranslation = kgaLangAndTranslation.getValue();
                    final String smigoTranslation = getVernaculars(plant.getId(), kgaLang);

                    if (kgaTranslation == null) {
                        log.info("KGA translation missing" + plant);
                    } else if (smigoTranslation == null) {
                        sqlStatement.add("INSERT INTO VERNACULARS(SPECIES_ID,VERNACULAR_NAME,LANGUAGE,COUNTRY) VALUES (" + plant.getId() + ",'" + kgaTranslation.replaceAll("'", "''") + "','" + kgaLang + "','');");
                    } else if (Objects.equals(smigoTranslation, kgaTranslation)) {
                        //looking good
                    } else {
                        log.error("Translation exists but differ kga:" + plant.getId() + kgaLangAndTranslation + " - smigo:'" + getVernaculars(plant.getId(), kgaLang) + "'");
                    }
                }
            } catch (Exception e) {
                log.error("Could not map plant:" + plant, e);
            }
            FileUtils.writeLines(new File("/tmp/data.sql"), sqlStatement);
            Assert.assertEquals(sqlStatement, new ArrayList<String>(), sqlStatement.toString());
        }
    }

    private Family getFamily(Plant plant) {
        final List<Family> families = familyDao.getFamilies();
        for (Family family : families) {
            if (family.getId() == plant.getId()) {
                return family;
            }
        }
        return null;
    }

    private Species getSpecies(Plant plant) {
        try {
            return speciesDao.getSpecies(plant.getId());
        } catch (EmptyResultDataAccessException e) {
        }
        return null;
    }

    private String getVernaculars(int id, String lang) {
        final String sql = "SELECT VERNACULAR_NAME FROM VERNACULARS WHERE SPECIES_ID=" + id + " AND LANGUAGE='" + lang + "';";
        final List<Map<String, Object>> ret = jdbcTemplate.queryForList(sql);
        return ret.iterator().hasNext() ? ret.iterator().next().values().iterator().next().toString() : null;

    }

}
