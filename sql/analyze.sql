SHOW PROCESSLIST;

SELECT
  users.username,
  user_id,
  regtime,
  decidetime,
  users.createdate,
  requests,
  sessions,
  count(IF(year = 2013, 1, NULL)) AS y2013,
  count(IF(year = 2014, 1, NULL)) AS y2014,
  count(IF(year = 2015, 1, NULL)) AS y2015
FROM users
  LEFT JOIN plants ON plants.fkuserid = users.user_id
  LEFT JOIN (SELECT
               username,
               count(*)                  AS requests,
               count(DISTINCT sessionid) AS sessions
             FROM visitlog
             GROUP BY username) AS r ON r.username = users.username
GROUP BY user_id
ORDER BY user_id DESC;


SELECT
  *
FROM (SELECT
        user_id
      FROM users
      WHERE user_id NOT IN (
        SELECT
          fkuserid
        FROM plants
          JOIN species ON plants.species = species.species_id
        WHERE item = TRUE OR annual = FALSE
        GROUP BY fkuserid)) sinners JOIN (SELECT
                                            fkuserid,
                                            count(*) AS no
                                          FROM plants
                                          GROUP BY fkuserid) noofplants ON sinners.user_id = noofplants.fkuserid;

