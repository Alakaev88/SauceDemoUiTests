import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    public LoginPage(WebDriver driver){
        this.driver = driver;
    }
    public void login(String username, String password) {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(usernameField)
                .sendKeys(username);

        driver.findElement(passwordField)
                .sendKeys(password);

        driver.findElement(loginButton)
                .click();
    }
    public String getErrorMessage(){
        return  driver.findElement(errorMessage).getText();

    }
}
