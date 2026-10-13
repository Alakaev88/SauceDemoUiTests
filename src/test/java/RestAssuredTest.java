import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue; // Импортируем проверку значения, которое не должно быть null
import java.util.UUID; // Подключаем класс UUID для генерации уникальных идентификаторов
import io.restassured.response.Response; // Подключаем класс, который представляет HTTP-ответ
import static org.junit.jupiter.api.Assertions.assertEquals; // Подключаем JUnit-проверку равенства
import static org.junit.jupiter.api.Assertions.assertTrue; // Подключаем проверку условия true

public class RestAssuredTest {
    private static final String TOKEN = System.getenv("GOREST_TOKEN"); // Получаем Bearer Token из переменной окружения
    private static final String GOREST_URL = "https://gorest.co.in/public/v2/users";
    private static final String REQUEST_BODY = """
        {
            "name": "Alex Negative Test",
            "email": "alex.negative@example.com",
            "gender": "male",
            "status": "active"
        }
        """;  // Сохраняем JSON-тело запроса для негативных тестов

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

    @Test // Помечаем метод как тест JUnit 5
    void createUserWithoutTokenTest() { // Проверяем создание пользователя без токена

        given() // Начинаем формировать HTTP-запрос
                .contentType("application/json") // Указываем, что отправляем JSON
                .body(REQUEST_BODY) // Передаем JSON-тело запроса
                .when() // Переходим к отправке запроса
                .post(GOREST_URL) // Отправляем POST-запрос на создание пользователя
                .then() // Переходим к проверке ответа
                .statusCode(401) // Проверяем, что сервер требует авторизацию
                .body("message", equalTo("Authentication failed")); // Проверяем сообщение об отсутствии авторизации
    }

    @Test // Помечаем метод как тест JUnit 5
    void createUserWithInvalidTokenTest() { // Проверяем создание пользователя с неправильным токеном

        given() // Начинаем формировать HTTP-запрос
                .header("Authorization", "Bearer this-is-wrong-token") // Передаем заведомо неправильный Bearer Token
                .contentType("application/json") // Указываем, что отправляем JSON
                .body(REQUEST_BODY) // Передаем JSON-тело запроса
                .when() // Переходим к отправке запроса
                .post(GOREST_URL) // Отправляем POST-запрос на создание пользователя
                .then() // Переходим к проверке ответа
                .statusCode(401) // Проверяем, что неправильный токен не дает доступ
                .body("message", equalTo("Invalid token")); // Проверяем сообщение сервера о неправильном токене
    }

    @Test // Помечаем метод как тест JUnit 5
    void getUserResponseTest() { // Создаем тест для работы с объектом HTTP-ответа

        Response response = given() // Создаем переменную для хранения ответа сервера
                .when() // Переходим к отправке запроса
                .get("https://jsonplaceholder.typicode.com/users/1"); // Выполняем GET-запрос

        int statusCode = response.getStatusCode(); // Получаем HTTP-статус из ответа
        String contentType = response.getContentType(); // Получаем Content-Type из заголовков ответа
        String city = response.jsonPath().getString("address.city"); // Достаем city из JSON-ответа

        System.out.println(statusCode); // Выводим HTTP-статус в консоль
        System.out.println(contentType); // Выводим тип содержимого в консоль
        System.out.println(city); // Выводим город пользователя в консоль

        assertEquals(200, statusCode); // Проверяем, что сервер вернул HTTP 200
        assertEquals("Gwenborough", city); // Проверяем, что city соответствует ожидаемому значению
        assertTrue(contentType.contains("application/json")); // Проверяем, что сервер вернул JSON
    }
    @Test // Помечаем метод как тест JUnit 5
    void createAndGetUserTest() { // Создаем пользователя и затем получаем его по id
        System.out.println("TOKEN null: " + (TOKEN == null)); // Проверяем, есть ли токен вообще

        System.out.println(
                "TOKEN length: " + (TOKEN == null ? 0 : TOKEN.length())
        ); // Показываем длину токена, не раскрывая сам токен
        String email = "alex.chain." + UUID.randomUUID() + "@example.com"; // Создаем уникальный email

        String requestBody = """
            {
                "name": "Alex Chain",
                "email": "%s",
                "gender": "male",
                "status": "active"
            }
            """.formatted(email); // Формируем JSON-тело с уникальным email

        Response createResponse = given() // Начинаем формировать POST-запрос
                .header("Authorization", "Bearer " + TOKEN) // Передаем Bearer Token
                .contentType("application/json") // Указываем, что отправляем JSON
                .body(requestBody) // Передаем JSON-тело запроса
                .when() // Переходим к отправке запроса
                .post(GOREST_URL); // Создаем нового пользователя


        System.out.println(createResponse.getStatusCode()); // Выводим статус POST-запроса
        System.out.println(createResponse.getBody().asString()); // Выводим тело ответа сервера

        assertEquals(201, createResponse.getStatusCode()); // Сначала проверяем, что POST действительно создал пользователя

        int userId = createResponse.jsonPath().getInt("id"); // Только после успешного POST получаем id

        Response getResponse = given() // Начинаем формировать GET-запрос
                .header("Authorization", "Bearer " + TOKEN) // Передаем тот же Bearer Token
                .pathParam("id", userId) // Передаем id созданного пользователя
                .when() // Переходим к отправке запроса
                .get(GOREST_URL + "/{id}"); // Получаем пользователя по его id

        assertEquals(200, getResponse.getStatusCode()); // Проверяем успешный GET-запрос
        assertEquals(userId, getResponse.jsonPath().getInt("id")); // Проверяем, что id совпадает
        assertEquals("Alex Chain", getResponse.jsonPath().getString("name")); // Проверяем имя пользователя
        assertEquals(email, getResponse.jsonPath().getString("email")); // Проверяем email пользователя
    }

