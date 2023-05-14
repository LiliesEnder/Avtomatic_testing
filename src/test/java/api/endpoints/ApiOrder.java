package api.endpoints;

import api.models.OrderRequest;
import api.models.User;
import io.qameta.allure.Step;

public class ApiOrder {

    Endpoint endpoint = new Endpoint("/api/order/");

    @Step("{this.endpoint.getUrl}: Оформление заказа")
    public void orderPhones(User user, OrderRequest orderRequestDto) {
        this.endpoint.post(orderRequestDto, 200, User.getHeader(user));
    }
}
