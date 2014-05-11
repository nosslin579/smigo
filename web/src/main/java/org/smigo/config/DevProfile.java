package org.smigo.config;

import org.smigo.config.EnvironmentProfile;
import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile(EnvironmentProfile.DEVELOPMENT)
public @interface DevProfile {

}
