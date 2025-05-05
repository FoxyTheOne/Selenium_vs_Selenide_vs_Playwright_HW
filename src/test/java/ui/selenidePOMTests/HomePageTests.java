package ui.selenidePOMTests;

import constants.CommonConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObjects.selenide.HomePage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomePageTests extends BaseTestSettings {
    HomePage homePage;

    @Override
    @BeforeEach
    void setup() {
        super.setup();
        homePage = new HomePage();
    }

    @Test
    void openHomePageTest() {
        homePage.open();
        String actualTitle = homePage.getWebTitle();

        assertEquals(CommonConstants.MAIN_TITLE_EXPECTED, actualTitle);
    }
}
