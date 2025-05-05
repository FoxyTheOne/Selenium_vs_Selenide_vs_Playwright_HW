package pageObjects.selenide;

import constants.CommonConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.*;

public class WebFormPage extends BasePage {
    private static final String WEB_FORM_URL = "web-form.html";

    @Step("Get expected page url")
    public String getWebFormExpectedUrlEnding() {
        return WEB_FORM_URL;
    }

    @Step("Get actual subtitle")
    public String getActualSubtitle() {
        return $(By.className("display-6")).getText();
    }

    @Step("Submit form")
    public void submitForm() {
        $(By.xpath("//button[text() = 'Submit']")).click();
    }

    @Step("Check that the page is Web form")
    public void checkIsWebPage() {
        assertAll(
                () -> assertEquals(CommonConstants.BASE_URL + getWebFormExpectedUrlEnding(), url()),
                () -> assertEquals(CommonConstants.MAIN_TITLE_EXPECTED, title()),
                () -> assertEquals("Web form", getActualSubtitle())
        );
    }

    @Step("Check that the form is submitted")
    public void checkIsFormSubmitted() {
        assertAll(
                () -> assertNotEquals(CommonConstants.BASE_URL + getWebFormExpectedUrlEnding(), url()),
                () -> assertEquals(CommonConstants.MAIN_TITLE_EXPECTED, title()),
                () -> assertEquals("Form submitted", getActualSubtitle())
        );
    }
}
