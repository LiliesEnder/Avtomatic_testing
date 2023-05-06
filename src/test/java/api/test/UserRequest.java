package api.test;

import java.util.List;

public class UserRequest extends AuthRequest {
    private String token;
    private String id;
    private List<Object> orders;

    public UserRequest(String username, String password, String email, String address, String phone) {
        super(username, password, email, address, phone);
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public List<Object> getOrders() {
        return orders;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrders(List<Object> orders) {
        this.orders = orders;
    }

}
