package api.models;

import io.restassured.http.Header;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private String address;
    private String phone;
    private String token;
    private String id;
    private List<Order> orders;


    public static ObjectId getObjectId(User user){
        return new ObjectId(user.id);
    }

    public static Header getHeader(User user){
        return new Header("Authorization", "Bearer " + user.getToken());
    }
}
