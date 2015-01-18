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
