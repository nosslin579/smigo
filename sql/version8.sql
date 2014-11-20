ALTER TABLE visitlog
CHANGE method method VARCHAR(6) NOT NULL;

ALTER TABLE species
CHANGE item item tinyint(1) DEFAULT 0;
ALTER TABLE species
CHANGE annual annual tinyint(1) DEFAULT 1;

INSERT INTO species(id,scientific_name,family_id) VALUES (126,'Coriandrum sativum',7201);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (126,'Coriander');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (126,'Cilantro','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (135,'Pimpinella anisum',7201);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (135,'Anise');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (135,'Anís','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (136,'Carum carvi',7201);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (136,'Caraway');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (136,'Comino','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (23,'Allium fistulosum',7202);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (23,'Welsh onion');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (23,'Cebolleta','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (133,'Allium ursinum',7202);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (133,'Ramsons');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (133,'Ajo de oso','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (139,'Sinapis alba',7204);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (139,'White mustard');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (139,'Mostaza blanca','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (140,'Brassica juncea',7204);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (140,'Mustard greens');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (140,'Mostaza china','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (141,'Brassica nigra',7204);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (141,'Black mustard');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (141,'Mostaza negra','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (174,'Brassica oleracea var. acephala',7204);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (174,'Kale');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (174,'Col crespa','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (143,'Cicer arietinum',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (143,'Chickpea');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (143,'Garbanzo','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (145,'Lupinus',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (145,'Lupinus');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (145,'Altramuz Azul','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (146,'Trigonella foenum-graecum',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (146,'Fenugreek');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (146,'Fenogreco','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (147,'Medicago sativa',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (147,'Alfalfa');

INSERT INTO species(id,scientific_name,family_id) VALUES (148,'Lens culinaris',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (148,'Lentil');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (148,'Lenteja','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (144,'Vicia Sativa',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (144,'Vetch');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (144,'Veza','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (147,'Vicia Sativa',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (144,'Vetch');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (144,'Veza','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (179,'Arachis hypogaea',7205);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (179,'Peanut');

INSERT INTO species(id,scientific_name,family_id) VALUES (131,'Carthamus tinctorius',7207);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (131,'Safflower');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (131,'Cártamo','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (137,'Artemisia dracunculus',720);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (137,'Tarragon');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (137,'Estragón','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (180,'Helianthus tuberosus',720);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (180,'Jerusalem artichoke');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (180,'','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (156,'Panicum miliaceum',7208);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (156,'Millet');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (156,'Mijo','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (157,'Secale cereale',7208);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (157,'Rye');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (157,'Centeno','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (158,'Hordeum vulgare',7208);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (158,'Barley');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (158,'Cebada','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (159,'Avena sativa',7208);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (159,'Oat');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (159,'Avena','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (160,'Triticum aestivum',7208);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (160,'Wheat');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (160,'Trigo','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (161,'Oryza sativa',7208);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (161,'Rice');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (161,'Arroz','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (134,'Atriplex hortensis',7209);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (134,'Orache');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (134,'Ajo de Oso','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (130,'Fagopyrum esculentum',7210);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (130,'Buckwheat');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (130,'Alforfón / Trigo Sarraceno','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (138,'Myosotis sylvatica',7214);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (138,'Forget-me-not');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (,'','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (151,'Melissa officinalis',7215);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (151,'Lemon balm');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (151,'Melisa','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (152,'Origanum majorana',7215);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (152,'Marjoram');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (152,'Mejorana','es');

INSERT INTO species(id,scientific_name,family_id) VALUES (142,'Humulus lupulus',7217);
INSERT INTO species_translation(species_id,vernacular_name) VALUES (142,'Hop');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (142,'Lúpulo','es');

