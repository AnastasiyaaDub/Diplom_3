import config.BaseElements;
import config.TestsBase;
import data.UserApi;
import data.UserUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Keys;
import pages.*;
import java.time.Duration;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LoginTest extends TestsBase {

    private UserUI testUser;
    private LoginPage loginPage;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        // Предусловие: создаем пользователя через API
        testUser = UserApi.createUserForUITest();

        loginPage = new LoginPage();
        mainPage = new MainPage();

        // Открываем браузер
        open(BaseElements.BASE_URL);

    }


    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Логин по кнопке 'Войти в аккаунт' на главной")
    void loginViaMainPageButton(String browser) {
        setupBrowser(browser);

        //На главной странице нажимаем "Войти в аккаунт"
        mainPage.clickloginButtonMain();

        //Ожидаем загрузки страницы логина
        $("h2").shouldHave(text("Вход"));

        //Заполняем форму логина
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Ожидаем успешного входа (редирект на главную)
        webdriver().shouldHave(urlContaining("/"), Duration.ofSeconds(5));

        //Можно проверить переход в личный кабинет
        mainPage.goToProfile();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Логин через кнопку 'Личный Кабинет'")
    void loginViaPersonalAccountButton(String browser) {
        setupBrowser(browser);

        //Нажимаем "Личный Кабинет"
        mainPage.goToProfile();

        //Проверяем редирект на страницу логина
        webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));
        $("h2").shouldHave(text("Вход"));

        //Логинимся через LoginPage
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход - переход в ЛК
        mainPage.goToProfile();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Логин через страницу регистрации")
    void loginFromRegistrationPage(String browser) {
        setupBrowser(browser);
        RegistrationPage registrationPage = new RegistrationPage();

        //Переходим на страницу регистрации
        mainPage.goToProfile();
        loginPage.clickRegisterLink();

        //Нажимаем "Войти" (ссылка на странице регистрации)
        registrationPage.clickLoginLink();

        //Проверяем переход на страницу логина
       webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));

        //Логинимся
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход
        mainPage.goToProfile();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Логин через страницу восстановления пароля")
    void loginFromPasswordRecoveryPage(String browser) {
        setupBrowser(browser);
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();

        //Переходим на страницу восстановления пароля
        mainPage.goToProfile();
        loginPage.clickForgotPasswordLink();

        //Нажимаем "Войти" (ссылка на странице восстановления)
        forgotPasswordPage.clickLoginLink();

        //Логинимся
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();

        //Проверяем успешный вход
        mainPage.goToProfile();
    }

    @AfterEach
    void tearDown() {
        // Постусловие: удаляем пользователя
        if (testUser != null && testUser.hasAccessToken()) {
            UserApi.deleteUser(testUser);
        }
    }
}
