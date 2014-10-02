ALTER TABLE visitlog
MODIFY COLUMN note VARCHAR(255);

ALTER TABLE users
DROP COLUMN registrationdate;

ALTER TABLE users
DROP COLUMN authority;

ALTER TABLE users
DROP COLUMN publicgarden;

ALTER TABLE users
DROP COLUMN location;

ALTER TABLE users
CHANGE user_id id INT NOT NULL AUTO_INCREMENT;

CREATE TABLE persistent_logins (
  username  VARCHAR(64) NOT NULL,
  series    VARCHAR(64) NOT NULL,
  token     VARCHAR(64) NOT NULL,
  last_used TIMESTAMP   NOT NULL,
  PRIMARY KEY (series)
);

CREATE TABLE openid (
  id           INT AUTO_INCREMENT,
  identity_url VARCHAR(255) NOT NULL,
  user_id      INT          NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE messages;

UPDATE species
SET annual = FALSE
WHERE item = TRUE;

ALTER TABLE users
ADD COLUMN termsofservice BOOL DEFAULT FALSE;

UPDATE users
SET termsofservice = TRUE;

ALTER TABLE visitlog
DROP COLUMN locale;

ALTER TABLE visitlog
DROP COLUMN localeport;

ALTER TABLE visitlog
DROP COLUMN servername;

ALTER TABLE visitlog
DROP COLUMN validsessionid;

ALTER TABLE visitlog
DROP COLUMN sessionexists;

ALTER TABLE visitlog
DROP COLUMN sessionidfromurl;

ALTER TABLE visitlog
DROP COLUMN sessionidfromcookie;

ALTER TABLE visitlog
ADD COLUMN referer VARCHAR(256);

ALTER TABLE visitlog
ADD COLUMN origin VARCHAR(256);

ALTER TABLE species
ADD COLUMN vernacularname VARCHAR(128);

UPDATE species
  JOIN usersettingforspecies ON species.species_id = usersettingforspecies.species
SET species.vernacularname = usersettingforspecies.translation
WHERE species.creator = usersettingforspecies.user;

UPDATE species
  JOIN usersettingforspecies ON species.species_id = usersettingforspecies.species
SET species.iconfilename = usersettingforspecies.iconfilename
WHERE species.creator = usersettingforspecies.user;


ALTER TABLE visitlog
ADD COLUMN httpstatus INT;

ALTER TABLE visitlog
ADD COLUMN sessionage INT;