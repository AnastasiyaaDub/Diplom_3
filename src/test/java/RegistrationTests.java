import config.TestsBase;
import data.UserUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import pages.LoginPage;
import pages.RegistrationPage;
import static com.codeborne.selenide.Selenide.*;
import static config.BaseElements.REGISTER_UI;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RegistrationTests extends TestsBase {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        open(REGISTER_UI);

    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    void registrationTest() {

        createdUser = UserUI.generateRandom();

        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.register(
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPassword()
        );

        LoginPage loginPage = new LoginPage();
        loginPage.checkPageLoaded();

        actions().sendKeys(Keys.ESCAPE).perform();
        // Проверка
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("login"),
                "Ожидался редирект на страницу логина. Текущий URL: " + currentUrl);
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя с минимальным паролем - 6 символов")
    void registrationPasswordMinTest() {

        createdUser = UserUI.generateRandom();
        createdUser.setPassword("Qwerty");


        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.register(
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPassword()
        );

        LoginPage loginPage = new LoginPage();
        loginPage.checkPageLoaded();

        actions().sendKeys(Keys.ESCAPE).perform();
        // Проверка
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("login"),
                "Ожидался редирект на страницу логина. Текущий URL: " + currentUrl);
    }


    @Test
    @DisplayName("Ошибка регистрации пользователя при вводе пароля менее 6 символов")
    void RegisterWithAFiveCharacterPassword() {

        createdUser = UserUI.generateRandom();
        createdUser.setPassword("12345");


        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.register(
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPassword()
        );

        assertTrue(registrationPage.isPasswordErrorDisplayed(),
                "Ошибка о некорректном пароле должна отображаться");

        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("register"),
                "Ожидалось остаться на странице регистрации при коротком пароле. Текущий URL: " + currentUrl);

    }

}
