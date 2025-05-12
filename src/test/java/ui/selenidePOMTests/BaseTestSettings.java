package ui.selenidePOMTests;

import com.codeborne.selenide.Configuration;
import config.ITestPropertiesConfig;
import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

public class BaseTestSettings {
    ITestPropertiesConfig config = ConfigFactory.create(ITestPropertiesConfig.class, System.getProperties());

    @BeforeEach
    void setup() {
        if (!config.isRemote()) {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        }
        initDriver();
    }

    private void initDriver() {
        String remoteUrl = System.getenv("SELENIDE_REMOTE_URL");
        if (remoteUrl != null) {
            if (!remoteUrl.isEmpty()) {
                Allure.addAttachment("remote", remoteUrl);
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");  // Add headless mode
                options.addArguments("--disable-gpu"); // Switch off GPU, because we don't need it in headless mode
                options.addArguments("--no-sandbox"); // Switch off sandbox to prevent access rights issues
                options.addArguments("--disable-dev-shm-usage"); // Use /tmp instead of /dev/shm
                options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));

                Configuration.remote = remoteUrl;
                Configuration.browserCapabilities = options;
            }
        }
    }
}
