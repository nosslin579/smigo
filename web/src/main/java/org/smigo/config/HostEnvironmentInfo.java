package org.smigo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostEnvironmentInfo {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String env;
    private boolean debug;
    private String plantPicDirectory;

    public HostEnvironmentInfo(String env, boolean debug, String plantPicDirectory) {
        this.debug = debug;
        this.env = env;
        this.plantPicDirectory = plantPicDirectory;
    }

    public String getEnv() {
        return env;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getPlantPicDirectory() {
        return plantPicDirectory;
    }
}
