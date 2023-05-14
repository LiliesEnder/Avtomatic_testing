package api.endpoints;

import api.models.Phone;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.List;

public class ApiCatalog {

    Endpoint endpoint = new Endpoint("/api/user/");

    @Step("{this.endpoint.getUrl}: Получение всех телефонов")
    public List<Phone> getAllPhones() {
        return Arrays.asList( this.endpoint.get(200, Phone[].class));
    }
}
