SHOW PROCESSLIST;

SELECT
  users.username,
  user_id,
  regtime,
  decidetime,
  users.createdate,
  requests,
  sessions,
  count(IF(year = 2012, 1, NULL)) AS y2012,
  count(IF(year = 2013, 1, NULL)) AS y2013,
  count(IF(year = 2014, 1, NULL)) AS y2014
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
