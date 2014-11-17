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
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (139,'Mostaza Blanca','es');