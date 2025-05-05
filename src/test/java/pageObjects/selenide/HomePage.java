package pageObjects.selenide;

import com.codeborne.selenide.Selenide;
import constants.CommonConstants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;

public class HomePage extends BasePage {
    @Step("Open homepage")
    public void open() {
        Selenide.open(CommonConstants.BASE_URL);
    }

    @Step("Get web site title")
    public String getWebTitle() {
        return title();
    }

    @Step("Open Web form page")
    public WebFormPage openWebFormPage() {
        $(By.linkText("Web form")).click();
        return new WebFormPage();
    }

    @Step("Open Navigation page")
    public NavigationPage openNavigationPage() {
        $(By.linkText("Navigation")).click();
        return new NavigationPage();
    }
}
