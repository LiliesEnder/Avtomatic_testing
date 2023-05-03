package api.test;

public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private String address;
    private String phone;

    public AuthRequest(String username, String password, String email, String address, String phone){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }
}
