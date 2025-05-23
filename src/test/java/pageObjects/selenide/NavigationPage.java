package pageObjects.selenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import constants.CommonConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static constants.CommonConstants.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NavigationPage extends BasePage {
    private static final String NAVIGATION_URL = "navigation1.html";

    @Step("Get expected page url")
    public String getNavigationExpectedUrlEnding() {
        return NAVIGATION_URL;
    }

    @Step("Click next button")
    public void clickNextButton() {
        $(By.linkText("Next")).click();
    }

    @Step("Check that the page is Navigation")
    public void checkIsNavigationPage() {
        assertAll(
                () -> assertEquals(BASE_URL + getNavigationExpectedUrlEnding(), url()),
                () -> assertEquals(CommonConstants.MAIN_TITLE_EXPECTED, title()),
                () -> assertEquals("Navigation example", getActualSubtitle())
        );
    }

    @Step("Find Previous button element")
    public SelenideElement getPreviousButton() {
        return $(".page-link").shouldHave(text("Previous"));
    }

//    @Step("Find Page button element")
//    public SelenideElement getPageByNumber(int number) {
//        return $x(String.format("//li[@class='page-item']/a[text()='%d']", number));
//    }

    @Step("Find Active page button element")
    public SelenideElement getActivePage() {
        return $("li.page-item.active a.page-link");
    }

    @Step("Find Next button element")
    public SelenideElement getNextButton() {
//        return $(".page-link").shouldHave(text("Next"));

        ElementsCollection pageLinks = $$("ul.pagination li.page-item");

//        boolean elementFound = false;
//
//        for (SelenideElement pageButton : pageLinks) {
//            try {
//                pageButton.shouldHave(text("Next"));
//                elementFound = true;
//            } catch (InvalidSelectorException | UnhandledAlertException | ElementNotFound | ElementShouldNot |
//                     ElementShould e) {
//                System.out.println(e.getMessage());
//            }
//
//            if (elementFound) {
//                return pageButton;
//            }
//        }
//
//        return null;

        return pageLinks.last();
    }

    @Step("Counting number of buttons (pages + next + previous buttons)")
    public ElementsCollection getPaginationItems() {
        return $$("ul.pagination li.page-item");
    }

//    @Step("Check the page button number")
//    public void checkIsItAnExpectedPageNumber(int pageNumber) {
//        String expectedPageNumberString = String.format("%s", pageNumber);
//        // Формируем XPath с явным указанием на текст и активное состояние
//        String activePageXpath = String.format(
//                "//li[contains(@class, 'active')]//a[contains(normalize-space(), '%d')]",
//                pageNumber
//        );
//
//        // Используем $x() для XPath и проверяем элемент
//        SelenideElement activePageNumberButton = $x(activePageXpath)
//                .as("Active page link")
//                .shouldBe(visible)
//                .shouldHave(
//                        attribute("href",
//                                CommonConstants.BASE_URL + "navigation" +
//                                        pageNumber +
//                                        ".html"
//                        )
//                );
//
//        assertEquals(expectedPageNumberString, activePageNumberButton.getText());
//    }
}
