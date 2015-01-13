SHOW PROCESSLIST;

SELECT
  users.username,
  id,
  regtime,
  decidetime,
  users.createdate,
  requests,
  sessions,
  count(IF(year = 2013, 1, NULL)) AS y2013,
  count(IF(year = 2014, 1, NULL)) AS y2014,
  count(IF(year = 2015, 1, NULL)) AS y2015
FROM nosslin2_db.users
  LEFT JOIN nosslin2_db.plants ON plants.user_id = users.id
  LEFT JOIN (SELECT
               username,
               count(*)                  AS requests,
               count(DISTINCT sessionid) AS sessions
             FROM nosslin2_db.visitlog
             GROUP BY username) AS r ON r.username = users.username
GROUP BY id
ORDER BY id DESC;