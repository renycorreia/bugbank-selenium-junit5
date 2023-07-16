package bugbank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
public class HomePage {
    private static WebElement element = null;

    public static WebElement pSaudacao(WebDriver driver) {
        element = driver.findElement(By.id("textName"));
        return element;
    }

    public static WebElement pConta(WebDriver driver) {
        element = driver.findElement(By.id("textAccountNumber"));
        return element;
    }

    public static WebElement pTextoSaldo(WebDriver driver) {
        element = driver.findElement(By.id("textBalance"));
        return element;
    }

    public static WebElement spanValorSaldo(WebDriver driver) {
        element = pTextoSaldo(driver).findElement(By.tagName("span"));
        return element;
    }

    public static WebElement btnTransferencia(WebDriver driver) {
        element = driver.findElement(By.id("btn-TRANSFERÊNCIA"));
        return element;
    }

    public static WebElement btnExtrato(WebDriver driver) {
        element = driver.findElement(By.id("btn-EXTRATO"));
        return element;
    }

}
