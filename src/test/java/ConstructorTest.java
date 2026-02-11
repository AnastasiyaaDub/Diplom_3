import config.BaseElements;
import config.TestsBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstructorTest extends TestsBase {

    private MainPage mainPage;


    @BeforeEach
    public void setUp() {
        mainPage = new MainPage();
        open(BaseElements.BASE_URL);

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
