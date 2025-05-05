package pageObjects.selenide;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class BasePage {
    @Step("Get actual subtitle")
    public String getActualSubtitle() {
        return $(By.className("display-6")).getText();
    }
}
