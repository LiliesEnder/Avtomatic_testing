package tests.ui;


import api.endpoints.ApiAuth;
import api.models.User;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pro.learnup.ext.UiTest;
import pro.learnup.pages.PhonesPage;
import pro.learnup.testdata.DBTestHelper;

import static com.codeborne.selenide.Selenide.open;

@DisplayName("Авторизация пользователя")
@UiTest
public class AuthTest {

    User user;
    String password;

    static Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        password = faker.internet().password();
        user = new ApiAuth().createUser(password);
    }

    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Авторизация пользователя")
    @Test
    public void authTest() {
        open("/", PhonesPage.class)
                .getHeaderBlock()
                .login(user.getUsername(), password);
    }

    @AfterEach
    void tearDown() {
        DBTestHelper.deleteUser(User.getObjectId(user));
    }
}