package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProfilePage {

    //кнопка выхода из ЛК
    private final SelenideElement logoutBotton = $x ("//button[contains(@class, 'Account_button__14Yp3') and text()='Выход']");

    //кнопка "Профиль"
    private final SelenideElement profileLink = $x("//a[text()='Профиль']");

    @Step("Нажать 'Выход' в личном кабинете")
    public void clickLogout() {
        logoutBotton.click();
    }

    @Step("Проверить что страница профиля загрузилась")
    public void shouldBeLoaded() {
        webdriver().shouldHave(urlContaining("account"));
        profileLink.shouldBe(visible)
                .shouldHave(attribute("aria-current", "page"));
    }

}
