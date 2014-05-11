package org.smigo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LogbackLearningTest {

  @Test
  public void testHelloWorld() {
    org.slf4j.impl.StaticLoggerBinder s;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.debug("Hello world.");
    Assert.assertTrue(true);
  }
}
