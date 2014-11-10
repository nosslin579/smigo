ALTER TABLE plants
CHANGE fkuserid user_id INT NOT NULL;

ALTER TABLE plants
CHANGE species species_id INT NOT NULL;

ALTER TABLE species
CHANGE family family_id INT;

ALTER TABLE species
CHANGE name scientific_name VARCHAR(255);

UPDATE species
SET family_id = NULL
WHERE family_id = 7200;

DELETE FROM families
WHERE id = 7200;

ALTER TABLE species AUTO_INCREMENT=10200;
