package org.smigo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test(groups = "selenium")
public class StartSmigoSeleniumTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    WebDriver w;

    @BeforeTest
    public void init() {
        w = new FirefoxDriver();
    }

    @AfterTest
    public void destory(){
        w.quit();
    }

    @Test(groups = "selenium")
    public void testAdd() throws Exception {
        w.get("http://localhost:8080/web");
        Assert.assertTrue(w.getPageSource().contains("smigo"),w.getPageSource());
    }
}
