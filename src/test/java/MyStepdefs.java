import com.codeborne.selenide.Condition;
import com.codeborne.selenide.selector.ByText;
import io.cucumber.java.ru.*;
import org.openqa.selenium.By;
import pro.learnup.pages.CartPage;
import pro.learnup.pages.HeaderBlock;
import pro.learnup.pages.PhonePage;
import pro.learnup.pages.PhonesPage;

import static com.codeborne.selenide.Selenide.*;

public class MyStepdefs {
    private static String url = "http://localhost:3000/";

    @Дано("Пользователь {string} {string} авторизован")
    public void пользовательАвторизован(String arg0, String arg1) {
        open(url, PhonesPage.class).getHeaderBlock().login(arg0, arg1);
    }

    @Когда("Пользователь выбирает телефон {string}")
    public void пользовательВыбираетТелефон(String arg0) {
        page(PhonesPage.class).selectPhone(arg0);
    }

    @Тогда("Пользователь находится на странице телефона {string}")
    public void пользовательНаходитсяНаСтраницеТелефона(String arg0) {
        page(PhonePage.class).checkPhoneName(arg0);
    }

    @Когда("Пользователь нажимает на кнопку {string}")
    public void пользовательНажимаетНаКнопку(String arg0) {
        $x("//button[.='"+arg0+"']").click();
    }

    @И("Пользователь переходит в корзину")
    public void пользовательПереходитВКорзину() {
        new HeaderBlock().goToCart();
    }

    @То("В корзине только телефон {string}")
    public void вКорзинеТолькоТелефон(String arg0) {
        new CartPage().checkCartContainExactly(arg0);
    }

    @Тогда("Отображается текст об успешном оформлении заказа")
    public void отображаетсяТекстОбУспешномОформленииЗаказа() {
        page(CartPage.class).checkThatCheckOutIsSuccessful();
    }
    @Дано("Открыта базовая страница")
    public void открытаБазоваяСтраница(){
        open(url);
    }
    @И("Пользователь вводит значения {string} в поле {string}")
    public void пользовательВводитЗначенияВПоле(String value, String field){
        $(By.xpath("//input[contains(@id, '"+field+"')]")).sendKeys(value);
    }
    @Тогда("Отображается кнопка {string}")
    public void отображаетсяКнопка(String button){
        $(new ByText(button)).shouldBe(Condition.visible);
    }

    @Тогда("Выйти из пользователя")
    public void выйтиИзПользователя() {
        open(url, PhonesPage.class).getHeaderBlock().logout();
    }
}
