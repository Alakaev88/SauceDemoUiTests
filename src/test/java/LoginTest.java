import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    LoginPage loginPage;
    WebDriver driver;
    ProductsPage productsPage;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
    }

    @Test
    void successfulLoginTest() {

    loginPage.login("standard_user","secret_sauce");

        String actualText = productsPage.getPageTitle();
        System.out.println(actualText);
        assertEquals("Products", actualText);
    }

    @Test
    void invalidPasswordTest(){
        loginPage.login("standard_user","wrong_password");

        String errorText = loginPage.getErrorMessage();
        System.out.println(errorText);
        assertEquals("Epic sadface: Username and password do not match any user in this service", errorText);
    }

    @Test
    void invalidUsernameTest(){
     loginPage.login("wrong_user","secret_sauce");

        String errorText = loginPage.getErrorMessage();
        System.out.println(errorText);
        assertEquals("Epic sadface: Username and password do not match any user in this service", errorText);
    }
    @Test
    void invalidUsernameAndPasswordTest(){
       loginPage.login("wrong_user","wrong_password");

        String errorText = loginPage.getErrorMessage();
        System.out.println(errorText);
        assertEquals("Epic sadface: Username and password do not match any user in this service", errorText);
    }

    @AfterEach
    void tearDown(){
        driver.quit();

    }
}