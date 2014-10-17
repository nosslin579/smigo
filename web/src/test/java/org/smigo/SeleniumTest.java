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
import java.util.Locale;
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
        d.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
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
        return addUser(false).getUsername();
    }

    private UserBean addUser(boolean addEmail) {
        final RegisterFormBean registerFormBean = new RegisterFormBean();
        final String username = "selenium" + System.currentTimeMillis();
        registerFormBean.setUsername(username);
        registerFormBean.setTermsOfService(true);
        registerFormBean.setPassword(HASHPW);
        int id = userDao.addUser(registerFormBean, HASHPW, 0);
        UserBean user = new UserBean(username, "", username + EMAIL_PROVIDER, "", Locale.ENGLISH);
        user.setTermsOfService(true);
        if (addEmail) {
            userDao.updateUser(id, user);
        }
        return user;
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

    @Test(enabled = true)
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

    @Test(enabled = true)
    public void addSpecies() throws InterruptedException {
        final String username = addUser();
        final String speciesName = SPECIES_NAME + System.currentTimeMillis();
        login(username, PASSWORD);
        //add species
        d.findElement(By.id("species-frame")).findElement(By.tagName("input")).sendKeys(speciesName);
        d.findElement(By.id("add-species-link")).click();
        w.until(ExpectedConditions.presenceOfElementLocated(By.linkText(speciesName)));
        d.findElement(By.className("square")).click();
        Thread.sleep(5000);

        d.findElement(By.id("logout-link")).click();
        Thread.sleep(1000);
        login(username, PASSWORD);

        final WebElement plant = w.until(ExpectedConditions.presenceOfElementLocated(By.className("plant")));
        Assert.assertEquals(plant.getAttribute("alt"), speciesName);

    }

    @Test(enabled = true)
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

    @Test(enabled = true)
    public void changePassword() throws InterruptedException {
        final String username = addUser();
        login(username, PASSWORD);

        //go to account details
        d.findElement(By.id("account-link")).click();

        //change password
        d.findElement(By.name("oldPassword")).sendKeys(PASSWORD);
        d.findElement(By.name("newPassword")).sendKeys(NEW_PASSWORD);
        d.findElement(By.name("verifyPassword")).sendKeys(NEW_PASSWORD);
        d.findElement(By.id("submit-password-button")).click();
        w.until(ExpectedConditions.presenceOfElementLocated(By.className("alert-success")));
        d.findElement(By.id("logout-link")).click();

        //login again
        login(username, NEW_PASSWORD);
        Assert.assertEquals(d.findElements(By.id("logout-link")).size(), 1);
    }

    //    @Test
    @Test(enabled = true)
    public void loginWrongPassword() throws InterruptedException {
        final String username = addUser();
        login(username, "wrong password");
        WebElement element = d.findElement(By.id("bad-credentials"));
        Assert.assertTrue(element.isDisplayed());
        Assert.assertFalse(element.getText().isEmpty());
    }

    @Test(enabled = true)
    public void resetPassword() throws InterruptedException {
        final UserBean user = addUser(true);

        d.findElement(By.id("login-link")).click();

        //Reset
        d.findElement(By.id("request-password-link")).click();
        d.findElement(By.name("email")).clear();
        d.findElement(By.name("email")).sendKeys(user.getEmail());
        d.findElement(By.id("submit-request-button")).submit();
        w.until(ExpectedConditions.presenceOfElementLocated(By.className("alert-info")));

        //Check email
        d.get("http://mailinator.com/inbox.jsp?to=" + user.getUsername());
        d.findElement(By.className("message")).findElement(By.tagName("a")).click();
        d.switchTo().frame(1).findElement(By.className("mailview")).findElement(By.tagName("a")).click();

        //Set new password
        d.switchTo().window((String) d.getWindowHandles().toArray()[1]);
        d.findElement(By.name("password")).sendKeys(NEW_PASSWORD);
        d.findElement(By.tagName("button")).click();

        //login
        login(user.getUsername(), NEW_PASSWORD);
        Assert.assertEquals(d.findElements(By.id("logout-link")).size(), 1);
    }

    @Test(enabled = true)
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

    @Test(enabled = true)
    public void updateAccount() throws InterruptedException {
        final String username = addUser();
        login(username, PASSWORD);

        d.findElement(By.id("account-link")).click();

        Assert.assertEquals(d.findElements(By.tagName("input")).size(), 6);

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
