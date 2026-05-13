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

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    public boolean isDisplayNameVisible(String displayName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[@class='name' and normalize-space()='" + displayName + "']")
        )).isDisplayed();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.logout"))).click();
    }
}