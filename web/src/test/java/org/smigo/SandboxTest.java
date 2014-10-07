package org.smigo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class SandboxTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void testAdd() throws Exception {
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        Assert.assertTrue(true);

    }
}
