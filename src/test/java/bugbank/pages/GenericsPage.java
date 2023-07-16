package bugbank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class GenericsPage {

    private static WebElement element = null;

    public static void esperaPorElementoById (WebDriver driver, String elemento, int duracaoEmSegundos)
    {
        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(duracaoEmSegundos));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id(elemento)));
    }

    public static void esperaPorElementoByName (WebDriver driver, String elemento, int duracaoEmSegundos)
    {
        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(duracaoEmSegundos));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.name(elemento)));
    }

    public static void esperaPorElementoByCssSelector (WebDriver driver, String elemento, int duracaoEmSegundos)
    {
        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(duracaoEmSegundos));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(elemento)));
    }

    public static WebElement txtMotal(WebDriver driver) {
        element = driver.findElement(By.id("modalText"));
        return element;
    }

    public static WebElement btnfecharModal(WebDriver driver) {
        element = driver.findElement(By.id("btnCloseModal"));
        return element;
    }
}
