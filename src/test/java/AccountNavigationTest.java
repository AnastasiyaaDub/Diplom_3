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
import pages.LoginPage;
import pages.MainPage;
import pages.ProfilePage;
import java.time.Duration;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class AccountNavigationTest extends TestsBase {

    private UserUI testUser;
    private MainPage mainPage;
    private ProfilePage profilePage;

    @BeforeEach
    void setUp() {
        //Создаем пользователя через API
        testUser = UserApi.createUserForUITest();

        LoginPage loginPage = new LoginPage();
        mainPage = new MainPage();
        profilePage = new ProfilePage();

        open(BaseElements.BASE_URL);
        if ("yandex".equals(System.getProperty("browser", "chrome"))) {
            try {
                // Пытаемся закрыть всплывашки
                executeJavaScript(
                        "document.querySelectorAll('[role=dialog], .modal, .popup, .alert, .overlay')" +
                                ".forEach(el => el.style.display = 'none');"
                );

                // Ждем небольшое время без sleep
                $("body").shouldNotHave(cssClass("modal-open"), Duration.ofSeconds(2));

            } catch (Exception e) {
                // Логируем но не падаем
                System.out.println("Не удалось закрыть popup: " + e.getMessage());
            }
        }

        mainPage.goToProfile();
        loginPage.login(testUser.getEmail(), testUser.getPassword());
        actions().sendKeys(Keys.ESCAPE).perform();
        webdriver().shouldHave(urlContaining("/"));
    }

    @AfterEach
    void tearDown() {
        if (testUser != null && testUser.hasAccessToken()) {
            UserApi.deleteUser(testUser);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Переход в Личный кабинет после логина")
    void goToPersonalAccountAfterLogin(String browser) {
        setupBrowser(browser);

        // Кликаем "Личный кабинет"
        mainPage.goToProfile();

        // Проверяем что попали в ЛК
        webdriver().shouldHave(urlContaining("account"), Duration.ofSeconds(5));
        profilePage.shouldBeLoaded();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Переход в Конструктор из Личного кабинета")
    void goToConstructorFromPersonalAccount(String browser) {
        setupBrowser(browser);

        //Идем в ЛК
        mainPage.goToProfile();
        webdriver().shouldHave(urlContaining("account"));

        //Кликаем "Конструктор"
        mainPage.goToConstructor();

        //Проверяем что вернулись в конструктор
        webdriver().shouldHave(urlContaining("/"), Duration.ofSeconds(5));
        mainPage.isTabActive("Булки");
        System.out.println("Текст активного таба: " + mainPage.isTabActive("Булки"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Переход по логотипу из Личного кабинета")
    void goToMainPageViaLogo(String browser) {
        setupBrowser(browser);

        mainPage.goToProfile();

        mainPage.clickLogoLink();

        // Проверяем что вернулись на главную
        webdriver().shouldHave(urlContaining("/"), Duration.ofSeconds(5));
        mainPage.isTabActive("Булки");
        System.out.println("Текст активного таба: " + mainPage.isTabActive("Булки"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Выход из аккаунта")
    void logout(String browser) {
        setupBrowser(browser);

        //Идем в ЛК
        mainPage.goToProfile();

        //Выходим
        profilePage.clickLogout();

        //Проверяем выход
        webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));
    }
}
