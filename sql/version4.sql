ALTER TABLE visitlog
MODIFY COLUMN note varchar (255);

ALTER TABLE users
DROP COLUMN registrationdate;

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