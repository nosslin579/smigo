SHOW PROCESSLIST;

SELECT
  users.username,
  users.locale,
  id,
  regtime,
  decidetime,
  users.createdate,
  requests,
  sessions,
  count(IF(year = 2016, 1, NULL)) AS y2016,
  count(IF(year = 2015, 1, NULL)) AS y2015,
  count(IF(year = 2014, 1, NULL)) AS y2014,
  speciescreated
FROM nosslin2_db.users
  LEFT JOIN nosslin2_db.plants ON plants.user_id = users.id
  LEFT JOIN (SELECT
               username,
               count(*)                  AS requests,
               count(DISTINCT sessionid) AS sessions
             FROM nosslin2_db.visitlog
             GROUP BY username) AS r ON r.username = users.username
  LEFT JOIN (SELECT
               creator AS speciescreator,
               count(creator) AS speciescreated
             FROM nosslin2_db.species
             GROUP BY creator) AS sc ON sc.speciescreator = users.id
GROUP BY id
ORDER BY id DESC;

SELECT
  referer,count(referer)
FROM nosslin2_db.visitlog
  WHERE createdate > '2015-01-01'
GROUP BY referer
ORDER BY count(referer) DESC
LIMIT 200;

SELECT
  *
FROM nosslin2_db.visitlog
  WHERE createdate > '2015-01-01' AND httpstatus = 500
ORDER BY createdate DESC
LIMIT 200;

SELECT species.id, u.username, def.vernacular_name, numofplants
FROM nosslin2_db.species
  JOIN nosslin2_db.species_translation def ON def.species_id = species.id
  LEFT JOIN nosslin2_db.users u ON u.id = species.creator
  LEFT JOIN (SELECT
               species_id,
               count(species_id) AS numofplants
             FROM nosslin2_db.plants
             GROUP BY species_id) AS pc ON pc.species_id = species.id
GROUP BY species.id
ORDER BY species.id DESC
LIMIT 500;
