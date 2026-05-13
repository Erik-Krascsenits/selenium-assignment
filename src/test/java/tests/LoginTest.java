package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pageobjects.LoginPage;
import utils.EnvReader;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private EnvReader env;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        env = EnvReader.load(Path.of(".env"));
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void loginWithEnvCredentials() {
        String username = env.get("KETKEREKEN_USERNAME");
        String password = env.get("KETKEREKEN_PASSWORD");
        String displayName = env.get("KETKEREKEN_DISPLAY_NAME");

        assertNotNull(username, "KETKEREKEN_USERNAME must be defined in .env");
        assertNotNull(password, "KETKEREKEN_PASSWORD must be defined in .env");
        assertNotNull(displayName, "KETKEREKEN_DISPLAY_NAME must be defined in .env");

        loginPage.open();
        assertTrue(loginPage.isLoginFormVisible(), "Login form should be visible on the page");

        loginPage.login(username, password);
        assertTrue(loginPage.isDisplayNameVisible(displayName), "The display name should be visible after login");

        loginPage.logout();
        assertTrue(loginPage.isLoginLinkVisible(), "The Bejelentkezés/Fiókom link should appear after logout");
    }
}
