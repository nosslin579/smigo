package org.smigo.config;

public interface Props {
    String getResetUrl();

    default boolean isDev() {
        return false;
    }
}
