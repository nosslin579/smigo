package org.smigo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.RegisterFormBean;
import org.smigo.user.UserBean;
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

    private static final String NON_LATIN_LETTERS = "Şehirde güzel köpek леп пас у граду 在城里漂亮的狗 سگ خوب در شهر";
    private static final String EMAIL_PROVIDER = "@mailinator.com";
    private static final String DISPLAY_NAME = "Tomte Nisse";
    private static final String PASSWORD = "password";
    private static final String HASHPW = BCrypt.hashpw(PASSWORD, BCrypt.gensalt(4));
    private static final String NEW_PASSWORD = "password1";
    private static final String SPECIES_NAME = "Frango Salada";
    private static final String SCIENTIFIC_NAME = "Frangus Saladus";
    private static final String ITEM_NAME = "Sand";
    private static final int NUMBER_OF_SPECIES = 83;

    @Autowired
    private UserDao userDao;

    private WebDriver d;
    private WebDriverWait w;


    @BeforeClass
    public void init() {
        d = new FirefoxDriver();
        d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        w = new WebDriverWait(d, 10);
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
        user.setTermsOfService(true);
        user.setPassword(HASHPW);
        userDao.addUser(user, HASHPW, 0);
        return username;
    }

    private void login(String username, String password) throws InterruptedException {
        d.findElement(By.id("login-link")).click();
        d.findElement(By.name("username")).clear();
        d.findElement(By.name("username")).sendKeys(username);
        d.findElement(By.name("password")).clear();
        d.findElement(By.name("password")).sendKeys(password);
        d.findElement(By.id("submit-login-register-form")).click();
        Thread.sleep(2000);
    }

    @Test
    public void register() throws Exception {
        final String username = "selenium" + System.currentTimeMillis();

        //add default species
        d.findElement(By.className("square")).click();

        //sign up
        d.findElement(By.id("register-link")).click();
        d.findElement(By.name("username")).clear();
        d.findElement(By.name("username")).sendKeys(username);
        d.findElement(By.name("password")).clear();
        d.findElement(By.name("password")).sendKeys(PASSWORD);
        d.findElement(By.name("passwordagain")).clear();
        d.findElement(By.name("passwordagain")).sendKeys(PASSWORD);
        d.findElement(By.name("termsOfService")).click();
        d.findElement(By.id("submit-login-register-form")).click();

        w.until(ExpectedConditions.presenceOfElementLocated(By.className("plant")));
        List<WebElement> src = d.findElements(By.className("plant"));
        Assert.assertEquals(src.size(), 1);


        UserBean user = userDao.getUser(username);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), username);
    }

    @Test
    public void addSpecies() throws InterruptedException {
        final String username = addUser();
        final String speciesName = SPECIES_NAME + System.currentTimeMillis();
        login(username, PASSWORD);
        //add species
        d.findElement(By.id("species-frame")).findElement(By.tagName("input")).sendKeys(speciesName);
        d.findElement(By.id("add-species-link")).click();
        d.findElement(By.linkText(speciesName)).click();

        d.findElement(By.className("square")).click();
        Thread.sleep(2000);

        d.findElement(By.id("logout-link")).click();
        login(username, PASSWORD);

        List<WebElement> plants = d.findElements(By.className("plant"));
        Assert.assertEquals(plants.size(), 1, "No plants found! User:" + username);
        Assert.assertEquals(plants.iterator().next().getAttribute("alt"), speciesName);

    }

    @Test
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

    @Test(enabled = false)
    public void changePassword() throws InterruptedException {
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

    @Test(enabled = false)
    public void resetPassword() throws InterruptedException {
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

    @Test
    public void registerWithOpenId() throws InterruptedException {
        userDao.deleteOpenId("https://www.google.com/accounts/o8/id?id=AItOawk7toFbMCzMKq-beo_Rjbo-QASKPaX1tBo");

        d.get("http://localhost:8080/web");
        d.findElement(By.className("square")).click();
        Thread.sleep(2000);

        d.findElement(By.id("login-link")).click();
        d.findElement(By.id("googleOpenId")).submit();
        d.findElement(By.id("Email")).sendKeys("smigo.org@gmail.com");
        d.findElement(By.id("Passwd")).sendKeys("lstN09LLrZZx");
        d.findElement(By.id("signIn")).click();
        d.findElement(By.id("accept-terms-of-service-button")).click();

        w.until(ExpectedConditions.presenceOfElementLocated(By.className("plant")));
        Assert.assertTrue(d.findElement(By.className("plant")).getAttribute("src").endsWith("species/1.png"));
        Assert.assertEquals(d.findElements(By.id("logout-link")).size(), 1);

        d.findElement(By.id("logout-link")).click();
        d.findElement(By.id("login-link")).click();
        d.findElement(By.id("googleOpenId")).submit();
        Assert.assertTrue(d.findElement(By.className("plant")).getAttribute("src").endsWith("species/1.png"));

    }

    @Test
    public void updateAccount() throws InterruptedException {
        final String username = addUser();
        login(username, PASSWORD);

        d.findElement(By.id("account-link")).click();

        Assert.assertTrue(d.getPageSource().contains(username));

        d.findElement(By.name("email")).sendKeys(username + EMAIL_PROVIDER);
        d.findElement(By.name("displayName")).sendKeys(DISPLAY_NAME);
        new Select(d.findElement(By.name("locale"))).selectByValue("sv");
        d.findElement(By.name("about")).sendKeys(NON_LATIN_LETTERS);
        d.findElement(By.id("submit-account-button")).click();

        w.until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success")));

        d.findElement(By.id("logout-link")).click();
        login(username, PASSWORD);

        d.findElement(By.id("account-link")).click();

        Thread.sleep(1000);

        Assert.assertEquals(d.findElement(By.name("email")).getAttribute("value"), username + EMAIL_PROVIDER);
        Assert.assertEquals(d.findElement(By.name("displayName")).getAttribute("value"), DISPLAY_NAME);
        Assert.assertEquals(new Select(d.findElement(By.name("locale"))).getFirstSelectedOption().getAttribute("value"), "sv");
        Assert.assertEquals(d.findElement(By.name("about")).getAttribute("value"), NON_LATIN_LETTERS);
    }
}
