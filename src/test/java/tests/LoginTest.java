package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.WebDriver;
import pageobjects.LoginPage;
import utils.BrowserType;
import utils.DriverFactory;
import utils.EnvReader;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    private WebDriver driver;

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest(name = "Login and logout on {0}")
    @EnumSource(BrowserType.class)
    void loginAndLogout(BrowserType browser) {
        EnvReader env = EnvReader.load(Path.of(".env"));
        String username = env.get("KETKEREKEN_USERNAME");
        String password = env.get("KETKEREKEN_PASSWORD");
        String displayName = env.get("KETKEREKEN_DISPLAY_NAME");
        boolean headless = Boolean.parseBoolean(env.getOrDefault("HEADLESS", "true"));

        driver = DriverFactory.createDriver(browser, headless);
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        assertTrue(loginPage.isLoginFormVisible(), "Login form should be visible before login");

        loginPage.login(username, password);
        assertTrue(loginPage.isDisplayNameVisible(displayName), "Display name '" + displayName + "' should be visible after login on " + browser);

        loginPage.logout();
        assertTrue(loginPage.isLoginFormVisible(), "Login form should be visible again after logout on " + browser);
    }
}