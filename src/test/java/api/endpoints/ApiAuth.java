package api.endpoints;
import api.models.User;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class ApiAuth {
    private final Endpoint endpoint = new Endpoint("/api/auth/register");
    static Faker faker = new Faker();

    public User createUser(String password){
        User user =  api.models.User.builder()
                .address(faker.address().fullAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .email(faker.internet().emailAddress())
                .username(faker.name().fullName())
                .password(password)
                .build();
        registerNewUser(user);
        return user;
    }

    @Step("{this.endpoint.getUrl}: Регистрация нового юзера {user.username}")
    public User registerNewUser(User user) {
        return this.endpoint.post(user, 201, User.class);
    }

    public String getUrl(){
        return this.endpoint.getEndpoint();
    }
}
