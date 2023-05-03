import api.test.AuthRequest;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.nio.charset.Charset;
import java.util.Random;

import static io.restassured.RestAssured.given;

@Slf4j
public class ShopApiTest {


    private final String url = "http://localhost:5000/api";

    @Test
    @DisplayName("/api/catalog: 200, Получение телефона без авторизации")
    public void getCatalogTest() {
        given().when()
                .contentType(ContentType.JSON)
                .get(url + "/catalog/").then()
                .statusCode(200)
                .body("info.name", Matchers.hasItems("Apple iPhone 8 Plus", "Apple iPhone X", "Huawei Mate 10 Pro"));
    }


    @Test
    @DisplayName("/api/auth/register: 201, Создание пользователя")
    public void createAuth(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String name = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        AuthRequest req = new AuthRequest(name, "pwd123", name + "@gmail.com", "-", "-");
        given().contentType(ContentType.JSON).body(req)
                .post(url + "/auth/register").then()
                .statusCode(201)
                .body("address", Matchers.equalTo(req.getAddress()))
                .body("username", Matchers.equalTo(req.getUsername()))
                .body("phone", Matchers.equalTo(req.getPhone()));
    }
}
