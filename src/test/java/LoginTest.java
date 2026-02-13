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

public class LoginTest extends TestsBase {

    private LoginPage loginPage;
    private MainPage mainPage;
    private ProfilePage profilePage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        // Предусловие: создаем пользователя через API
        createdUser = UserApi.createUserForUITest();

        loginPage = new LoginPage();
        mainPage = new MainPage();
        profilePage = new ProfilePage();

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

        //Ожидаем успешного входа (редирект на главную)
        webdriver().shouldHave(urlContaining("/"), Duration.ofSeconds(5));

        //Можно проверить переход в личный кабинет
        mainPage.goToProfile();
        profilePage.shouldBeLoaded();
    }

    @Test
    @DisplayName("Логин через кнопку 'Личный Кабинет'")
    void loginViaPersonalAccountButton() {
        //Нажимаем "Личный Кабинет"
        mainPage.goToProfile();

        //Проверяем редирект на страницу логина
        webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));
        loginPage.checkPageLoaded();

        //Логинимся через LoginPage
        loginPage.login(createdUser.getEmail(), createdUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход - переход в ЛК
        mainPage.goToProfile();
        profilePage.shouldBeLoaded();
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

        //Проверяем переход на страницу логина
       webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));

        //Логинимся
        loginPage.login(createdUser.getEmail(), createdUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход
        mainPage.goToProfile();
        profilePage.shouldBeLoaded();
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
        profilePage.shouldBeLoaded();
    }

}
