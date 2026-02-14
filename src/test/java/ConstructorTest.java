import config.BaseElements;
import config.TestsBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstructorTest extends TestsBase {

    private MainPage mainPage;


    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        mainPage = new MainPage();
        open(BaseElements.BASE_URL);

    }

    @Test
    @DisplayName("Переход к разделу 'Булки'")
    void goToBunsSection() {
        // По умолчанию должны быть активны "Булки"
        assertTrue(mainPage.isTabActive("Булки"));
        mainPage.verifyBunsSectionVisible();
    }

    @Test
    @DisplayName("Переход к разделу 'Соусы'")
    void goToSaucesSection() {
        mainPage.clickTab("Соусы");
        assertTrue(mainPage.isTabActive("Соусы"));
        mainPage.verifySaucesSectionVisible();
    }

    @Test
    @DisplayName("Переход к разделу 'Начинки'")
    void goToFillingsSection() {
        mainPage.clickTab("Начинки");
        assertTrue(mainPage.isTabActive("Начинки"));
        mainPage.verifyFillingsSectionVisible();
    }
}
