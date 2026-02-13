import config.BaseElements;
import config.TestsBase;
import data.UserApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import pages.LoginPage;
import pages.MainPage;
import pages.ProfilePage;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountNavigationTest extends TestsBase {

    private MainPage mainPage;
    private ProfilePage profilePage;


    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        //Создаем пользователя через API
        createdUser = UserApi.createUserForUITest();

        mainPage = new MainPage();
        profilePage = new ProfilePage();

        open(BaseElements.BASE_URL);

        mainPage.goToProfile();
        LoginPage loginPage = new LoginPage();
        loginPage.login(createdUser.getEmail(), createdUser.getPassword());

        actions().sendKeys(Keys.ESCAPE).perform();
        webdriver().shouldHave(urlContaining("/"));
    }


    @Test
    @DisplayName("Переход в Личный кабинет после логина")
    void goToPersonalAccountAfterLogin() {
        // Кликаем "Личный кабинет"
        mainPage.goToProfile();

        // Проверяем что попали в ЛК
        webdriver().shouldHave(urlContaining("account"), Duration.ofSeconds(5));
        profilePage.shouldBeLoaded();
    }

    @Test
    @DisplayName("Переход в Конструктор из Личного кабинета")
    void goToConstructorFromPersonalAccount() {
        //Идем в ЛК
        mainPage.goToProfile();
        webdriver().shouldHave(urlContaining("account"));

        //Кликаем "Конструктор"
        mainPage.goToConstructor();

        //Проверяем что вернулись в конструктор
        webdriver().shouldHave(urlContaining("/"), Duration.ofSeconds(5));

        assertTrue(mainPage.isTabActive("Булки"));


    }

    @Test
    @DisplayName("Переход по логотипу из Личного кабинета")
    void goToMainPageViaLogo() {

        mainPage.goToProfile();

        mainPage.clickLogoLink();

        // Проверяем что вернулись на главную
        webdriver().shouldHave(urlContaining("/"), Duration.ofSeconds(5));
        mainPage.isTabActive("Булки");
        System.out.println("Текст активного таба: " + mainPage.isTabActive("Булки"));
    }

    @Test
    @DisplayName("Выход из аккаунта")
    void logout() {
        //Идем в ЛК
        mainPage.goToProfile();

        //Выходим
        profilePage.clickLogout();

        //Проверяем выход
        webdriver().shouldHave(urlContaining("login"), Duration.ofSeconds(5));
    }

}
