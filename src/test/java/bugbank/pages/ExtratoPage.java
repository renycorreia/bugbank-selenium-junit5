package bugbank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ExtratoPage {

    private static List<WebElement> elements = null;

    public static List<WebElement> datasTransacoes(WebDriver driver) {
        elements = driver.findElements(By.cssSelector("p#textDateTransaction"));
        return elements;
    }

    public static List<WebElement> tipoTransacoes(WebDriver driver) {
        elements = driver.findElements(By.cssSelector("p#textTypeTransaction"));
        return elements;
    }

    public static List<WebElement> descricaoTransacoes(WebDriver driver) {
        elements = driver.findElements(By.cssSelector("p#textDescription"));
        return elements;
    }

    public static List<WebElement> valorTransacoes(WebDriver driver) {
        elements = driver.findElements(By.cssSelector("p#textTransferValue"));
        return elements;
    }

    public static WebElement classeValorTransacao(WebDriver driver) {
        return driver.findElement(By.cssSelector(".gvXfbP"));
    }

}
