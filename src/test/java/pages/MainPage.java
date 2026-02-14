package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    // Верхняя панель
    private final SelenideElement profileButton = $x("//p[@class='AppHeader_header__linkText__3q_va ml-2' and text()='Личный Кабинет']");

    private final SelenideElement constructorButton = $x("//p[@class='AppHeader_header__linkText__3q_va ml-2' and text()='Конструктор']");
    // Ссылка логотипа (кликабельна и ведет на главную)
    private final SelenideElement logoLink = $("svg[width='290'][height='50']");

    // Табы конструктора
    private final SelenideElement bunTab = $x("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Булки']");
    private final SelenideElement sauceTab = $x("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Соусы']");
    private final SelenideElement fillingTab = $x("//div[contains(@class, 'tab_tab__1SPyG')]//span[text()='Начинки']");
    // Индикатор активного таба (чтобы проверять, какой таб выбран)
    private final SelenideElement activeTab = $("div.tab_tab_type_current__2BEPc");

    //кнопка "Войти в аккаунт"
    private final SelenideElement loginButtonMain = $("button.button_button_size_large__G21Vg");


    @Step("Перейти в личный кабинет")
    public void goToProfile() {
        profileButton.click();
    }

    @Step("Нажать на кнопку 'Войти в аккаунт'")
    public void clickloginButtonMain() {
        loginButtonMain.click();
    }

    @Step("Перейти на главную по нажатию на лого")
    public void clickLogoLink() {
        logoLink.click();
    }

    //Проверка активного таба
    public boolean isTabActive(String tabName) {
        return activeTab.getText().contains(tabName);
    }

    @Step("Выбрать булки")
    public void selectBunTab() {
        bunTab.click();
    }

    @Step("Выбрать соусы")
    public void selectSauceTab() {
        sauceTab.click();
    }

    @Step("Выбрать начинки")
    public void selectFillingTab() {
        fillingTab.click();
    }

    @Step("Перейти в конструктор")
    public void goToConstructor() {
        constructorButton.click();
    }

    @Step("Проверить что раздел 'Булки' виден")
    public void verifyBunsSectionVisible() {
        $x("//h2[text()='Булки']").shouldBe(visible);

    }

    @Step("Проверить что раздел 'Соусы' виден")
    public void verifySaucesSectionVisible() {
        $x("//h2[text()='Соусы']").shouldBe(visible);
    }

    @Step("Проверить что раздел 'Начинки' виден")
    public void verifyFillingsSectionVisible() {
        $x("//h2[text()='Начинки']").shouldBe(visible);
    }

    @Step("Кликнуть на таб '{tabName}'")
    public void clickTab(String tabName) {
        switch (tabName) {
            case "Булки":
                selectBunTab();
                break;
            case "Соусы":
                selectSauceTab();
                break;
            case "Начинки":
                selectFillingTab();
                break;
            default:
                throw new IllegalArgumentException("Неизвестный таб: " + tabName);
        }
    }

}



