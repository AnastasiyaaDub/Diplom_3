import config.BaseElements;
import config.TestsBase;
import data.UserApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import pages.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends TestsBase {

    private LoginPage loginPage;
    private MainPage mainPage;


    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        // Предусловие: создаем пользователя через API
        createdUser = UserApi.createUserForUITest();

        loginPage = new LoginPage();
        mainPage = new MainPage();

        // Открываем браузер
        open(BaseElements.BASE_URL);

    }

    @Test
    @DisplayName("Логин по кнопке 'Войти в аккаунт' на главной")
    void loginViaMainPageButton() {

        //На главной странице нажимаем "Войти в аккаунт"
        mainPage.clickloginButtonMain();

        //Ожидаем загрузки страницы логина
        loginPage.checkPageLoaded();

        //Заполняем форму логина
        loginPage.login(createdUser.getEmail(), createdUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();


        webdriver().shouldHave(urlContaining("/"), Duration.ofSeconds(5));
        //Ожидаем успешного входа (редирект на главную)
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("login"),
                "После логина должны быть на главной странице");

        //Можно проверить переход в личный кабинет
        mainPage.goToProfile();
        String profileUrl = webdriver().driver().url();
        assertTrue(profileUrl.contains("account"), "Должны попасть в Личный кабинет");
    }

    @Test
    @DisplayName("Логин через кнопку 'Личный Кабинет'")
    void loginViaPersonalAccountButton() {
        //Нажимаем "Личный Кабинет"
        mainPage.goToProfile();

        webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));
        assertTrue(webdriver().driver().url().contains("login"),
                "Должны перейти на страницу логина");

        //Логинимся через LoginPage
        loginPage.checkPageLoaded();
        loginPage.login(createdUser.getEmail(), createdUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход - переход в ЛК
        mainPage.goToProfile();
        webdriver().shouldHave(urlContaining("account"), Duration.ofSeconds(5));
        assertTrue(webdriver().driver().url().contains("account"),
                "После логина должны быть в Личном кабинете");
    }

    @Test
    @DisplayName("Логин через страницу регистрации")
    void loginFromRegistrationPage() {

        RegistrationPage registrationPage = new RegistrationPage();

        //Переходим на страницу регистрации
        mainPage.goToProfile();
        loginPage.clickRegisterLink();

        //Нажимаем "Войти" (ссылка на странице регистрации)
        registrationPage.clickLoginLink();

        webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));
        assertTrue(webdriver().driver().url().contains("login"),
                "Должны вернуться на страницу логина");

        //Логинимся
        loginPage.login(createdUser.getEmail(), createdUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход
        mainPage.goToProfile();
        webdriver().shouldHave(urlContaining("account"), Duration.ofSeconds(5));
        assertTrue(webdriver().driver().url().contains("account"),
                "После логина должны быть в ЛК");
    }

    @Test
    @DisplayName("Логин через страницу восстановления пароля")
    void loginFromPasswordRecoveryPage() {

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();

        //Переходим на страницу восстановления пароля
        mainPage.goToProfile();
        loginPage.clickForgotPasswordLink();

        //Нажимаем "Войти" (ссылка на странице восстановления)
        forgotPasswordPage.clickLoginLink();

        //Логинимся
        loginPage.login(createdUser.getEmail(), createdUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход
        mainPage.goToProfile();

        webdriver().shouldHave(urlContaining("account"), Duration.ofSeconds(5));
        assertTrue(webdriver().driver().url().contains("account"),
                "После логина должны быть в ЛК");
    }

}
