package utils;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final Path SCREENSHOT_DIR = Paths.get("build", "reports", "tests", "test", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        Optional<Object> testInstance = context.getTestInstance();
        Optional<WebDriver> driver = testInstance.flatMap(this::findWebDriver);

        driver.ifPresent(webDriver -> {
            if (webDriver instanceof TakesScreenshot) {
                takeScreenshot((TakesScreenshot) webDriver, context);
            }
        });
    }

    private Optional<WebDriver> findWebDriver(Object testInstance) {
        Class<?> clazz = testInstance.getClass();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (WebDriver.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(testInstance);
                        if (value instanceof WebDriver) {
                            return Optional.of((WebDriver) value);
                        }
                    } catch (IllegalAccessException e) {
                        // ignore and continue
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return Optional.empty();
    }

    private void takeScreenshot(TakesScreenshot driver, ExtensionContext context) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);

            String testClass = context.getRequiredTestClass().getSimpleName();
            String method = context.getRequiredTestMethod().getName();
            String platform = sanitize(context.getDisplayName());
            String timestamp = LocalDateTime.now().format(TIMESTAMP);

            String fileName = String.format("%s_%s_%s_%s.png", testClass, method, platform, timestamp);
            Path destination = SCREENSHOT_DIR.resolve(fileName);

            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), destination);
            System.out.println("Saved screenshot on failure to: " + destination.toAbsolutePath());
        } catch (IOException | RuntimeException e) {
            System.err.println("Failed to save failure screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "_");
    }
}