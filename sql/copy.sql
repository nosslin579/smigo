CREATE TABLE families LIKE nosslin2_db.families;
INSERT INTO families SELECT * FROM nosslin2_db.families;

CREATE TABLE openid LIKE nosslin2_db.openid;
INSERT INTO openid SELECT * FROM nosslin2_db.openid;

CREATE TABLE persistent_logins LIKE nosslin2_db.persistent_logins;

CREATE TABLE plants LIKE nosslin2_db.plants;
INSERT INTO plants SELECT * FROM nosslin2_db.plants;

CREATE TABLE rules LIKE nosslin2_db.rules;
INSERT INTO rules SELECT * FROM nosslin2_db.rules;

CREATE TABLE species LIKE nosslin2_db.species;
INSERT INTO species SELECT * FROM nosslin2_db.species;

CREATE TABLE speciestransbylanguage LIKE nosslin2_db.speciestransbylanguage;
INSERT INTO speciestransbylanguage SELECT * FROM nosslin2_db.speciestransbylanguage;

CREATE TABLE users LIKE nosslin2_db.users;
INSERT INTO users SELECT * FROM nosslin2_db.users;

CREATE TABLE usersettingforspecies LIKE nosslin2_db.usersettingforspecies;
INSERT INTO usersettingforspecies SELECT * FROM nosslin2_db.usersettingforspecies;

CREATE TABLE visitlog LIKE nosslin2_db.visitlog;