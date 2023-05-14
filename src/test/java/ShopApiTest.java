import api.test.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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
    private static String user_id;

    private static MongoDatabase mongo;

    @BeforeAll
    public static void generateUser() {
        mongo = MongoConnector.getDataBase();
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

    @AfterAll
    public static void cleanup(){
        MongoCollection<Document> carts = mongo.getCollection("Cart");
        Bson query = eq("_id", cart_id);
        carts.deleteOne(query);

        MongoCollection<Document> users = mongo.getCollection("User");
        query = eq("_id", user_id);
        users.deleteOne(query);
    }

    @Test
    @Order(1)
    @DisplayName("/api/catalog: 200, Получение телефона без авторизации")
    public void getCatalogTest() {
        List<Phone> phones = new ArrayList<>();
        mongo.getCollection("products", Phone.class).find().into(phones);

        Response response = given().when()
                .contentType(ContentType.JSON)
                .get(url + "/catalog");
        JsonPath js = response.jsonPath();
        Phone[] catalog = response.then()
                .log().body()
                .statusCode(200)
                .extract().as(Phone[].class);
//        assertThat(catalog).containsExactlyInAnyOrderElementsOf(phones);
        product_id = catalog[0].get_id();
        product_name = catalog[0].getInfo().getName();
        product_id_del = catalog[1].get_id();
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
        user_id = js.getString("id");
    }

    @Test
    @Order(4)
    @DisplayName("/api/cart: 201, Добавить товар в корзину")
    public void addToCart() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CartAdd(product_id, 1, user_id))
                .post(url + "/cart")
                .then()
                .log().body().statusCode(200);
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CartAdd(product_id_del, 2, user_id))
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
        Cart cart = response.then().log().body()
                .statusCode(200)
                .extract()
                .as(Cart.class);
        System.out.println("Items: " + cart.getItems());
        Assertions.assertEquals(cart.getItems().get(0).getProduct().get_id(), product_id);
        Assertions.assertEquals(cart.getItems().get(0).getQuantity(), 1);
        Assertions.assertEquals(cart.getItems().get(1).getProduct().get_id(), product_id_del);
        Assertions.assertEquals(cart.getItems().get(1).getQuantity(), 2);
        cart_id = cart.get_id();
        item_to_del = cart.getItems().get(1).get_id();
        if (cart_id == null) {
            System.exit(-1);
        }
    }

    @Test
    @Order(6)
    @DisplayName("/api/cart: 200, Удалить товар")
    public void delProduct() {
        given().log().body().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(new CartDel(cart_id, item_to_del))
                .put(url + "/cart")
                .then().log().body()
                .statusCode(200);
        Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get(url + "/cart");
        Cart cart = response.then().log().body().statusCode(200).extract().as(Cart.class);
        Assertions.assertEquals(cart.getItems().get(0).getProduct().get_id(), product_id);
        Assertions.assertEquals(cart.getItems().get(0).getQuantity(), 1);
        Assertions.assertEquals(cart.getItems().size(), 1);
    }

    @Test
    @Order(7)
    @DisplayName("api/user: 200, Данные пользователя")
    public void getUser() {
        Response response = given().contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .get(url + "/user");
        JsonPath js = response.jsonPath();
        UserRequest user = response.then()
                .statusCode(200)
                .extract().as(UserRequest.class);

        Assertions.assertEquals(user.getAddress(), "-");
        Assertions.assertEquals(user.getUsername(), username);
        Assertions.assertEquals(user.getPhone(), "-");

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
                .body(new OrderRequest(new api.test.Order(1643796608156L, product_name, 700, 2)))
                .then().log().body().statusCode(200);
    }
}
