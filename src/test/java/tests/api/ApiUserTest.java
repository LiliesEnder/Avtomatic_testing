package tests.api;

import api.endpoints.ApiAuth;
import api.endpoints.ApiUser;
import api.models.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.learnup.ext.ApiTest;
import pro.learnup.testdata.DBTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/api/user")
@ApiTest
public class ApiUserTest {
    User userDto;

    @BeforeEach
    void setUp() {
        userDto = new ApiAuth().createUser(new Faker().internet().password());
    }

    @Test
    @DisplayName("/api/user: 200: получение информации о юзере авторизованным пользователем")
    void successfulGetUserTest() {
        assertThat(new ApiUser().getUser(User.builder().token(userDto.getToken()).build()))
                .usingRecursiveComparison()
                .isEqualTo(userDto);
    }

    @AfterEach
    void tearDown() {
        DBTestHelper.deleteUser(User.getObjectId(userDto));
    }
}
