package ui.lessonCode;

import com.codeborne.selenide.*;
import config.ITestPropertiesConfig;
import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.selenide.HomePage;
import pageObjects.selenide.WebFormPage;
import steps.AllureSteps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static constants.CommonConstants.BASE_URL;
import static constants.CommonConstants.MAIN_TITLE_EXPECTED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Код с лекции
 */
class SelenideTests {
    ITestPropertiesConfig config = ConfigFactory.create(ITestPropertiesConfig.class, System.getProperties());
    AllureSteps allureSteps = new AllureSteps();

    @BeforeEach
    void setup() {
        if (!config.isRemote()) {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        }
        initDriver();
    }

    // AfterEach для закрытия браузера нам не нужен, т.к. в селениде он закрывается автоматически

    @Test
    void openHomepageTest() {
        // в Selenide драйвер инициализирован по умолчанию, поэтому мы не обращаемся к нему для открытия страницы
        open(BASE_URL);

        // Просто для практики после открытия страницы, проверим url и title
        assertAll(
                () -> assertEquals(MAIN_TITLE_EXPECTED, title()),
                () -> assertEquals(BASE_URL, url())
        );
    }

    // Другие тесты из проекта преподавателя:
    @Test
    void successfulLoginTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/login-form.html");

        SelenideElement subTitle = $(By.className("display-6"));
        WebElement loginInput = $("#username");
        WebElement passwordInput = $("#password");
        WebElement submitButton = $(By.xpath("//button[@type='submit']"));

        loginInput.sendKeys("user");
        passwordInput.sendKeys("user");
        String textBeforeClick = subTitle.getText();
        submitButton.click();

        assertThat(textBeforeClick).isEqualTo("Login form");
        subTitle.shouldHave(text("Login form"));
        WebElement successMessage = $("#success");
        assertThat(successMessage.isDisplayed()).isTrue();
    }

    @Test
    void openSiteTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/");
        assertEquals("Hands-On Selenium WebDriver with Java", title());
    }

    @Test
    void openForm() {
        open("https://bonigarcia.dev/selenium-webdriver-java/");
        WebElement webFormButton = $(By.xpath("//div[@class = 'card-body']")).find(By.xpath(".//a[contains(@class, 'btn')]"));
        webFormButton.click();
        SelenideElement actualH1 = $(By.xpath("//h1[@class='display-6']"));
        actualH1.shouldHave(text("Web form"));
    }

    @Test
    @DisplayName("Check screenshot attachment")
    void infiniteScrollTestWithAttach() throws InterruptedException, IOException {
        open("https://bonigarcia.dev/selenium-webdriver-java/infinite-scroll.html");
        // Пример перехода от Selenide к Selenium:
        WebDriver driver = Selenide.webdriver().object();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        By pLocator = By.tagName("p");
        List<WebElement> paragraphs = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, 0));
        int initParagraphsNumber = paragraphs.size();

        WebElement lastParagraph = driver.findElement(By.xpath(String.format("//p[%d]", initParagraphsNumber)));
        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, lastParagraph);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, initParagraphsNumber));
        Thread.sleep(3000);
        allureSteps.captureScreenshotSelenide();
        allureSteps.captureScreenshotSelenideSpoiler();
    }

    @Test
    void loadingImagesDefaultWaitTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/loading-images.html");

        // Пример теста, который мы ранее писали на Selenium, где ждали, когда загрузятся 4 картинки
        // Так как мы по умолчанию в Selenium ждем 4 секунды, для компаса нам не нужно писать никакой дополнительный код
        // * здесь означает, что слева и справа могут быть какие-то символы
        $("#compass").shouldHave(attributeMatching("src", ".*compass.*"));
    }

    @Test
    void loadingImagesWithUpdatedTimeoutWaitTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/loading-images.html");
        // Как нам ждать больше? Можно увеличить дефолтный таймаут:
        Configuration.timeout = 10_000;
        // Если поставим больше, он не будет ждать и завершится, когда тест выполнится. Но если не выполнится, то придется ждать
        // Т.е. это аналог неявного ожидания из Selenium
        $("#landscape").shouldHave(attributeMatching("src", ".*landscape.*"));
    }

    @Test
    void loadingImagesWithExplicitTimeoutWaitTest() {
        open("https://bonigarcia.dev/selenium-webdriver-java/loading-images.html");

        // Сохраняем элементы в коллекцию, фильтруя, что они должны быть видимые
        ElementsCollection images = $$("img").filter(Condition.visible);
        // И затем проверяем, что их должно быть 4 штуки
        // Аналог явного ожидания:
        images.shouldHave(size(4), Duration.ofSeconds(10));
    }

    @Test
    void pageObjectTest() {
        HomePage homePage = new HomePage();
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();
        webFormPage.submitForm();

        Assertions.assertThat(url()).contains("https://bonigarcia.dev/selenium-webdriver-java/submitted-form.html");
    }

    private void initDriver() {
        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            Allure.addAttachment("remote", remoteUrl);
            ChromeOptions options = new ChromeOptions();
            options.addArguments(
                    "--headless",
                    "--disable-gpu",
                    "--no-sandbox",
                    "--disable-dev-shm-usage"
            );
            options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));

            try {
                WebDriver driver = new RemoteWebDriver(new URL(remoteUrl), options);
                WebDriverRunner.setWebDriver(driver); // 🔑 Ключевое исправление
                driver.manage().window().maximize();
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL for Selenium", e);
            }
        }
    }

//    // Selenium code:
//    private void initDriver() {
//        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
//        if (remoteUrl != null) {
//            if (!remoteUrl.isEmpty()) {
//                WebDriver driver;
//                Allure.addAttachment("remote", remoteUrl);
//                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--headless");  // Add headless mode
//                options.addArguments("--disable-gpu"); // Switch off GPU, because we don't need it in headless mode
//                options.addArguments("--no-sandbox"); // Switch off sandbox to prevent access rights issues
//                options.addArguments("--disable-dev-shm-usage"); // Use /tmp instead of /dev/shm
//                options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));
//                try {
//                    driver = new RemoteWebDriver(new URL(remoteUrl), options);
//                } catch (MalformedURLException e) {
//                    throw new RuntimeException("Malformed URL for Selenium Remote WebDriver", e);
//                }
//                driver.manage().window().maximize();
//            }
//        } else {
////            Selenide настраивает драйвер по умолчанию, поэтому здесь нам это не нужно
////            driver = new ChromeDriver();
//        }
////        driver.manage().window().maximize();
//    }
}
