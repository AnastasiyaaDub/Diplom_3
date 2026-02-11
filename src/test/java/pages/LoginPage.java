package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;


public class LoginPage {

    //поле ввода почты
    private final SelenideElement emailInput = $("input[name='name']");

    //поле ввода пароля
   private final SelenideElement passwordInput = $("input[type='password']");

    //Кнопка "Войти" в форме авторизации (в личном кабинете)
    private final SelenideElement loginFormButton = $x("//button[text()='Войти']");

    //восстановление пароля
    private final SelenideElement forgotPasswordLink = $("a[href='/forgot-password']");

    // кнопка "Зарегистрироваться"
    private final SelenideElement registerLink = $("a.Auth_link__1fOlj[href='/register']");


    @Step("Ввести email")
    public void setEmail(String email) {
        emailInput.shouldBe(visible, Duration.ofSeconds(5))
                .setValue(email);
    }


    @Step("Ввести пароль")
    public void setPassword(String password) {
        passwordInput.shouldBe(visible, Duration.ofSeconds(5))
                .setValue(password);
    }

    @Step("Нажать 'Войти' в форме 'Личный кабинет'")
    public void clickLoginButton() {
        loginFormButton.click();
    }

    @Step("Нажать 'Зарегистрироваться'")
    public void clickRegisterLink() {
        registerLink.click();

    }

    @Step("Кнопка восстановления пароля")
    public void clickForgotPasswordLink() {
        forgotPasswordLink.click();
    }


    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

}
