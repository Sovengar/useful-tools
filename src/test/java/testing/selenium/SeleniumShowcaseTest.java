package testing.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("selenium")
@DisplayName("Selenium E2E Showcase")
class SeleniumShowcaseTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Critical for CI/CD environments
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Basic Navigation and Assertion")
    void basicNavigation() {
        // 1. Navigate to a known testing site
        driver.get("https://the-internet.herokuapp.com/");

        // 2. Click on a link
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Challenging DOM")));
        link.click();

        // 3. Assertions
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h3")));
        assertThat(heading.getText()).isEqualTo("Challenging DOM");
        assertThat(driver.getCurrentUrl()).contains("challenging_dom");
    }

    @Test
    @DisplayName("Element interaction and Visibility")
    void elementInteraction() {
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement flashMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        assertThat(flashMessage.getText()).contains("You logged into a secure area!");
    }
}
