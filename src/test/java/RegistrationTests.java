import config.BaseElements;
import config.TestsBase;
import data.UserUI;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import pages.RegistrationPage;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static config.BaseElements.REGISTER_UI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RegistrationTests extends TestsBase {
    private UserUI createdUser;


    @BeforeEach
    public void setUp() {
        super.setUp();
        open(REGISTER_UI);

        createdUser = null;
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

        $(withText("Вход")).shouldBe(visible, Duration.ofSeconds(5));
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

        $(withText("Вход")).shouldBe(visible, Duration.ofSeconds(5));
        actions().sendKeys(Keys.ESCAPE).perform();
        // Проверка
        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("login"),
                "Ожидался редирект на страницу логина. Текущий URL: " + currentUrl);
    }


    @Test
    @DisplayName("Ошибка регистрации пользователя при вводе пароля менее 5 символов")
    void RegisterWithAFiveCharacterPassword() {

        createdUser = UserUI.generateRandom();
        createdUser.setPassword("12345");


        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.register(
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPassword()
        );

        $(byClassName("input__error")).shouldBe(visible, Duration.ofSeconds(5));

        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("register"),
                "Ожидалось остаться на странице регистрации при коротком пароле. Текущий URL: " + currentUrl);

        assertTrue(registrationPage.isPasswordErrorDisplayed(),
                "Ошибка пароля должна отображаться");
    }

    @AfterEach
    void tearDown() {
        // Удаляем пользователя, если он был создан
        if (createdUser != null && createdUser.getPassword() != null
                && createdUser.getPassword().length() >= 6) {
            try {
                String token = given()
                        .contentType(ContentType.JSON)
                        .body(createdUser)  // если UserUI сериализуется корректно
                        .when()
                        .post(BaseElements.LOGIN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("accessToken");

                // Если есть токен - удаляем
                if (token != null) {
                    given()
                            .header("Authorization", token)
                            .when()
                            .delete(BaseElements.USER)
                            .then()
                            .statusCode(202);
                    System.out.println("Удален пользователь: " + createdUser.getEmail());
                }
            } catch (Exception e) {
                System.err.println("Не удалось удалить пользователя: " + e.getMessage());
            }


        }
    }
}
