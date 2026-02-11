import config.BaseElements;
import config.TestsBase;
import data.UserApi;
import data.UserUI;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Keys;
import pages.RegistrationPage;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static config.BaseElements.REGISTER_UI;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Feature("Регистрация пользователя")
public class RegistrationTests extends TestsBase {
    private UserUI createdUser;

    @BeforeEach
    void setUp() {
        // Только открываем базовую страницу
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
        createdUser = null;
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Успешная регистрация нового пользователя")

    void registrationTest(String browser) {
        // Настраиваем браузер перед тестом
        setupBrowser(browser);


        createdUser = UserUI.generateRandom();

        open(REGISTER_UI);
        $("h1, h2, h3").shouldBe(visible);

        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.register(
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPassword()
        );

        $(withText("Вход")).shouldBe(visible, Duration.ofSeconds(5));
        actions().sendKeys(Keys.ESCAPE).perform();
        // Проверка
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("login"),
                "Ожидался редирект на страницу логина. Текущий URL: " + currentUrl);
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Успешная регистрация нового пользователя с минимальным паролем - 6 символов")

    void registrationPasswordMinTest(String browser) {
        // Настраиваем браузер перед тестом
        setupBrowser(browser);


        createdUser = UserUI.generateRandom();
        createdUser.setPassword("Qwerty");

        open(REGISTER_UI);
        $("h1, h2, h3").shouldBe(visible);

        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.register(
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPassword()
        );

        $(withText("Вход")).shouldBe(visible, Duration.ofSeconds(5));
        actions().sendKeys(Keys.ESCAPE).perform();
        // Проверка
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("login"),
                "Ожидался редирект на страницу логина. Текущий URL: " + currentUrl);
    }


    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Ошибка регистрации пользователя при вводе пароля менее 5 символов")

    void RegisterWithAFiveCharacterPassword (String browser) {
        // Настраиваем браузер перед тестом
        setupBrowser(browser);

        createdUser = UserUI.generateRandom();
        createdUser.setPassword("12345");

        open(REGISTER_UI);
        $("h1, h2, h3").shouldBe(visible);

        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.register(
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPassword()
        );


        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("register"),
                "Ожидалось остаться на странице регистрации при коротком пароле. Текущий URL: " + currentUrl);

        assertTrue(registrationPage.isPasswordErrorDisplayed(),
                "Ошибка пароля должна отображаться");
    }

    @AfterEach
    void tearDown() {
        // Удаляем пользователя, если он был создан
        if (createdUser != null) {
            try {
                // Пытаемся получить токен через логин и удалить
                UserApi.deleteUser(createdUser);
            } catch (Exception e) {
                System.err.println("Не удалось удалить пользователя: " + e.getMessage());
            }
        }
    }

}
