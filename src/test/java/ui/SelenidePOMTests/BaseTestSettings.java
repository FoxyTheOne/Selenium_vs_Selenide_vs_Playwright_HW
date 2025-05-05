package ui.SelenidePOMTests;

import config.ITestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;

public class BaseTestSettings {
    ITestPropertiesConfig config = ConfigFactory.create(ITestPropertiesConfig.class, System.getProperties());

    @BeforeEach
    void setup() {
        if (!config.isRemote()) {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        }
    }
}
