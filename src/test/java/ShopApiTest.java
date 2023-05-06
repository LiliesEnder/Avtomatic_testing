import api.test.AuthRequest;
import api.test.UserRequest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShopApiTest {

    private final String url = "http://localhost:5000/api";
    private static String username;
    private static String password;
    private static String token;
    private static String product_id;
    private static String product_name;
    private static String product_id_del;
    private static String cart_id;
    private static String item_to_del;

    @BeforeAll
    public static void generateUser() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String name = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        username = name;
        password = name + "pwd123";
    }

    @Test
    @Order(1)
    @DisplayName("/api/catalog: 200, Получение телефона без авторизации")
    public void getCatalogTest() {
        Response response = given().when()
                .contentType(ContentType.JSON)
                .get(url + "/catalog/");
        JsonPath js = response.jsonPath();
        response.then()
                .log().body()
                .statusCode(200)
                .body("info.name", Matchers.hasItems("Apple iPhone 8 Plus", "Apple iPhone X", "Huawei Mate 10 Pro"));
        product_id = js.get("[0]._id");
        product_name = js.get("[0].info.name");
        System.out.println("Product_id: " + product_id);
        product_id_del = js.get("[1]._id");
    }


    @Test
    @Order(2)
    @DisplayName("/api/auth/register: 201, Создание пользователя")
    public void createAuth() {
        AuthRequest req = new AuthRequest(username, password, username + "@gmail.com", "-", "-");
        given().log().body().contentType(ContentType.JSON).body(req)
                .post(url + "/auth/register").then()
                .statusCode(201)
                .body("address", Matchers.equalTo(req.getAddress()))
                .body("username", Matchers.equalTo(req.getUsername()))
                .body("phone", Matchers.equalTo(req.getPhone()));
    }

    @Test
    @Order(3)
    @DisplayName("/api/auth/login: 201, Вход пользователя")
    public void loginAuth() {
        Response response = given().log().body().contentType(ContentType.JSON)
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .post(url + "/auth/login");
        JsonPath js = response.jsonPath();
        response.then().log().body()
                .statusCode(200)
                .body("username", Matchers.equalTo(username))
                .body("phone", Matchers.equalTo("-"));
        token = js.getString("token");
    }

    @Test
    @Order(4)
    @DisplayName("/api/cart: 201, Добавить товар в корзину")
    public void addToCart() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("{\"product\": \"" + product_id + "\", \"quantity\": 1}")
                .post(url + "/cart")
                .then()
                .log().body().statusCode(200);
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("{\"product\": \"" + product_id_del + "\", \"quantity\": 2}")
                .post(url + "/cart")
                .then()
                .log().body().statusCode(200);
    }

    @Test
    @Order(5)
    @DisplayName("/api/cart: 200, Состав корзины")
    public void getCart() {
        Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get(url + "/cart");
        JsonPath js = response.jsonPath();
        response.then().log().body()
                .statusCode(200)
                .body("items[0].product._id", Matchers.equalTo(product_id))
                .body("items[0].quantity", Matchers.equalTo(1))
                .body("items[1].product._id", Matchers.equalTo(product_id_del))
                .body("items[1].quantity", Matchers.equalTo(2));
        cart_id = js.getString("_id");
        item_to_del = js.getString("items[1]._id");
    }

    @Test
    @Order(6)
    @DisplayName("/api/cart: 200, Удалить товар")
    public void delProduct() {
        given().log().body().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("{\"cartId\": \"" + cart_id + "\", \"itemId\": \"" + item_to_del + "\"}")
                .put(url + "/cart")
                .then().log().body()
                .statusCode(200);
        Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get(url + "/cart");
        JsonPath js = response.jsonPath();
        response.then().log().body()
                .statusCode(200)
                .body("items[0].product._id", Matchers.equalTo(product_id))
                .body("items[0].quantity", Matchers.equalTo(1));
        List<Object> items = js.getList("items");
        Assertions.assertEquals(items.size(), 1);
    }

    @Test
    @Order(7)
    @DisplayName("api/user: 200, Данные пользователя")
    public void getUser() {
        Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get(url + "/user");
        JsonPath js = response.jsonPath();
        response.then()
                .statusCode(200)
                .body("address", Matchers.equalTo("-"))
                .body("username", Matchers.equalTo(username))
                .body("phone", Matchers.equalTo("-"));
        UserRequest user = new UserRequest(
                js.getString("username"),
                null,
                js.getString("email"),
                js.getString("address"),
                js.getString("phone")
        );
        user.setId(js.getString("id"));
        user.setToken(js.getString("token"));
        user.setOrders(js.getList("orders"));
        user.setPhone("79001002000");
        user.setAddress("test address");
        given().log().body().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(user)
                .put(url + "/user")
                .then().statusCode(200).body("message", Matchers.equalTo("User updated"));
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get(url + "/user")
                .then()
                .log().body()
                .statusCode(200)
                .body("address", Matchers.equalTo(user.getAddress()))
                .body("username", Matchers.equalTo(username))
                .body("phone", Matchers.equalTo(user.getPhone()));
//                .body("password", Matchers.equalTo(user.getPassword()));
    }

    @Test
    @Order(8)
    @DisplayName("api/order: 200, Покупка")
    public void order(){
        given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("{\n" +
                        "  \"order\": {\n" +
                        "    \"dateCreated\": 1643796608156,\n" +
                        "    \"name\": \""+product_name+"\",\n" +
                        "    \"price\": 700,\n" +
                        "    \"quantity\": 2\n" +
                        "  }\n" +
                        "}")
                .then().log().body().statusCode(200);
    }
}
