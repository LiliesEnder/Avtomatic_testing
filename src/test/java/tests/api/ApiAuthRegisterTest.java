package tests.api;

import api.endpoints.ApiAuth;
import api.models.User;
import com.github.javafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pro.learnup.ext.ApiTest;
import pro.learnup.testdata.DBTestHelper;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@DisplayName("/api/auth/register")
@ApiTest
public class ApiAuthRegisterTest {
    static Faker faker = new Faker();
    User userDto;

    public static Stream<User> successfulCreateUserRequests() {
        return Stream.of(User.builder()
                        .username(faker.name().fullName())
                        .password(faker.internet().password())
                        .build(),
                User.builder()
                        .address(faker.address().fullAddress())
                        .phone(faker.phoneNumber().phoneNumber())
                        .email(faker.internet().emailAddress())
                        .username(faker.name().fullName())
                        .password(faker.internet().password())
                        .build()
        );
    }

    @ParameterizedTest
    @DisplayName("/api/auth/register: 201: успешное создание юзера")
    @MethodSource("successfulCreateUserRequests")
    void createUserTest(User userDto) {
        this.userDto = new ApiAuth().registerNewUser(userDto);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.userDto)
                .as("Созданный юзер должен быть равен ожидаемому")
                .usingRecursiveComparison()
                .ignoringFields("id", "orders", "password", "token")
                .isEqualTo(this.userDto);
        softAssertions.assertThat(this.userDto.getId()).isNotEmpty();
        softAssertions.assertThat(this.userDto.getPassword()).isNotEmpty();
        softAssertions.assertThat(this.userDto.getToken()).isNotEmpty();
        softAssertions.assertThat(this.userDto.getOrders()).isEmpty();
        softAssertions.assertAll();
    }


    public static Stream<User> failedCreateUserRequests() {
        return Stream.of(User.builder()
                        .password(faker.internet().password())
                        .build(),
                User.builder()
                        .username(faker.name().fullName())
                        .build()
        );
    }

    @ParameterizedTest
    @DisplayName("/api/auth/register: 400: неуспешное создание юзера")
    @MethodSource("failedCreateUserRequests")
    void failedCreateUser400Test(User userDto) {
        given()
                .body(userDto)
                .post(new ApiAuth().getUrl())
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("/api/auth/register: 409: User already exists")
    void failedCreateUser409Test() {
        userDto = new ApiAuth().createUser(faker.internet().password());

        given()
                .body(userDto)
                .post(new ApiAuth().getUrl())
                .then()
                .statusCode(409)
                .body("message", Matchers.equalTo("User already exists"));
    }

    @AfterEach
    void tearDown() {
        DBTestHelper.deleteUser(User.getObjectId(userDto));
    }
}