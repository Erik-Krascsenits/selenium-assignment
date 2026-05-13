package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private static final String PAGE_URL = "https://www.ketkereken.hu/fiokom/";
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(name = "login")
    private WebElement loginButton;

    @FindBy(css = "form.woocommerce-form-login")
    private WebElement loginForm;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(PAGE_URL);
        wait.until(ExpectedConditions.visibilityOf(loginForm));
    }

    public boolean isLoginFormVisible() {
        return loginForm.isDisplayed();
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(usernameInput)).clear();
        usernameInput.sendKeys(username);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    public boolean isDisplayNameVisible(String displayName) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//p[@class='name' and normalize-space()=\'" + displayName + "\']"),
                displayName
        ));
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.logout"))).click();
    }

    public boolean isLoginLinkVisible() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='https://ketkereken.hu/fiokom' and normalize-space()='Bejelentkezés/Fiókom']")
        )).isDisplayed();
    }

    public boolean isLoggedIn() {
        return wait.until(driver -> {
            if (!driver.getCurrentUrl().contains("/fiokom/")) {
                return true;
            }
            return !driver.findElements(By.cssSelector("a[href*='logout'], a.logout, .woocommerce-MyAccount-content, .account")).isEmpty();
        });
    }
}
