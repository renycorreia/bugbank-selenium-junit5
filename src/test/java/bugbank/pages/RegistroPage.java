package bugbank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RegistroPage {
    private static WebElement element = null;

    public static WebElement cmpEmailRegistro(WebDriver driver) {
        element = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/form/div[2]/input"));
        return element;
    }

    public static WebElement cmpNomeRegistro(WebDriver driver) {
        element = driver.findElement(By.name("name"));
        return element;
    }

    public static WebElement cmpSenhaRegistro(WebDriver driver) {
        element = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/form/div[4]/div/input"));
        return element;
    }

    public static WebElement cmpConfirmSenhaRegistro(WebDriver driver) {
        element = driver.findElement(By.name("passwordConfirmation"));
        return element;
    }

    public static WebElement chkDefineSaldoRegistro(WebDriver driver) {
        element = driver.findElement(By.id("toggleAddBalance"));
        return element;
    }

    public static List<WebElement> labelsClasseChkDefineSaldoRegistro(WebDriver driver) {
        return driver.findElements(By.tagName("label"));
    }

    public static WebElement btnCadastrar(WebDriver driver) {
        element = driver.findElement(By.cssSelector("button.style__ContainerButton-sc-1wsixal-0.CMabB.button__child"));
        return element;
    }

}
