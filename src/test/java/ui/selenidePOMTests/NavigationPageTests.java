package ui.selenidePOMTests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObjects.selenide.HomePage;
import pageObjects.selenide.NavigationPage;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NavigationPageTests extends BaseTestSettings {
    HomePage homePage;
    NavigationPage navigationPage;

    @Override
    @BeforeEach
    void setup() {
        super.setup();
        homePage = new HomePage();
        homePage.open();
        navigationPage = homePage.openNavigationPage();
        Configuration.timeout = 5000;
    }

    @Step("Open Navigation page test")
    @Test
    void openNavigationTest() throws InterruptedException {
//        homePage.open();
//        NavigationPage navigationPage = homePage.openNavigationPage();

        navigationPage.checkIsNavigationPage();
    }

    /**
     * Проверка поведения по умолчанию при открытии страницы:
     * Проверяем, что элемент Previous имеет класс 'disabled';
     * Проверяем, что элемент страницы 1 имеет класс 'active';
     * Проверяем, что элемент Next не имеет класса 'disabled'.
     */
    @Step("Default check: if previous button is disabled, is it page 1 and is next button active")
    @Test
    void CheckDefaultBehaviour() {
        SelenideElement previousButton = navigationPage.getPreviousButton();
        SelenideElement activePageButton = navigationPage.getActivePage();
        SelenideElement nextButton = navigationPage.getNextButton();

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    previousButton.parent().shouldHave(cssClass("disabled"));
                }),
                () -> assertDoesNotThrow(() -> {
                    activePageButton.shouldHave(text("1"));
                }),
                () -> assertDoesNotThrow(() -> {
                    nextButton.shouldNotHave(cssClass("disabled"));
                })
        );
    }

    /**
     * Проверка переходов на другие страницы:
     * - Кликаем на Next.
     * - Проверяем, что Previous активен (нет класса 'disabled').
     * - Проверяем, что текущая страница теперь активна (например, страница 2).
     * - Кликаем на Previous.
     * - Проверяем, что Previous снова 'disabled' и активна страница 1.
     */
    @Step("Check if previous button is active on the 2 page and is it disabled when we return back on 1 page")
    @Test
    void testForwardBackwardNavigation() {
        // Переход вперед
        navigationPage.getNextButton().click();
        Selenide.sleep(500); // Даем время на загрузку

        // Проверяем состояние на второй странице
        SelenideElement previousButton = navigationPage.getPreviousButton();
        SelenideElement activePageButton = navigationPage.getActivePage();

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    previousButton.parent().shouldNotHave(cssClass("disabled"));
                }),
                () -> assertDoesNotThrow(() -> {
                    activePageButton.shouldHave(text("2"));
                })
        );

        // Возврат назад
        previousButton.click();
        Selenide.sleep(500);

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    previousButton.parent().shouldHave(cssClass("disabled"));
                }),
                () -> assertDoesNotThrow(() -> {
                    activePageButton.shouldHave(text("1"));
                })
        );
    }

    /**
     * Тест на исправность Next:
     * - Определяем общее количество страниц.
     * - Нажимаем Next (количество страниц - 1) раз.
     * - Проверяем, что Next теперь 'disabled'.
     */
    @Step("Check is next button disabled on the last page")
    @Test
    void testNextButtonFunctionality() {
        int totalPages = navigationPage.getPaginationItems().size() - 2;// Исключаем Previous и Next
        SelenideElement nextButton = navigationPage.getNextButton();
        SelenideElement activePageButton = navigationPage.getActivePage();

        assertAll(
                // Проходим все страницы
                () -> assertDoesNotThrow(() -> {
                    for (int i = 1; i < totalPages; i++) {
                        nextButton.click();
                        Selenide.sleep(500);
                        activePageButton.shouldHave(text(String.valueOf(i + 1)));
                    }
                }),
                // Проверяем состояние Next на последней странице
                () -> assertDoesNotThrow(() -> {
                    nextButton.shouldHave(cssClass("disabled"));
                })
        );
    }

//    @Step("Click the next button test")
//    @ParameterizedTest
//    @ValueSource(ints = {3, 2, 1})
//    void nextButtonTest(int pageNumber) {
////        homePage.open();
////        NavigationPage navigationPage = homePage.openNavigationPage();
//
//        for (int i = 1; i < pageNumber; i++) {
//            navigationPage.clickNextButton();
//        }
//
//        navigationPage.checkIsItAnExpectedPageNumber(pageNumber);
//    }
}
