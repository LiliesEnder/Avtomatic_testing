package api.endpoints;

import api.models.User;
import io.qameta.allure.Step;

public class ApiUser {
    Endpoint endpoint = new Endpoint("/api/user/");

    @Step("{this.endpoint.getUrl}: Получении информации об юзера {userDto.username}")
    public User getUser(User user) {
        return this.endpoint.get(user, 200, User.class, User.getHeader(user));
    }

}