    @Test // Помечаем метод как тест JUnit 5
    void createUpdateAndGetUserTest() { // Создаем пользователя, обновляем его и затем проверяем изменения

        String email = "alex.update." + UUID.randomUUID() + "@example.com"; // Создаем уникальный email для нового пользователя

        String requestBody = """
            {
                "name": "Alex Update",
                "email": "%s",
                "gender": "male",
                "status": "active"
            }
            """.formatted(email); // Формируем JSON-тело для создания пользователя

        Response createResponse = given() // Начинаем формировать POST-запрос
                .header("Authorization", "Bearer " + TOKEN) // Передаем Bearer Token
                .contentType("application/json") // Указываем, что отправляем JSON
                .body(requestBody) // Передаем тело запроса
                .when() // Переходим к отправке запроса
                .post(GOREST_URL); // Создаем пользователя

        assertEquals(201, createResponse.getStatusCode()); // Проверяем успешное создание пользователя

        int userId = createResponse.jsonPath().getInt("id"); // Получаем id созданного пользователя

        String newEmail = "alex.changed." + UUID.randomUUID() + "@example.com"; // Создаем новый уникальный email

        String patchBody = """
            {
                "email": "%s"
            }
            """.formatted(newEmail); // Формируем JSON только с изменяемым полем email

        Response patchResponse = given() // Начинаем формировать PATCH-запрос
                .header("Authorization", "Bearer " + TOKEN) // Передаем Bearer Token
                .contentType("application/json") // Указываем JSON
                .pathParam("id", userId) // Передаем id пользователя
                .body(patchBody) // Передаем JSON с новым email
                .when() // Переходим к отправке запроса
                .patch(GOREST_URL + "/{id}"); // Частично обновляем пользователя

        assertEquals(200, patchResponse.getStatusCode()); // Проверяем успешное обновление
        assertEquals(newEmail, patchResponse.jsonPath().getString("email")); // Проверяем email в ответе PATCH

        Response getResponse = given() // Начинаем формировать GET-запрос
                .header("Authorization", "Bearer " + TOKEN) // Передаем Bearer Token
                .pathParam("id", userId) // Передаем тот же id
                .when() // Переходим к отправке запроса
                .get(GOREST_URL + "/{id}"); // Получаем пользователя после обновления

        assertEquals(200, getResponse.getStatusCode()); // Проверяем успешный GET
        assertEquals(userId, getResponse.jsonPath().getInt("id")); // Проверяем id пользователя
        assertEquals(newEmail, getResponse.jsonPath().getString("email")); // Проверяем, что новый email сохранился
    }

}
