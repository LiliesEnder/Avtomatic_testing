package pro.learnup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopTest {

    protected WebDriver webDriver;
    protected WebDriverWait webDriverWait;

    protected void login() {
        webDriver.get("http://localhost:3000/");

        webDriver.findElement(By.xpath("//button[.='LOGIN']")).click();
        webDriver.findElement(By.xpath("//input[contains(@id, 'Username')]")).sendKeys("admin");
        webDriver.findElement(By.xpath("//input[contains(@id, 'Password')]")).sendKeys("admin");
        webDriver.findElement(By.xpath("//button[.='Submit']")).click();
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='LOGOUT']")));
    }

    @BeforeEach
    void setUp() {

        ChromeOptions chromeOptions = new ChromeOptions().addArguments("--blink-settings=imagesEnabled=false");
        webDriver = WebDriverManager.chromedriver().create();
//        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        webDriverWait = new WebDriverWait(webDriver, 3);
    }

    @AfterEach
    void tearDown() {
        webDriver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"HTC U11"})
    public void buyPhoneTest(String phoneName) {

        login();

        List<WebElement> phones = webDriver.findElements(By.className("product"));
        phones.forEach(p -> System.out.println(p.getText() + "\n\n"));

        phones.stream()
                .filter(el -> el.getText().contains(phoneName))
                .findFirst()
                .orElseThrow()
                .findElement(By.xpath(".//a[.='See more']")).click();

        assertThat(webDriver.findElement(By.cssSelector(".product-details-container h1")).getText())
                .as("Должна открыться страница с телефоном " + phoneName)
                .isEqualTo(phoneName);

        webDriver.findElement(By.xpath("//button[.='Add to cart']")).click();
        webDriver.findElement(By.xpath("//a[.='CART']")).click();

        List<String> actualPhoneList = webDriver.findElement(By.className("cart-items"))
                .findElements(By.xpath(".//table//tbody/tr"))
                .stream()
                .map(el -> el.findElements(By.xpath("./td")).get(1).getText())
                .collect(Collectors.toList());

        assertThat(actualPhoneList).containsExactlyInAnyOrder(phoneName);

        webDriver.findElement(By.xpath("//button[.='Checkout']")).click();

        webDriverWait.until(ExpectedConditions.textToBe(By.xpath("//h1[text()='Checkout Information']/../table//tbody/tr/td[1]"), phoneName));

        WebElement confirm =  webDriver.findElement(By.xpath("//button[.='Confirm']"));
        confirm.click();

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[.='OK']")))
                .click();

        webDriver.quit();
    }
}
