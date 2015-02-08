CREATE TABLE UserConnection (userId         VARCHAR(255) NOT NULL,
                             providerId     VARCHAR(255) NOT NULL,
                             providerUserId VARCHAR(255),
                             rank           INT          NOT NULL,
                             displayName    VARCHAR(255),
                             profileUrl     VARCHAR(512),
                             imageUrl       VARCHAR(512),
                             accessToken    VARCHAR(255) NOT NULL,
                             secret         VARCHAR(255),
                             refreshToken   VARCHAR(255),
                             expireTime     BIGINT,
  PRIMARY KEY (userId, providerId, providerUserId));
CREATE UNIQUE INDEX UserConnectionRank ON UserConnection (userId, providerId, rank);



INSERT INTO families(id,name) VALUES (7223,'Grossulariaceae');
INSERT INTO families(id,name) VALUES (7224,'Ericaceae');
INSERT INTO families(id,name) VALUES (7225,'Ericaceae');
INSERT INTO families(id,name) VALUES (7226,'Iridaceae');
INSERT INTO families(id,name) VALUES (7227,'Liliaceae');
INSERT INTO families(id,name) VALUES (7228,'Papaveraceae');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (127,'Ribes uva-crispa',7223,'127.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (127,'Gooseberry');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (127,'Uva Crispa','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (127,'Stachelbeeren','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (149,'Ribes rubrum',7223,'149.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (149,'Redcurrant');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (149,'Grosella Roja','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (149,'Rote Johannisbeere','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (128,'Vaccinium myrtillus',7224,'128.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (128,'Blueberry');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (128,'Arándano','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (128,'Heidelbeeren','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (129,'Abelmoschus esculentus',7225,'129.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (129,'Okra');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (129,'Okra','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (129,'Okra','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (132,'Allium cepa',7202,'132.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (132,'Shallot');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (132,'Chalota','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (132,'Schalotte','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (150,'Crocus sativus',7226,'150.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (150,'Saffron');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (150,'Azafrán','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (150,'Saffran','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (153,'Tulipa gesneriana',7227,'153.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (153,'Tulip');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (153,'Tulipan','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (153,'Tulpe','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (154,'Gossypium',7225,'154.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (154,'Cotton');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (154,'Algodón','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (154,'Baumwolle','de');

INSERT INTO species(id,scientific_name,family_id,iconfilename) VALUES (155,'Papaver somniferum',7228,'155.png');
INSERT INTO species_translation(species_id,vernacular_name) VALUES (155,'Poppy');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (155,'Opio / Adormidera','es');
INSERT INTO species_translation(species_id,vernacular_name,language) VALUES (155,'Mohn','de');

ALTER TABLE visitlog
ADD COLUMN querystring VARCHAR(256);

