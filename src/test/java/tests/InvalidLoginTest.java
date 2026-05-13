package tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pageobjects.LoginPage;
import utils.BrowserType;
import utils.DriverFactory;
import utils.EnvReader;
import utils.InvalidPasswordGenerator;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvalidLoginTest extends BaseTest {

    @ParameterizedTest(name = "Invalid login on {0}")
    @EnumSource(BrowserType.class)
    void loginWithInvalidPassword(BrowserType browser) {
        EnvReader env = EnvReader.load(Path.of(".env"));
        String username = env.get("KETKEREKEN_USERNAME");
        String password = env.get("KETKEREKEN_PASSWORD");
        boolean headless = Boolean.parseBoolean(env.getOrDefault("HEADLESS", "true"));

        driver = DriverFactory.createDriver(browser, headless);
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login(username, InvalidPasswordGenerator.from(password));
        assertTrue(loginPage.isLoginErrorVisible(), "Error message should be visible after invalid login on " + browser);
    }
}