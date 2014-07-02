ALTER TABLE visitlog
MODIFY COLUMN note varchar (255);

ALTER TABLE users
DROP COLUMN registrationdate;