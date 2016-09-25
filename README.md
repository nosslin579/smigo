# Smigo
## A vegetable garden planner


* A simple single page web application
* Design and plan your vegetable garden and share with friends.
* Get advice for crop rotation and companion planting.
* Based on square foot gardening.
* Available at [smigo.org](http://smigo.org)

## Up and running

1. Download [Kga](https://sourceforge.net/projects/kitchengarden) and install to local repo using `mvn install:install-file -Dfile=KitchenGardenAid.1.8.1.jar -DgroupId=kga -DartifactId=kga -Dversion=1.8.1 -Dpackaging=jar`
2. Setup a H2 database and create tables using [ddl-db.sql](https://github.com/nosslin579/smigo/blob/master/ddl-db.sql)
3. Create file local.properties(TODO rename to test.properties) in test resource folder to be able to run tests. Since the tests have dependecies on production data, my recommendation is to disable all tests.
4. Create file dev.properties in main resource folder to be able to run development environment
5. Create file prod.properties in main resource folder to be able to run production environment
6. Add these values to one or more .properties files and edit where appropriate:
``` 
databaseUser=sa
databasePassword=
databaseUrl=jdbc:h2:file:~/db/smigo;AUTO_SERVER=TRUE;IFEXISTS=TRUE
#Obtained from facebook. Can be empty but then facebook login not possible.
facebookAppSecret=
#Obtained from facebook. Can be empty but then facebook login not possible.
facebookAppId=
#In dev this can be anything. In prod this is the login to the mailserver.
mailSenderUsername=smigo@smigo.org
#Only used in prod
mailSenderPassword=s3cret
#Only used in prod
mailSenderHost=mail-provider-url.com
#Only used in prod
mailSenderPort=465
#In dev this can be anything. In prod this is the email all errors are sent.
notifierEmail=christian1195@gmail.com
#Backup directory for db. In dev set to OS temp dir.
sqlDirectory=/tmp
#Message reload period
resourceCachePeriod=1
#The userId of admin account. Insert user to DB before first launch.
welcomeCommentSubmitter=1
```

Build with `mvn clean install` and add war file to Tomcat(or equivalent). Start with `-DsmigoProfile=dev` and optionally also `-Dlogback.configurationFile=~/smigo/logback-dev.xml`
