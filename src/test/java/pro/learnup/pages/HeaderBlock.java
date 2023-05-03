package pro.learnup.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import io.qameta.allure.Step;

public class HeaderBlock {

    private SelenideElement userNameInput = $(By.xpath("//input[contains(@id, 'Username')]"));
    private SelenideElement passwordInput = $(By.xpath("//input[contains(@id, 'Password')]"));
    private SelenideElement cartButton = $(By.xpath("//a[.='CART']"));
    private SelenideElement loginButton = $(By.xpath("//button[.='LOGIN']"));

    @Step("Авторизоваться пользователем {login} {password}")
    public PhonesPage login(String login, String password) {
        loginButton.click();
        userNameInput.sendKeys(login);
        passwordInput.sendKeys(password);
        new Button("Submit").click();
        $(byText("LOGOUT")).shouldBe(Condition.visible);
        return page(PhonesPage.class);
    }

    @Step("Перейти в корзину")
    public CartPage goToCart() {
        cartButton.click();
        return page(CartPage.class);
    }

    @Step("Выйти из пользователя")
    public void logout(){
        $(byText("LOGOUT")).shouldBe(Condition.visible).click();
    }
}