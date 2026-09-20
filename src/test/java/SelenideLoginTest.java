import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SelenideLoginTest {

    SelenideLoginPage loginPage = new SelenideLoginPage();
    SelenideProductsPage productsPage = new SelenideProductsPage();

    @Test
    void successfulLoginTest() {
         open("https://www.saucedemo.com/");

         loginPage.login("standard_user", "secret_sauce");

         productsPage.getPageTitle().shouldHave(text("Products"));
        // $("[data-test='title']").shouldHave(text("Products"));
    }
    @Test
    void invalidPasswordTest() {
        open("https://www.saucedemo.com/");

        loginPage.login("standard_user", "wrong_password");

        loginPage.getErrorMessage().shouldHave(text
                ("Epic sadface: Username and password do not match any user in this service"));
//        $("[data-test='error']").shouldHave(text
//                ("Epic sadface: Username and password do not match any user in this service"));
    }
}
