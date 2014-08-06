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
