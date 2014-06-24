package org.smigo.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LogbackLearningTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testHelloWorld() {
        logger.trace("Hello trace.");

        logger.debug("Hello debug.");
        logger.info("Hello nfo.");
        logger.warn("Hello warn.");
        logger.error("Hello error.");
        System.out.println("sout");
        System.err.println("serr");
        Assert.assertTrue(true);
    }
}
