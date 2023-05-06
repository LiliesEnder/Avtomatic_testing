package api.test;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String address;
    private String phone;
    private String token;
    private String id;
    private List<Order> orders;
}
