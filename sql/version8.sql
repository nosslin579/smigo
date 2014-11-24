ALTER TABLE visitlog
CHANGE method method VARCHAR(6) NOT NULL;

ALTER TABLE species
CHANGE item item tinyint(1) DEFAULT 0;
ALTER TABLE species
CHANGE annual annual tinyint(1) DEFAULT 1;

ALTER TABLE species
DROP COLUMN vernacularname;

UPDATE plants SET species_id = 10001 WHERE species_id = 174;
UPDATE plants SET species_id = 10002 WHERE species_id = 176;
UPDATE plants SET species_id = 10003 WHERE species_id = 177;
UPDATE plants SET species_id = 10004 WHERE species_id = 178;
UPDATE plants SET species_id = 10005 WHERE species_id = 179;
UPDATE plants SET species_id = 10006 WHERE species_id = 181;
UPDATE plants SET species_id = 10007 WHERE species_id = 188;
UPDATE plants SET species_id = 10008 WHERE species_id = 189;
UPDATE plants SET species_id = 10009 WHERE species_id = 190;
UPDATE plants SET species_id = 10010 WHERE species_id = 191;
UPDATE plants SET species_id = 10011 WHERE species_id = 192;
UPDATE plants SET species_id = 10012 WHERE species_id = 193;
UPDATE plants SET species_id = 10013 WHERE species_id = 194;
UPDATE plants SET species_id = 10014 WHERE species_id = 195;
UPDATE plants SET species_id = 10015 WHERE species_id = 196;
UPDATE plants SET species_id = 10016 WHERE species_id = 197;
UPDATE plants SET species_id = 10017 WHERE species_id = 198;
UPDATE plants SET species_id = 10018 WHERE species_id = 199;
UPDATE plants SET species_id = 10019 WHERE species_id = 200;
UPDATE plants SET species_id = 10020 WHERE species_id = 201;
UPDATE plants SET species_id = 10021 WHERE species_id = 202;
UPDATE plants SET species_id = 10022 WHERE species_id = 203;
UPDATE plants SET species_id = 10023 WHERE species_id = 204;

UPDATE species_translation SET species_id = 10001 WHERE species_id = 174;
UPDATE species_translation SET species_id = 10002 WHERE species_id = 176;
UPDATE species_translation SET species_id = 10003 WHERE species_id = 177;
UPDATE species_translation SET species_id = 10004 WHERE species_id = 178;
UPDATE species_translation SET species_id = 10005 WHERE species_id = 179;
UPDATE species_translation SET species_id = 10006 WHERE species_id = 181;
UPDATE species_translation SET species_id = 10007 WHERE species_id = 188;
UPDATE species_translation SET species_id = 10008 WHERE species_id = 189;
UPDATE species_translation SET species_id = 10009 WHERE species_id = 190;
UPDATE species_translation SET species_id = 10010 WHERE species_id = 191;
UPDATE species_translation SET species_id = 10011 WHERE species_id = 192;
UPDATE species_translation SET species_id = 10012 WHERE species_id = 193;
UPDATE species_translation SET species_id = 10013 WHERE species_id = 194;
UPDATE species_translation SET species_id = 10014 WHERE species_id = 195;
UPDATE species_translation SET species_id = 10015 WHERE species_id = 196;
UPDATE species_translation SET species_id = 10016 WHERE species_id = 197;
UPDATE species_translation SET species_id = 10017 WHERE species_id = 198;
UPDATE species_translation SET species_id = 10018 WHERE species_id = 199;
UPDATE species_translation SET species_id = 10019 WHERE species_id = 200;
UPDATE species_translation SET species_id = 10020 WHERE species_id = 201;
UPDATE species_translation SET species_id = 10021 WHERE species_id = 202;
UPDATE species_translation SET species_id = 10022 WHERE species_id = 203;
UPDATE species_translation SET species_id = 10023 WHERE species_id = 204;

