package org.smigo.user.springsocial;

import org.smigo.user.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.web.context.request.NativeWebRequest;

import javax.sql.DataSource;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {


    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserHandler userHandler;
    @Value("${postSignInUrl}")
    private String postSignInUrl;
    @Value("${facebookAppId}")
    private String facebookAppId;
    @Value("${facebookAppSecret}")
    private String facebookAppSecret;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(facebookAppId, facebookAppSecret));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new UserIdSource() {
            @Override
            public String getUserId() {
                throw new UnsupportedOperationException("getUserIdSource not supported");
            }
        };
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        final JdbcUsersConnectionRepository ret = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        ret.setConnectionSignUp(new AddUserConnectionSignUp(userHandler));
        return ret;
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
        final ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new SignInAdapter() {
            @Override
            public String signIn(String userId, Connection<?> connection, NativeWebRequest nativeWebRequest) {
                return null;
            }
        });
        providerSignInController.setSignUpUrl("/accept-termsofservice");
        providerSignInController.setSignInUrl("/login");
        providerSignInController.setPostSignInUrl(postSignInUrl);
        return providerSignInController;
    }
}