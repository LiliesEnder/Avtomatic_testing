package pro.learnup.testdata;

import api.models.Phone;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.qameta.allure.Step;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import pro.learnup.testdata.MongoConnector;

public class DBTestHelper {

    static MongoDatabase mongo = MongoConnector.getDataBase();

    @Step("Получить в БД все телефоны")
    public static List<Phone> getAllPhones() {
        List<Phone> phoneDtoList = new ArrayList<>();
        return mongo.getCollection("products", Phone.class).find().into(phoneDtoList);
    }

    public static void deleteUser(User user) {
        deleteUser(user.getId());
    }

    @Step("Удалить в БД юзера {id}")
    public static void deleteUser(ObjectId id) {
        mongo.getCollection("users").deleteOne(Filters.eq("_id", id));
    }
}
