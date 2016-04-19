System properties:
-Dlogback.configurationFile=web/logback-dev.xml


If Tomcat then context.xml should contain these:

   <Environment name="profile" value="dev" type="java.lang.String" override="false"/>
   <Environment name="mailSenderHost" value="" type="java.lang.String" override="false"/>
   <Environment name="mailSenderUsername" value="" type="java.lang.String" override="false"/>
   <Environment name="mailSenderPassword" value="" type="java.lang.String" override="false"/>
   <Environment name="mailSenderPort" value="" type="java.lang.String" override="false"/>

   <Environment name="facebookAppId" value="" type="java.lang.String" override="false"/>
   <Environment name="facebookAppSecret" value="" type="java.lang.String" override="false"/>

    <Environment name="databaseUser" value="sa" type="java.lang.String" override="false"/>
   <Environment name="databasePassword" value="123" type="java.lang.String" override="false"/>
   <Environment name="databaseUrl" value="jdbc:h2:file:~/db/dev;AUTO_SERVER=TRUE" type="java.lang.String" override="false"/>



Merge species
UPDATE PLANTS
SET SPECIES_ID = 1
WHERE SPECIES_ID IN (2,3,4);

UPDATE VARIETIES
SET SPECIES_ID = 1
WHERE SPECIES_ID IN (2,3,4);

DELETE FROM SPECIES_TRANSLATION
WHERE SPECIES_ID IN (2,3,4);

DELETE FROM SPECIES
WHERE ID IN (2,3,4);
