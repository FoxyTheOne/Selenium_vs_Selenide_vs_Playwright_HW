package ui.SelenidePOMTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObjects.selenide.HomePage;
import pageObjects.selenide.WebFormPage;

class WebFormPageTests extends BaseTestSettings {
    HomePage homePage;

    @Override
    @BeforeEach
    void setup() {
        super.setup();
        homePage = new HomePage();
    }

    @Test
    void openWebFormTest() {
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();

        webFormPage.checkIsWebPage();
    }

    @Test
    void submitFormTest() throws InterruptedException {
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();

        webFormPage.submitForm();

        webFormPage.checkIsFormSubmitted();
    }
}
