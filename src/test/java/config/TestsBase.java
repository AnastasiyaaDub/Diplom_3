package config;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestsBase {

    @BeforeAll
    static void setupAll() {
        Configuration.baseUrl = BaseElements.BASE_URL;
        Configuration.timeout = 10000;
    }
    protected void setupBrowser(String browser) {
        if ("yandex".equals(browser)) {
            System.setProperty("webdriver.chrome.driver", "yandexdriver.exe");
        }
        Configuration.browser = "chrome"; // Яндекс тоже chrome
    }


    @BeforeEach
    public void setupYandexBrowser() {
        ChromeOptions options = new ChromeOptions();

        // ФИКС для data:, и всплывашек
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-restore-session-state");
        options.addArguments("--disable-features=StartupPromos,WelcomePage");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");

        // Отключаем автоматизацию detection
        options.setExperimentalOption("excludeSwitches",
                Arrays.asList("enable-automation", "enable-logging"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Отключаем сохранение паролей
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        Configuration.browserCapabilities = options;
        Configuration.browser = "chrome";

        // Увеличиваем таймауты для Яндекса
        Configuration.timeout = 20000;
        Configuration.pageLoadTimeout = 30000;

        System.out.println("Настроен Яндекс.Браузер");
    }

    @AfterEach
    void tearDown() {
        // Закрываем браузер после каждого теста
        closeWebDriver();
    }
}

