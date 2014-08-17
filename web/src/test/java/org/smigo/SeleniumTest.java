package org.smigo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.RegisterFormBean;
import org.smigo.user.User;
import org.smigo.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Test(groups = "selenium")
@ContextConfiguration(classes = {TestConfiguration.class})
public class SeleniumTest extends AbstractTestNGSpringContextTests {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String EMAIL_PROVIDER = "@mailinator.com";
    private static final String PASSWORD = "password";
    private static final String HASHPW = BCrypt.hashpw(PASSWORD, BCrypt.gensalt(4));
    private static final String NEW_PASSWORD = "password1";
    private static final String SPECIES_NAME = "Frango";
    private static final String SCIENTIFIC_NAME = "Frangus Saladus";
    private static final String ITEM_NAME = "Sand";
    private static final int NUMBER_OF_SPECIES = 83;

    @Autowired
    private UserDao userDao;

    private WebDriver d;
    private WebDriverWait w;
    private Actions a;


    @BeforeClass
    public void init() {
        d = new FirefoxDriver();
        d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        w = new WebDriverWait(d, 10);
        a = new Actions(d);
    }

    @BeforeMethod
    public void goHome() {
        d.manage().deleteCookieNamed("JSESSIONID");
        d.manage().deleteCookieNamed("remember-me");
        d.get("http://localhost:8080/web");
    }

    @AfterClass
    public void destruct() {
        d.quit();
    }

    private String addUser() {
        final RegisterFormBean user = new RegisterFormBean();
        final String username = "selenium" + System.currentTimeMillis();
        user.setUsername(username);
        user.setPassword(HASHPW);
//        user.setEmail(username + EMAIL_PROVIDER);
        userDao.addUser(user, HASHPW, 0, 0);
        return username;
    }

    private void login(String username, String password) {
        d.findElement(By.id("login-link")).click();
        d.findElement(By.name("username")).sendKeys(username);
        d.findElement(By.name("password")).sendKeys(password);
        d.findElement(By.id("submit-login-register-form")).click();
    }

    public void register() throws Exception {
        final String username = "selenium" + System.currentTimeMillis();

        //add default species
        d.findElement(By.className("square")).click();

        //sign up
        d.findElement(By.id("register-link")).click();
        d.findElement(By.name("username")).sendKeys(username);
        d.findElement(By.name("password")).sendKeys(PASSWORD);
        d.findElement(By.name("passwordagain")).sendKeys(PASSWORD);
        d.findElement(By.name("termsOfService")).click();
        d.findElement(By.id("submit-login-register-form")).click();

        List<WebElement> src = d.findElements(By.className("square"));
        Assert.assertEquals(src.size(), 1);


        User user = userDao.getUserByUsername(username);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), username);
    }

    private void clickGrid() {
        WebElement grid = d.findElement(By.className("square"));
        a.moveToElement(grid).moveByOffset(48, 48).click().perform();
    }

    @Test(enabled = false)
    public void addSpecies() {
        final String username = addUser();
        login(username, PASSWORD);
        //add species
        d.findElement(By.id("plants-menu-item")).click();
        Assert.assertEquals(d.findElements(By.className("speciesrow")).size(), NUMBER_OF_SPECIES);
        d.findElement(By.id("add-species-link")).click();
        d.findElement(By.name("vernacularName")).sendKeys(SPECIES_NAME);
        d.findElement(By.name("scientificName")).sendKeys(SCIENTIFIC_NAME);
        new Select(d.findElement(By.name("family"))).selectByIndex(2);
        d.findElement(By.id("submit-speciesform-button")).click();

        //plant new species
        d.findElement(By.id("garden-menu-item")).click();
        d.findElement(By.xpath("//div[contains(text(), '" + SPECIES_NAME + "')]")).click();
        d.findElement(By.id("origo")).click();
        d.findElement(By.id("savebutton")).click();
        w.until(ExpectedConditions.textToBePresentInElementLocated(By.id("userdialog"), "Ok"));

        d.navigate().refresh();
        Assert.assertEquals(d.findElements(By.className("speciesimage")).size(), 1);
    }

    public void addYear() {
        //add default species
        d.findElement(By.className("square")).click();

        //add year
        d.findElement(By.id("add-year-link")).click();
        d.findElement(By.id("add-forward-year-button")).click();

        Assert.assertEquals(d.findElements(By.className("visible-remainder")).size(), 1);
        Assert.assertEquals(d.findElement(By.id("garden-frame")).findElements(By.tagName("li")).size(), 3);
        Assert.assertEquals(d.findElements(By.className("plant")).size(), 0);

        d.findElement(By.partialLinkText("Lawn")).click();
        d.findElement(By.className("visible-remainder")).click();

        //add year
        d.findElement(By.id("add-year-link")).click();
        d.findElement(By.id("add-forward-year-button")).click();

        Assert.assertEquals(d.findElements(By.className("visible-remainder")).size(), 1);
        Assert.assertEquals(d.findElement(By.id("garden-frame")).findElements(By.tagName("li")).size(), 4);
        Assert.assertEquals(d.findElements(By.className("plant")).size(), 1);

    }

    public void changePassword() {
        final String username = addUser();
        login(username, PASSWORD);

        //go to account details
        d.findElement(By.id("account-details-link")).click();

        //change password
        d.findElement(By.id("edit-password-link")).click();
        d.findElement(By.name("oldPassword")).sendKeys(PASSWORD);
        d.findElement(By.name("newPassword")).sendKeys(NEW_PASSWORD);
        d.findElement(By.id("passwordagain")).sendKeys(NEW_PASSWORD);
        d.findElement(By.id("submit-password-button")).click();
        d.findElement(By.id("logout-link")).click();


        //login again
        login(username, NEW_PASSWORD);
        log.info("Url after login:" + d.getCurrentUrl());
        Assert.assertEquals(d.getCurrentUrl(), "http://localhost:8080/web/garden");
        Assert.assertEquals(d.findElement(By.id("account-details-link")).getText(), username);
    }

    public void resetPassword() {
        final String username = addUser();
        final String email = username + EMAIL_PROVIDER;

        //Reset
        d.get("http://localhost:8080/web/reset-password");
        d.findElement(By.name("email")).sendKeys(email);
        d.findElement(By.tagName("form")).submit();

        //Check email
        d.get("http://mailinator.com/inbox.jsp?to=" + username);
        d.findElements(By.className("subject")).get(1).click();
        final String link = d.switchTo().frame(1).findElement(By.className("mailview")).findElement(By.tagName("a")).getText();
        d.get(link);

        //Set new password
        d.findElement(By.name("newPassword")).sendKeys(NEW_PASSWORD);
        d.findElement(By.id("passwordagain")).sendKeys(NEW_PASSWORD);
        d.findElement(By.tagName("form")).submit();

        //Logout and login
        d.findElement(By.id("logout-link")).click();
        login(username, NEW_PASSWORD);
        Assert.assertEquals(d.findElement(By.id("account-details-link")).getText(), username);
    }

    public void loginOpenId() {
        d.get("http://localhost:8080/web/login");
        d.findElement(By.id("googleOpenId")).submit();
        d.findElement(By.id("Email")).sendKeys("smigo.org@gmail.com");
        d.findElement(By.id("Passwd")).sendKeys("lstN09LLrZZx");
        d.findElement(By.id("signIn")).click();
        Assert.assertEquals(d.findElements(By.id("logout-link")).size(), 1);
    }
}
