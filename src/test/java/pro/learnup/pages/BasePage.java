package pro.learnup.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;

public class BasePage {
    @Getter
    private HeaderBlock headerBlock = new HeaderBlock();
}