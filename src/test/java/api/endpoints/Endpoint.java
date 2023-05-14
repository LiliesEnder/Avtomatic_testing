package api.endpoints;

import io.restassured.http.Header;

import static io.restassured.RestAssured.given;

public class Endpoint {
    private String url;

    public Endpoint(String url) {
        this.url = url;
    }

    ;

    public String getEndpoint() {
        return this.url;
    }

    public <R, T> R post(T body, int status, Class<R> rClass) {
        return given()
                .body(body)
                .post(this.url)
                .then()
                .statusCode(status)
                .extract()
                .as(rClass);
    }

    public <R, T> R post(T body, int status, Class<R> rClass, Header header) {
        return given()
                .body(body)
                .header(header)
                .post(this.url)
                .then()
                .statusCode(status)
                .extract()
                .as(rClass);
    }

    public <T> void post(T body, int status, Header header) {
        given()
                .body(body)
                .header(header)
                .post(this.url)
                .then()
                .statusCode(status);
    }

    public <R> R get(int status, Class<R> rClass) {
        return given()
                .get(this.url)
                .then()
                .statusCode(status)
                .extract()
                .as(rClass);
    }

    public <R, T> R get(T body, int status, Class<R> rClass) {
        return given()
                .body(body)
                .get(this.url)
                .then()
                .statusCode(status)
                .extract()
                .as(rClass);
    }

    public <R, T> R get(T body, int status, Class<R> rClass, Header header) {
        return given()
                .body(body)
                .header(header)
                .get(this.url)
                .then()
                .statusCode(status)
                .extract()
                .as(rClass);
    }


}
