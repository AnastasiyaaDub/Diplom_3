package config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.HashMap;
import java.util.Map;
import static com.codeborne.selenide.Selenide.*;
import static java.util.Arrays.asList;

public class TestsBase {

    @BeforeAll
    static void setupAll() {
        Configuration.baseUrl = BaseElements.BASE_URL;
        Configuration.timeout = 10000;

    }


    @BeforeEach
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");

        // Настройка Яндекс.Браузера
        if ("yandex".equals(browser)) {
            setupYandexBrowser();
        } else {
            // Настройки для обычного Chrome (если нужно что-то специфическое)
            Configuration.browser = "chrome";
        }

        //Закрытие попапов (если браузер уже запущен)
        if (WebDriverRunner.hasWebDriverStarted()) {
            closePopupsForYandex();
        }
    }

    private void setupYandexBrowser() {
        System.setProperty("webdriver.chrome.driver", "yandexdriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments(asList(
                "--no-first-run",
                "--no-default-browser-check",
                "--disable-restore-session-state",
                "--disable-features=StartupPromos,WelcomePage",
                "--disable-save-password-bubble",
                "--disable-infobars",
                "--disable-notifications",
                "--remote-allow-origins=*",
                "--disable-blink-features=AutomationControlled",
                "--disable-extensions",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-web-security",
                "--disable-gpu",
                "--window-size=1920,1080"
        ));

        options.setExperimentalOption("excludeSwitches",
                asList("enable-automation", "enable-logging"));
        options.setExperimentalOption("useAutomationExtension", false);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        Configuration.browserCapabilities = options;
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pageLoadTimeout = 30000;
        Configuration.pollingInterval = 500;

    }

    private void closePopupsForYandex() {
        try {
            executeJavaScript(
                    "document.querySelectorAll('[role=dialog], .modal, .popup, .alert, .overlay')" +
                            ".forEach(el => el.style.display = 'none');"
            );
        } catch (Exception e) {
            // Браузер еще не открыт - игнорируем
        }
    }

    @AfterEach
    void tearDown() {
        // Закрываем браузер после каждого теста
        closeWebDriver();
    }
}


