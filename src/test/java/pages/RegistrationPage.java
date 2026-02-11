package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RegistrationPage {

    //поле имени
    private final SelenideElement nameInput = $x("//label[text()='Имя']/following::input[1]");

    //поле почты
    private final SelenideElement emailInput = $x("//label[text()='Email']/following::input[1]");

    //поле пароля
    private final SelenideElement passwordInput = $x("//label[text()='Пароль']/following::input[1]");

    //кнопка регистрации
    private final SelenideElement registerButton = $x("//button[text()='Зарегистрироваться']");

    //логин
    private final SelenideElement loginLink = $("a.Auth_link__1fOlj[href='/login']");

    //ошибка поля пароля (менее 6 символов)
    private final SelenideElement passwordError = $("p.input__error.text_type_main-default");


    @Step("Ввести имя")
    public void setName(String name) {
        nameInput.setValue(name);
    }

    @Step("Ввести почту")
    public void setEmail(String email) {
        emailInput.setValue(email);
    }

    @Step("Ввести пароль")
    public void setPassword(String password) {
        passwordInput.setValue(password);
    }

    @Step("Нажать кнопку регистрации")
    public void clickRegisterButton() {
        registerButton.click();
    }

    @Step("Нажать кнопку авторизации (форма регистрации)")
    public void clickLoginLink() {
        loginLink.click();
    }

    public void register(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
        clickRegisterButton();
    }

    // Метод проверки видимости ошибки пароля
    public boolean isPasswordErrorDisplayed() {
        return passwordError.isDisplayed();
    }

}
