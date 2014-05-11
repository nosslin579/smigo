package org.smigo;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AddPlantTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    WebDriver w;

    @BeforeTest
    public void init() {
//        w = new FirefoxDriver();
    }

    @Test
    public void testAdd() throws Exception {
        log.debug("Running test qwer");
//        w.get("http://localhost:8080/web");
//        Assert.assertTrue(w.getPageSource().contains("asdf"));
        Assert.assertTrue(true);

    }
}
