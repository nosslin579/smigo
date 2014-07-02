package org.smigo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Test(groups = "selenium")
public class SeleniumTest {

    public static final String USERNAME = "selenium" + System.currentTimeMillis();
    public static final String EMAIL = USERNAME + "@smigo.org";
    public static final String PASSWORD = "password";
    public static final String DISPLAY_NAME = "Seleniumsson";
    public static final String ABOUT = "Eco friendly garden";
    public static final String ADD = "1";
    private static final String NEW_PASSWORD = "drowssap";
    public static final String SPECIES_NAME = "Frango";
    public static final String SCIENTIFIC_NAME = "Frangus Saladus";
    private static final String PERENNIAL_NAME = "Sand";
    public static final int NUMBER_OF_SPECIES = 83;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private WebDriver d;
    private WebDriverWait w;

    @BeforeTest
    public void init() {
        d = new FirefoxDriver();
        d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        d.manage().deleteCookieNamed("jsessionid");
        w = new WebDriverWait(d, 10);
    }

    @AfterClass
    public void destruct() {
        d.quit();
    }

    @Test
    public void testRegister() throws Exception {
        log.debug("Running test qwer");
        d.get("http://localhost:8080/web");

        //add tomato
        d.findElement(By.id("jslistspeciesid_44")).click();
        d.findElement(By.id("origo")).click();
        d.findElement(By.id("savebutton")).click();

        //sign up
        d.findElement(By.id("signup-link")).click();
        d.findElement(By.name("username")).sendKeys(USERNAME);
        d.findElement(By.name("password")).sendKeys(PASSWORD);
        d.findElement(By.name("passwordagain")).sendKeys(PASSWORD);
        d.findElement(By.name("email")).sendKeys(EMAIL);
        d.findElement(By.name("displayname")).sendKeys(DISPLAY_NAME);
        d.findElement(By.name("about")).sendKeys(ABOUT);
        d.findElement(By.name("termsofservice")).click();
        Thread.sleep(500);//todo this is a workaround for a bug
        d.findElement(By.id("submit-userform-button")).click();

        //login
        d.findElement(By.name("j_username")).sendKeys(USERNAME);
        d.findElement(By.name("j_password")).sendKeys(PASSWORD);
        d.findElement(By.id("submit-loginform-form")).click();

        String src = d.findElement(By.cssSelector("#origo .speciesimage")).getAttribute("src");
        Assert.assertEquals(src, "http://localhost:8080/pic/28.png");

        //add species
        d.findElement(By.id("plants-menu-item")).click();
        Assert.assertEquals(d.findElements(By.className("speciesrow")).size(), NUMBER_OF_SPECIES);
        d.findElement(By.id("add-species-link")).click();
        d.findElement(By.name("vernacularName")).sendKeys(SPECIES_NAME);
        d.findElement(By.name("scientificName")).sendKeys(SCIENTIFIC_NAME);
        d.findElement(By.name("scientificName")).sendKeys(SCIENTIFIC_NAME);
        new Select(d.findElement(By.name("family"))).selectByIndex(2);
        d.findElement(By.id("submit-speciesform-button")).click();

        //plant new species
        d.findElement(By.id("garden-menu-item")).click();
        d.findElement(By.xpath("//div[contains(text(), '" + SPECIES_NAME + "')]")).click();
        d.findElement(By.id("origo")).click();

        //put concrete anywhere in grid
        d.findElement(By.xpath("//div[contains(text(), '" + PERENNIAL_NAME + "')]")).click();
        d.findElement(By.className("jsgridcell")).click();
        d.findElement(By.id("savebutton")).click();
        w.until(ExpectedConditions.textToBePresentInElementLocated(By.id("userdialog"), "Ok"));

        int numberOfPlantsThisYear = d.findElements(By.className("speciesimage")).size();
        Assert.assertEquals(numberOfPlantsThisYear, 3);

        //add year
        d.findElement(By.id("add-year-link")).click();
        d.findElement(By.name("year")).sendKeys(Calendar.getInstance().get(Calendar.YEAR) + 1 + "");
        d.findElement(By.id("addYearFormBean")).submit();

        int numberOfPlantsNextYear = d.findElements(By.className("speciesimage")).size();
        Assert.assertEquals(numberOfPlantsNextYear, 1);


        //go to account details
        d.findElement(By.id("account-details-link")).click();
        Assert.assertTrue(d.getPageSource().contains(EMAIL));
        Assert.assertTrue(d.getPageSource().contains(DISPLAY_NAME));
        Assert.assertTrue(d.getPageSource().contains(ABOUT));

        //change password
        d.findElement(By.id("edit-password-link")).click();
        d.findElement(By.name("oldPassword")).sendKeys(PASSWORD);
        d.findElement(By.name("newPassword")).sendKeys(NEW_PASSWORD);
        d.findElement(By.name("newPasswordAgain")).sendKeys(NEW_PASSWORD);
        d.findElement(By.id("submit-password-button")).click();
        d.findElement(By.id("logout-link")).click();


        //login again
        d.findElement(By.id("login-link")).click();
        d.findElement(By.name("j_username")).sendKeys(USERNAME);
        d.findElement(By.name("j_password")).sendKeys(NEW_PASSWORD);
        d.findElement(By.id("submit-loginform-form")).click();
        log.info("Url after login:" + d.getCurrentUrl());
        Assert.assertEquals(d.getCurrentUrl(), "http://localhost:8080/web/garden");

        int numberOfPlantsAfterSessionRefresh = d.findElements(By.className("speciesimage")).size();
        Assert.assertEquals(numberOfPlantsAfterSessionRefresh, 1);
    }
}
