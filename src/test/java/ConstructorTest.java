import config.BaseElements;
import config.TestsBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.MainPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstructorTest extends TestsBase {

    private MainPage mainPage;

    @BeforeEach
    void setUp() {
        mainPage = new MainPage();
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
        $x("//h1[contains(text(),'Соберите бургер')]").shouldBe(visible);
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Переход к разделу 'Булки'")
    void goToBunsSection(String browser) {
        setupBrowser(browser);

        // По умолчанию должны быть активны "Булки"
        assertTrue(mainPage.isTabActive("Булки"));
        mainPage.verifyBunsSectionVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Переход к разделу 'Соусы'")
    void goToSaucesSection(String browser) {
        setupBrowser(browser);

        mainPage.clickTab("Соусы");
        assertTrue(mainPage.isTabActive("Соусы"));
        mainPage.verifySaucesSectionVisible();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Переход к разделу 'Начинки'")
    void goToFillingsSection(String browser) {
        setupBrowser(browser);

        mainPage.clickTab("Начинки");
        assertTrue(mainPage.isTabActive("Начинки"));
        mainPage.verifyFillingsSectionVisible();
    }
}
