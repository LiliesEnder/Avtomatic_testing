package tests.api;


import api.endpoints.ApiAuth;
import api.endpoints.ApiCatalog;
import api.endpoints.ApiOrder;
import api.endpoints.ApiUser;
import api.models.Order;
import api.models.OrderRequest;
import api.models.Phone;
import api.models.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pro.learnup.ext.ApiTest;
import pro.learnup.testdata.DBTestHelper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/api/order")
@ApiTest
public class ApiOrderTest {
    static User user;
    static String password;
    List<Order> orders;

    @BeforeAll
    static void beforeAll() {
        password = new Faker().internet().password();
        user = new ApiAuth().createUser(password);
    }

    @BeforeEach
    void setUp() {
        orders = new ApiUser().getUser(ApiOrderTest.user).getOrders();
    }

    public static Stream<Phone> phones() {
        return new ApiCatalog().getAllPhones().stream();
    }

    @DisplayName("/api/order: 200: успешное оформление заказа")
    @ParameterizedTest
    @MethodSource("phones")
    void apiOrderSuccessfulTest(Phone phoneDto) {
        Order expectedOrder = Order.builder()
                .dateCreated(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(3)))
                .name(phoneDto.getInfo().getName())
                .price(phoneDto.getInfo().getPrice())
                .quantity(1)
                .build();

        new ApiOrder().orderPhones(user, OrderRequest.builder()
                .order(expectedOrder)
                .build());

        orders.add(expectedOrder);
        assertThat(new ApiUser().getUser(user).getOrders())
                .as("У юзера добавился еще один заказ")
                .containsExactlyInAnyOrderElementsOf(orders);
    }

    @AfterAll
    static void tearDown() {
        DBTestHelper.deleteUser(User.getObjectId(user));
    }
}