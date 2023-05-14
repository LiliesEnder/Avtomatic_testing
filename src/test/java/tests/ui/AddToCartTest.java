package tests.ui;

import api.endpoints.ApiAuth;
import api.endpoints.ApiCart;
import api.models.CartItem;
import api.models.Phone;
import api.models.User;
import com.github.javafaker.Faker;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.learnup.ext.UiTest;
import pro.learnup.pages.PhonesPage;
import pro.learnup.testdata.DBTestHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.learnup.steps.UiSteps.openPage;;

@UiTest
public class AddToCartTest {

    Faker faker = new Faker();
    User user;
    String password;
    Phone phoneDto;

    @BeforeEach
    void setUp() {
        password = faker.internet().password();
        user = new ApiAuth().registerNewUser(
                api.models.User.builder()
                        .address(faker.address().fullAddress())
                        .phone(faker.phoneNumber().phoneNumber())
                        .email(faker.internet().emailAddress())
                        .username(faker.name().fullName())
                        .password(password)
                        .build());
        phoneDto = DBTestHelper.getAllPhones().get(0);
    }

    @Test
    void addToCartTest() {
        pro.learnup.testdata.User userl = new pro.learnup.testdata.User(new ObjectId(user.getId()), user.getUsername(), password, user.getToken());
        openPage(userl, PhonesPage.class)
                .selectPhone(phoneDto.getInfo().getName())
                .checkPhoneName(phoneDto.getInfo().getName())
                .clickAddToCart()
                .checkSuccessfulPhoneAddedToCart();

        List<CartItem> items = new ApiCart().getCart(user).getItems();

        assertThat(items).hasSize(1);
        assertThat(items.get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(CartItem.builder()
                        .product(phoneDto)
                        .quantity(1)
                        .build());
    }
}
