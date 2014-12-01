DROP TABLE families;
CREATE TABLE families LIKE nosslin2_db.families;
INSERT INTO families SELECT * FROM nosslin2_db.families;

DROP TABLE openid;
CREATE TABLE openid LIKE nosslin2_db.openid;
INSERT INTO openid SELECT * FROM nosslin2_db.openid;

DROP TABLE persistent_logins;
CREATE TABLE persistent_logins LIKE nosslin2_db.persistent_logins;

DROP TABLE plants;
CREATE TABLE plants LIKE nosslin2_db.plants;
INSERT INTO plants SELECT * FROM nosslin2_db.plants;

DROP TABLE rules;
CREATE TABLE rules LIKE nosslin2_db.rules;
INSERT INTO rules SELECT * FROM nosslin2_db.rules;

DROP TABLE species;
CREATE TABLE species LIKE nosslin2_db.species;
INSERT INTO species SELECT * FROM nosslin2_db.species;
ALTER TABLE species AUTO_INCREMENT=30200;


DROP TABLE species_translation;
CREATE TABLE species_translation LIKE nosslin2_db.species_translation;
INSERT INTO species_translation SELECT * FROM nosslin2_db.species_translation;

DROP TABLE users;
CREATE TABLE users LIKE nosslin2_db.users;
INSERT INTO users SELECT * FROM nosslin2_db.users;

DROP TABLE rules_x_impacts;
CREATE TABLE rules_x_impacts LIKE nosslin2_db.rules_x_impacts;
INSERT INTO rules_x_impacts SELECT * FROM nosslin2_db.rules_x_impacts;

DROP TABLE impacts;
CREATE TABLE impacts LIKE nosslin2_db.impacts;
INSERT INTO impacts SELECT * FROM nosslin2_db.impacts;

DROP TABLE visitlog;
CREATE TABLE visitlog LIKE nosslin2_db.visitlog;