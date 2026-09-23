import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import io.qameta.allure.Allure;

public class SelenideLoginTest {
    static {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    SelenideLoginPage loginPage = new SelenideLoginPage();
    SelenideProductsPage productsPage = new SelenideProductsPage();

    @BeforeEach
    void setUP(){
        open("https://www.saucedemo.com/");
    }
    @Test
    void successfulLoginTest() {

         loginPage.login("standard_user", "secret_sauce");
        Allure.step("Проверить результат авторизации", () -> {
            productsPage.getPageTitle().shouldHave(text("Products"));
        });
    }
    @ParameterizedTest
    @CsvSource({
            "standard_user, wrong_password",
            "wrong_user, secret_sauce"
    })
    void invalidCredentialsTest(String username, String password){

        loginPage.login(username,password);
        loginPage.getErrorMessage().shouldHave(text
                ("Epic sadface: Username and password do not match any user in this service"));
  }
}
