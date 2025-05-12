package ui.selenidePOMTests;

import com.codeborne.selenide.Configuration;
import config.ITestPropertiesConfig;
import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
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

//    Selenide Configuration class contains public static MutableCapabilities browserCapabilities which is used by the driver for browser startup, if provided.
//
//    For Chrome:
//
//    ChromeOptions options = new ChromeOptions()
//            .setHeadless(true)
//            .addArguments("--lang=en_US");
//    Configuration.browserCapabilities = options;
//
//    But note --lang argument might be ignored on Linux.
//
//    For Firefox:
//
//    FirefoxProfile profile = new FirefoxProfile();
//profile.setPreference("intl.accept_languages", "en-US");
//    FirefoxOptions options = new FirefoxOptions()
//            .setHeadless(true)
//            .setProfile(profile);
//    Configuration.browserCapabilities = options;

    // Selenium code:
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
        } else {
//            Selenide настраивает драйвер по умолчанию, поэтому здесь нам это не нужно
//            driver = new ChromeDriver();
        }
//        driver.manage().window().maximize();
    }
}
