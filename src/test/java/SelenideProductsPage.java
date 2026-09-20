import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SelenideProductsPage {
    private SelenideElement pageTitle = $("[data-test='title']");

    public SelenideElement getPageTitle(){
        return pageTitle;
    }
}
