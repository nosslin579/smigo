package org.smigo;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SandboxTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public void testAdd() throws Exception {
        Class<?>[] classes = getClass().getClasses();
        Class<? extends ClassLoader> aClass = getClass().getClassLoader().getClass();
        Assert.assertTrue(true);

    }
}
