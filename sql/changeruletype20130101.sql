alter table rules add column type int not null default 0;
update rules set type = 1 where kind like "fightdisease";
update rules set type = 2 where kind like "repelpest";
update rules set type = 3 where kind like "improvesflavor";
update rules set type = 4 where kind like "badcompanion";
update rules set type = 5 where kind like "goodcroprotation";
update rules set type = 6 where kind like "badcroprotation";
update rules set type = 7 where kind like "speciesrepetition";
alter table rules drop column kind;
alter table rules modify rule_id int(11) auto_increment;
alter table rules modify displaybydefault boolean default true;

