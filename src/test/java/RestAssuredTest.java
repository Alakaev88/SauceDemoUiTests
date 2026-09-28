import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RestAssuredTest {
    @Test
    void getUserTest(){
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users/1")
                .then()
                .statusCode(200)
                .body("id",equalTo(1))
                .body("address.city", equalTo("Gwenborough"));
    }

    @Test
    void getUsersTest() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(10))
                .body("[0].id", equalTo(1))
                .body("[9].id", equalTo(10));
    }

    @Test
    void createUserTest() {
        String requestBody = """
            {
                "name": "Alex",
                "username": "alex123",
                "email": "alex@example.com"
            }
            """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("https://jsonplaceholder.typicode.com/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Alex"))
                .body("username", equalTo("alex123"))
                .body("email", equalTo("alex@example.com"))
                .body("id", notNullValue());
    }
}
