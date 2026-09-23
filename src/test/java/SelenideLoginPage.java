import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SelenideLoginPage {

    private SelenideElement usernameField = $("#user-name");
    private SelenideElement passwordField = $("#password");
    private SelenideElement loginButton = $("#login-button");
    private SelenideElement errorMessage = $("[data-test='error']");

    @Step("Авторизация пользователя")
    public void login(String username, String password){

        usernameField.setValue(username);
        passwordField.setValue(password);
        loginButton.click();
    }

    public SelenideElement getErrorMessage() {
        return errorMessage;
    }
}
