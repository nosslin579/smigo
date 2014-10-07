DROP TABLE usersettingforspecies;
DROP TABLE speciestransbylanguage;

ALTER TABLE visitlog
ADD COLUMN host VARCHAR(255);
