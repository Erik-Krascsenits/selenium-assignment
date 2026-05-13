package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import utils.FileDownloader;

public class LoginPage {

    private static final String PAGE_URL = "https://www.ketkereken.hu/fiokom/";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(name = "login")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(PAGE_URL);
        wait.until(ExpectedConditions.visibilityOf(usernameInput));
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(usernameInput)).clear();
        usernameInput.sendKeys(username);

        wait.until(ExpectedConditions.elementToBeClickable(passwordInput)).clear();
        passwordInput.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public boolean isLoginFormVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("label[for='username']")
        )).isDisplayed();
    }

    public boolean isLoginErrorVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.woocommerce-error li")
        )).isDisplayed();
    }

    public boolean isDisplayNameVisible(String displayName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[@class='name' and normalize-space()='" + displayName + "']")
        )).isDisplayed();
    }

    public void downloadAvatar(Path destination) throws IOException {
        WebElement avatar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("img.avatar")
        ));
        FileDownloader.download(avatar.getAttribute("src"), destination);
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.logout"))).click();
    }

    public boolean isSessionCookieValid(String cookieName, String wpUsername) {
        Cookie cookie = driver.manage().getCookieNamed(cookieName);
        return cookie != null && cookie.getValue().startsWith(wpUsername + "%7C");
    }
}