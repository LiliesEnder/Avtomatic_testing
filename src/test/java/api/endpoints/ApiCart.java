package api.endpoints;

import api.models.Cart;
import api.models.User;
import io.qameta.allure.Step;

public class ApiCart {
    Endpoint endpoint = new Endpoint("/api/cart/");

    @Step("{this.endpoint.getUrl}: Получение корзины")
    public Cart getCart(User user) {
        return this.endpoint.get(user, 200, Cart.class, User.getHeader(user));
    }
}
