package bugbank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
public class TransferenciaPage {

    private static WebElement element = null;

    public static WebElement cmpConta(WebDriver driver) {
        element = driver.findElement(By.name("accountNumber"));
        return element;
    }

    public static WebElement cmpDigitoConta(WebDriver driver) {
        element = driver.findElement(By.name("digit"));
        return element;
    }

    public static WebElement cmpValorTranferencia(WebDriver driver) {
        element = driver.findElement(By.name("transferValue"));
        return element;
    }

    public static WebElement cmpDescricaoTranferencia(WebDriver driver) {
        element = driver.findElement(By.name("description"));
        return element;
    }

    public static WebElement btnTransferir(WebDriver driver) {
        element = driver.findElement(By.cssSelector(".CMabB"));
        return element;
    }

    public static WebElement btnVoltar(WebDriver driver) {
        element = driver.findElement(By.id("btnBack"));
        return element;
    }

}
