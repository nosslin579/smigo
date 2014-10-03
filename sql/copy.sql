CREATE TABLE species LIKE nosslin2_db.species;
INSERT INTO species SELECT * FROM nosslin2_db.species;
SELECT * FROM species;

CREATE TABLE families LIKE nosslin2_db.families;
INSERT INTO families SELECT * FROM nosslin2_db.families;
SELECT * FROM families;

CREATE TABLE plants LIKE nosslin2_db.plants;
INSERT INTO plants SELECT * FROM nosslin2_db.plants;
SELECT * FROM plants;

CREATE TABLE rules LIKE nosslin2_db.rules;
INSERT INTO rules SELECT * FROM nosslin2_db.rules;
SELECT * FROM rules;

CREATE TABLE speciestransbylanguage LIKE nosslin2_db.speciestransbylanguage;
INSERT INTO speciestransbylanguage SELECT * FROM nosslin2_db.speciestransbylanguage;
SELECT * FROM speciestransbylanguage;

CREATE TABLE users LIKE nosslin2_db.users;
INSERT INTO users SELECT * FROM nosslin2_db.users;
SELECT * FROM users;

CREATE TABLE usersettingforrules LIKE nosslin2_db.usersettingforrules;
INSERT INTO usersettingforrules SELECT * FROM nosslin2_db.usersettingforrules;
SELECT * FROM usersettingforrules;

CREATE TABLE usersettingforspecies LIKE nosslin2_db.usersettingforspecies;
INSERT INTO usersettingforspecies SELECT * FROM nosslin2_db.usersettingforspecies;
SELECT * FROM usersettingforspecies;

CREATE TABLE visitlog LIKE nosslin2_db.visitlog;
INSERT INTO visitlog SELECT * FROM nosslin2_db.visitlog;
SELECT * FROM visitlog;