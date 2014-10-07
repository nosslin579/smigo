CREATE TABLE species LIKE nosslin2_db.species;
INSERT INTO species SELECT * FROM nosslin2_db.species;

CREATE TABLE families LIKE nosslin2_db.families;
INSERT INTO families SELECT * FROM nosslin2_db.families;

CREATE TABLE plants LIKE nosslin2_db.plants;
INSERT INTO plants SELECT * FROM nosslin2_db.plants;

CREATE TABLE rules LIKE nosslin2_db.rules;
INSERT INTO rules SELECT * FROM nosslin2_db.rules;

CREATE TABLE speciestransbylanguage LIKE nosslin2_db.speciestransbylanguage;
INSERT INTO speciestransbylanguage SELECT * FROM nosslin2_db.speciestransbylanguage;

CREATE TABLE users LIKE nosslin2_db.users;
INSERT INTO users SELECT * FROM nosslin2_db.users;

CREATE TABLE usersettingforrules LIKE nosslin2_db.usersettingforrules;
INSERT INTO usersettingforrules SELECT * FROM nosslin2_db.usersettingforrules;

CREATE TABLE usersettingforspecies LIKE nosslin2_db.usersettingforspecies;
INSERT INTO usersettingforspecies SELECT * FROM nosslin2_db.usersettingforspecies;

CREATE TABLE visitlog LIKE nosslin2_db.visitlog;
INSERT INTO visitlog SELECT * FROM nosslin2_db.visitlog;