UPDATE species SET id = 10001 WHERE id = 174;
UPDATE species SET id = 10002 WHERE id = 176;
UPDATE species SET id = 10003 WHERE id = 177;
UPDATE species SET id = 10004 WHERE id = 178;
UPDATE species SET id = 10005 WHERE id = 179;
UPDATE species SET id = 10006 WHERE id = 181;
UPDATE species SET id = 10007 WHERE id = 188;
UPDATE species SET id = 10008 WHERE id = 189;
UPDATE species SET id = 10009 WHERE id = 190;
UPDATE species SET id = 10010 WHERE id = 191;
UPDATE species SET id = 10011 WHERE id = 192;
UPDATE species SET id = 10012 WHERE id = 193;
UPDATE species SET id = 10013 WHERE id = 194;
UPDATE species SET id = 10014 WHERE id = 195;
UPDATE species SET id = 10015 WHERE id = 196;
UPDATE species SET id = 10016 WHERE id = 197;
UPDATE species SET id = 10017 WHERE id = 198;
UPDATE species SET id = 10018 WHERE id = 199;
UPDATE species SET id = 10019 WHERE id = 200;
UPDATE species SET id = 10020 WHERE id = 201;
UPDATE species SET id = 10021 WHERE id = 202;
UPDATE species SET id = 10022 WHERE id = 203;
UPDATE species SET id = 10023 WHERE id = 204;

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (126,'Coriandrum sativum',7201,'126.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (126,'Coriander');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (126,'Cilantro','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (135,'Pimpinella anisum',7201,'135.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (135,'Anise');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (135,'Anís','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (136,'Carum carvi',7201,'136.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (136,'Caraway');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (136,'Comino','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (23,'Allium fistulosum',7202,'23.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (23,'Welsh onion');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (133,'Allium ursinum',7202,'133.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (133,'Ramsons');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (133,'Ajo de oso','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (139,'Sinapis alba',7204,'139.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (139,'White mustard');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (139,'Mostaza blanca','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (140,'Brassica juncea',7204,'140.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (140,'Mustard greens');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (140,'Mostaza china','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (141,'Brassica nigra',7204,'141.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (141,'Black mustard');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (141,'Mostaza negra','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (174,'Brassica oleracea var. acephala',7204,'174.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (174,'Kale');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (174,'Col crespa','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (143,'Cicer arietinum',7205,'143.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (143,'Chickpea');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (143,'Garbanzo','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (145,'Lupinus',7205,'145.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (145,'Lupinus');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (145,'Altramuz Azul','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (146,'Trigonella foenum-graecum',7205,'146.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (146,'Fenugreek');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (146,'Fenogreco','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (147,'Medicago sativa',7205,'147.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (147,'Alfalfa');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (148,'Lens culinaris',7205,'148.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (148,'Lentil');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (148,'Lenteja','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (144,'Vicia Sativa',7205,'144.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (144,'Vetch');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (144,'Veza','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (179,'Arachis hypogaea',7205,'179.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (179,'Peanut');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (131,'Carthamus tinctorius',7207,'131.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (131,'Safflower');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (131,'Cártamo','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (137,'Artemisia dracunculus',7207,'137.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (137,'Tarragon');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (137,'Estragón','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (180,'Helianthus tuberosus',7207,'180.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (180,'Jerusalem artichoke');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (180,'','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (156,'Panicum miliaceum',7208,'156.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (156,'Millet');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (156,'Mijo','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (157,'Secale cereale',7208,'157.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (157,'Rye');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (157,'Centeno','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (158,'Hordeum vulgare',7208,'158.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (158,'Barley');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (158,'Cebada','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (159,'Avena sativa',7208,'159.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (159,'Oat');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (159,'Avena','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (160,'Triticum aestivum',7208,'160.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (160,'Wheat');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (160,'Trigo','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (161,'Oryza sativa',7208,'161.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (161,'Rice');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (161,'Arroz','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (134,'Atriplex hortensis',7209,'134.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (134,'Orache');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (134,'Ajo de Oso','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (130,'Fagopyrum esculentum',7210,'130.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (130,'Buckwheat');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (130,'Alforfón / Trigo Sarraceno','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (138,'Myosotis sylvatica',7214,'138.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (138,'Forget-me-not');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (138,'Nomeolvides','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (151,'Melissa officinalis',7215,'151.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (151,'Lemon balm');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (151,'Melisa','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (152,'Origanum majorana',7215,'152.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (152,'Marjoram');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (152,'Mejorana','es');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (142,'Humulus lupulus',7217,'142.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (142,'Hop');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (142,'Lúpulo','es');

