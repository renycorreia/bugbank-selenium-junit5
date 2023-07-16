package bugbank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private static WebElement element = null;

    public static WebElement cmpEmailLogin(WebDriver driver) {
        element = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[1]/form/div[1]/input"));
        return element;
    }

    public static WebElement cmpSenhaLogin(WebDriver driver) {
        element = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[1]/form/div[2]/div/input"));
        return element;
    }

    public static WebElement btnAcessar(WebDriver driver) {
        element = driver.findElement(By.cssSelector(".otUnI"));
        return element;
    }

    public static WebElement btnRegistrar(WebDriver driver) {
        element = driver.findElement(By.cssSelector(".ihdmxA"));
        return element;
    }

}
