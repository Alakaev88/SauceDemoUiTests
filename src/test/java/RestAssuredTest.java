import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue; // Импортируем проверку значения, которое не должно быть null
import java.util.UUID; // Подключаем класс UUID для генерации уникальных идентификаторов

public class RestAssuredTest {
    private static final String TOKEN = System.getenv("GOREST_TOKEN"); // Получаем Bearer Token из переменной окружения

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

    @Test
    void getUserByIdTest() {
        int userId = 2;

        given()
                .pathParam("id", userId)
                .when()
                .get("https://jsonplaceholder.typicode.com/users/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(2));
    }

    @Test
    void getUserByUsernameTest() {
        given()
                .queryParam("username", "Bret")
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].username", equalTo("Bret"));
    }

    @Test                                      // JUnit: этот метод является тестом
    void deleteUserTest() {                    // Создаём тест удаления пользователя

        int userId = 2;                        // Сохраняем ID пользователя, которого хотим удалить

        given()                                // Начинаем формировать HTTP-запрос
                .pathParam("id", userId)       // Передаём значение 2 в Path Parameter {id}
                .when()                        // Переходим к отправке запроса
                .delete("https://jsonplaceholder.typicode.com/users/{id}") // Отправляем DELETE
                .then()                        // Начинаем проверять ответ сервера
                .statusCode(200);              // Проверяем, что сервер вернул HTTP 200
    }

    @Test                                      // JUnit: этот метод является тестом
    void updateUserTest() {                    // Создаём тест обновления пользователя

        int userId = 2;                        // ID пользователя, которого хотим обновить
                                                // Создаём JSON, который отправим серверу
        String requestBody = """                
            {
                "name": "Alex",
                "username": "alex123",
                "email": "alex@example.com"
            }
            """;

        given()                                // Начинаем формировать HTTP-запрос
                .contentType("application/json") // Говорим серверу, что отправляем JSON
                .pathParam("id", userId)       // Передаём ID пользователя в {id}
                .body(requestBody)             // Добавляем JSON-тело запроса
                .when()                        // Переходим к отправке запроса
                .put("https://jsonplaceholder.typicode.com/users/{id}") // Отправляем PUT
                .then()                        // Начинаем проверять ответ сервера
                .statusCode(200)               // Проверяем HTTP-статус 200
                .body("name", equalTo("Alex")) // Проверяем изменённое имя
                .body("username", equalTo("alex123")) // Проверяем username
                .body("email", equalTo("alex@example.com")); // Проверяем email
    }

    @Test // Помечаем метод как тест JUnit 5
    void patchUserTest() { // Создаем тест частичного изменения пользователя

        int userId = 2; // Сохраняем ID пользователя, которого будем изменять

        String requestBody = """ 
            {
                "email": "new@example.com"
            }
            """; // Создаем JSON-тело только с тем полем, которое хотим изменить

        given() // Начинаем формировать HTTP-запрос
                .contentType("application/json") // Указываем, что отправляем JSON
                .pathParam("id", userId) // Передаем ID пользователя как Path Parameter
                .body(requestBody) // Передаем JSON-тело запроса
                .when() // Переходим к отправке запроса
                .patch("https://jsonplaceholder.typicode.com/users/{id}") // Отправляем PATCH-запрос
                .then() // Переходим к проверке ответа
                .statusCode(200) // Проверяем, что сервер вернул HTTP 200
                .body("email", equalTo("new@example.com")); // Проверяем, что email в ответе изменился
    }

    @Test // Помечаем метод как тест JUnit 5
    void getUsersFieldsTest() { // Создаем тест проверки полей всех пользователей

        given() // Начинаем формировать GET-запрос
                .when() // Переходим к отправке запроса
                .get("https://jsonplaceholder.typicode.com/users") // Получаем список пользователей
                .then() // Переходим к проверке ответа
                .statusCode(200) // Проверяем, что сервер вернул HTTP 200
                .body("every { it.id != null }", equalTo(true)) // Проверяем, что у каждого пользователя есть id
                .body("every { it.name != null }", equalTo(true)) // Проверяем, что у каждого пользователя есть name
                .body("every { it.username != null }", equalTo(true)) // Проверяем, что у каждого пользователя есть username
                .body("every { it.email != null }", equalTo(true)); // Проверяем, что у каждого пользователя есть email
    }

    @Test // Помечаем метод как тест JUnit 5
    void createGoRestUserTest() { // Создаем тест создания пользователя через Go REST

        String email = "alex.java." + UUID.randomUUID() + "@example.com"; // Создаем уникальный email для каждого запуска

        String requestBody = """
            {
                "name": "Alex Java",
                "email": "%s",
                "gender": "male",
                "status": "active"
            }
            """.formatted(email); // Формируем JSON-тело и подставляем уникальный email

        given() // Начинаем формировать HTTP-запрос
                .header("Authorization", "Bearer " + TOKEN) // Передаем Bearer Token в Authorization
                .contentType("application/json") // Указываем, что тело запроса содержит JSON
                .body(requestBody) // Передаем JSON-тело запроса
                .when() // Переходим к отправке запроса
                .post("https://gorest.co.in/public/v2/users") // Отправляем POST-запрос на создание пользователя
                .then() // Переходим к проверке ответа
                .statusCode(201) // Проверяем, что пользователь успешно создан
                .body("name", equalTo("Alex Java")) // Проверяем имя созданного пользователя
                .body("email", equalTo(email)) // Проверяем email созданного пользователя
                .body("id", notNullValue()); // Проверяем, что сервер выдал пользователю id
    }
}
