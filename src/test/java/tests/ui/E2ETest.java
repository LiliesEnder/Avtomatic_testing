package tests.ui;

import api.endpoints.ApiAuth;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import api.models.Phone;
import api.models.User;
//import pro.learnup.extensions.UiTest;
import pro.learnup.ext.UiTest;
import pro.learnup.pages.PhonesPage;
import pro.learnup.testdata.DBTestHelper;
import com.github.javafaker.Faker;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.parameter;

@DisplayName("Покупка телефона")
@UiTest
public class E2ETest {
    User user;
    String password;
    Phone phoneDto;

    static Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        password = faker.internet().password();
        user = new ApiAuth().registerNewUser(
                User.builder()
                        .address(faker.address().fullAddress())
                        .phone(faker.phoneNumber().phoneNumber())
                        .email(faker.internet().emailAddress())
                        .username(faker.name().fullName())
                        .password(password)
                        .build());;
        phoneDto = DBTestHelper.getAllPhones().get(0);
    }

    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Покупка телефона")
    @Test
    public void buyPhoneTest() {
        open("/", PhonesPage.class)
                .getHeaderBlock()
                .login(user.getUsername(), password)
                .selectPhone(phoneDto.getInfo().getName())
                .checkPhoneName(phoneDto.getInfo().getName())
                .clickAddToCart()
                .getHeaderBlock()
                .goToCart()
                .checkCartContainExactly(phoneDto.getInfo().getName())
                .clickCheckOut()
                .clickConfirm()
                .checkThatCheckOutIsSuccessful();
    }

    @AfterEach
    void tearDown() {
        DBTestHelper.deleteUser(User.getObjectId(user));
    }
}