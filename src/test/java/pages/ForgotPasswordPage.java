package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class ForgotPasswordPage {

    // Локатор кнопки "Войти" на странице восстановления пароля
    private final SelenideElement loginLink = $("a.Auth_link__1fOlj[href='/login']");

    @Step("Нажать 'Войти' на странице восстановления пароля")
    public void clickLoginLink() {
        loginLink.click();
    }
}
