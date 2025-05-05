package ui.SelenidePOMTests;

import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObjects.selenide.HomePage;
import pageObjects.selenide.NavigationPage;

class NavigationPageTests extends BaseTestSettings {
    HomePage homePage;

    @Override
    @BeforeEach
    void setup() {
        super.setup();
        homePage = new HomePage();
    }

    @Step("Open Navigation page test")
    @Test
    void openNavigationTest() throws InterruptedException {
        homePage.open();
        NavigationPage navigationPage = homePage.openNavigationPage();

        navigationPage.checkIsNavigationPage();
    }

    @Step("Click the next button test")
    @Test
    void nextButtonTest() {
        homePage.open();
        NavigationPage navigationPage = homePage.openNavigationPage();
        int pageNumber = 3;

        for (int i = 1; i < pageNumber; i++) {
            navigationPage.clickNextButton();
        }

        navigationPage.checkIsItAnExpectedPageNumber(pageNumber);
    }
}
