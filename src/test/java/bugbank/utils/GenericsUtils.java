package bugbank.utils;

import bugbank.domain.Transferencia;
import bugbank.domain.Usuario;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericsUtils {
    public static void registrarUsuario(WebDriver driver, Usuario usuario, boolean temSaldo) {
        WebElement btnRegistrar = driver.findElement(By.cssSelector(".ihdmxA"));
        btnRegistrar.click();

        WebElement cmpEmail = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/form/div[2]/input"));
        cmpEmail.click();
        cmpEmail.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        cmpEmail.sendKeys(Keys.BACK_SPACE);
        cmpEmail.sendKeys(usuario.getEmail());

        WebElement cmpNome = driver.findElement(By.name("name"));
        cmpNome.click();
        cmpNome.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        cmpNome.sendKeys(Keys.BACK_SPACE);
        cmpNome.sendKeys(usuario.getNome());

        WebElement cmpSenha = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[2]/form/div[4]/div/input"));
        cmpSenha.click();
        cmpSenha.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        cmpSenha.sendKeys(Keys.BACK_SPACE);
        cmpSenha.sendKeys(usuario.getSenha());

        WebElement cmpConfirmSenha = driver.findElement(By.name("passwordConfirmation"));
        cmpConfirmSenha.click();
        cmpConfirmSenha.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        cmpConfirmSenha.sendKeys(Keys.BACK_SPACE);
        cmpConfirmSenha.sendKeys(usuario.getSenha());

        List<WebElement> labels = driver.findElements(By.tagName("label"));
        for(WebElement label: labels )
        {
            String classes = label.getAttribute("class");
            if((temSaldo) && (classes.contains("kIwoPV"))) {
                WebElement chkDefineSaldo = driver.findElement(By.id("toggleAddBalance"));
                chkDefineSaldo.click();
            }
        }

        WebElement btnCadastrar = driver.findElement(By.cssSelector("button.style__ContainerButton-sc-1wsixal-0.CMabB.button__child"));
        btnCadastrar.click();

        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(10));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalText")));

        WebElement modalSucesso = driver.findElement(By.id("modalText"));
        String textoModal = modalSucesso.getText();

        assertEquals("A conta", textoModal.substring(0, 7));
        assertEquals("foi criada com sucesso", textoModal.substring(textoModal.length() - 22));

        String stringConta = textoModal.substring(8, 13);
        int numeroConta = Integer.parseInt(stringConta.substring(0, 3)), digitoConta = Integer.parseInt(stringConta.substring(stringConta.length() - 1));
        usuario.setNumeroConta(numeroConta);
        usuario.setDigitoConta(digitoConta);

        WebElement btnfecharModal = driver.findElement(By.id("btnCloseModal"));
        btnfecharModal.click();
    }

    public static void realizarLogin(WebDriver driver, Usuario usuarioLogado, Usuario usuarioAReceberTransferencia) {

        login (driver, usuarioLogado);

        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(10));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("textName")));

        WebElement pSaudacao = driver.findElement(By.id("textName"));
        String textoSaudacao = pSaudacao.getText();

        assertEquals("Olá "+usuarioLogado.getNome()+",", textoSaudacao);

        WebElement pConta = driver.findElement(By.id("textAccountNumber"));
        String textoConta= pConta.getText();

        assertEquals("Conta digital: "+ usuarioLogado.retornaContaTratada(), textoConta);

        WebElement pSaldo = driver.findElement(By.id("textBalance"));
        WebElement spanSaldo= pSaldo.findElement(By.tagName("span"));
        String textoSpan = spanSaldo.getText();
        String saldoTratado = textoSpan.replace("R$ ", "").replace(",00","").replace(".","");

        usuarioLogado.setSaldo(Float.parseFloat(saldoTratado));

        double numRandom = (Math.random() * usuarioLogado.getSaldo()) + 1;
        BigDecimal bd = new BigDecimal(numRandom+ 0.01).setScale(2, RoundingMode.HALF_EVEN);

        Transferencia transferencia = new Transferencia (usuarioAReceberTransferencia, bd.doubleValue(), "Teste");

        realizarTransferencia(driver, transferencia);

        validarExtratoTransferente(driver, transferencia);

        validarExtratoRecebedor(driver, usuarioAReceberTransferencia, transferencia);
    }

    private static void validarExtratoTransferente(WebDriver driver, Transferencia transferencia) {

        WebElement btnExtrato = driver.findElement(By.id("btn-EXTRATO"));
        btnExtrato.click();

        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(10));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p#textDateTransaction")));

        List<WebElement> datasTransacoes = driver.findElements(By.cssSelector("p#textDateTransaction"));
        String dataUltimaTransacao = datasTransacoes.get(datasTransacoes.size()-1).getText();

        String pattern = "dd/MM/yyyy";
        String dataAtual = new SimpleDateFormat(pattern).format(new Date());

        assertEquals(dataAtual, dataUltimaTransacao);

        List<WebElement> tipoTransacoes = driver.findElements(By.cssSelector("p#textTypeTransaction"));
        String tipoUltimaTransacao = tipoTransacoes.get(tipoTransacoes.size()-1).getText();
        assertEquals("Transferência enviada", tipoUltimaTransacao);

        List<WebElement> descricaoTransacoes = driver.findElements(By.cssSelector("p#textDescription"));
        String descricaoUltimaTransacao = descricaoTransacoes.get(descricaoTransacoes.size()-1).getText();
        assertEquals(transferencia.getDescricao(), descricaoUltimaTransacao);

        List<WebElement> valorTransacoes = driver.findElements(By.cssSelector("p#textTransferValue"));
        String valorUltimaTransacao = valorTransacoes.get(valorTransacoes.size()-1).getText();
        assertEquals("-R$ "+Double.toString(transferencia.getValor()).replace(".", ","), valorUltimaTransacao);

        String classes = valorTransacoes.get(valorTransacoes.size()-1).getAttribute("class");
        assertTrue(classes.contains("gvXfbP"));

        WebElement classeValorTransacao = driver.findElement(By.cssSelector(".gvXfbP"));

        String cor = classeValorTransacao.getCssValue("color");
        assertEquals("rgba(255, 0, 0, 1)", cor);
    }

    private static void validarExtratoRecebedor(WebDriver driver, Usuario usuario, Transferencia transferencia) {
        WebElement btnSair = driver.findElement(By.id("btnExit"));
        btnSair.click();

        login (driver, usuario);

        WebElement btnExtrato = driver.findElement(By.id("btn-EXTRATO"));
        btnExtrato.click();

        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(10));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p#textDateTransaction")));

        List<WebElement> datasTransacoes = driver.findElements(By.cssSelector("p#textDateTransaction"));
        String dataUltimaTransacao = datasTransacoes.get(datasTransacoes.size()-1).getText();

        String pattern = "dd/MM/yyyy";
        String dataAtual = new SimpleDateFormat(pattern).format(new Date());

        assertEquals(dataAtual, dataUltimaTransacao);

        List<WebElement> tipoTransacoes = driver.findElements(By.cssSelector("p#textTypeTransaction"));
        String tipoUltimaTransacao = tipoTransacoes.get(tipoTransacoes.size()-1).getText();
        assertEquals("Transferência recebida", tipoUltimaTransacao);

        List<WebElement> descricaoTransacoes = driver.findElements(By.cssSelector("p#textDescription"));
        String descricaoUltimaTransacao = descricaoTransacoes.get(descricaoTransacoes.size()-1).getText();
        assertEquals(transferencia.getDescricao(), descricaoUltimaTransacao);

        List<WebElement> valorTransacoes = driver.findElements(By.cssSelector("p#textTransferValue"));
        String valorUltimaTransacao = valorTransacoes.get(valorTransacoes.size()-1).getText();
        assertEquals("R$ "+Double.toString(transferencia.getValor()).replace(".", ","), valorUltimaTransacao);

        String classes = valorTransacoes.get(valorTransacoes.size()-1).getAttribute("class");
        assertTrue(classes.contains("7n8vh8"));

    }

    private static void realizarTransferencia(WebDriver driver, Transferencia transferencia) {

        WebElement btnTransferencia = driver.findElement(By.id("btn-TRANSFERÊNCIA"));
        btnTransferencia.click();

        WebDriverWait block = new WebDriverWait(driver, Duration.ofSeconds(10));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.name("accountNumber")));

        WebElement cmpConta = driver.findElement(By.name("accountNumber"));
        cmpConta.click();
        cmpConta.sendKeys(String.valueOf(transferencia.getRecebedor().getNumeroConta()));

        WebElement cmpDigito = driver.findElement(By.name("digit"));
        cmpDigito.click();
        cmpDigito.sendKeys(String.valueOf(transferencia.getRecebedor().getDigitoConta()));

        WebElement cmpValorTranferencia = driver.findElement(By.name("transferValue"));
        cmpValorTranferencia.click();
        cmpValorTranferencia.sendKeys(String.valueOf(transferencia.getValor()));

        WebElement cmpDescricaoTranferencia = driver.findElement(By.name("description"));
        cmpDescricaoTranferencia.click();
        cmpDescricaoTranferencia.sendKeys(String.valueOf(transferencia.getDescricao()));

        WebElement btnTransferir = driver.findElement(By.cssSelector(".CMabB"));
        btnTransferir.click();

        block = new WebDriverWait(driver, Duration.ofSeconds(10));
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("modalText")));

        WebElement modalSucesso = driver.findElement(By.id("modalText"));
        String textoModal = modalSucesso.getText();

        assertEquals("Transferencia realizada com sucesso", textoModal);

        WebElement btnfecharModal = driver.findElement(By.id("btnCloseModal"));
        btnfecharModal.click();

        WebElement btnVoltar = driver.findElement(By.id("btnBack"));
        btnVoltar.click();
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static void login (WebDriver driver, Usuario usuario)
    {
        WebElement cmpEmail = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[1]/form/div[1]/input"));
        cmpEmail.click();
        cmpEmail.sendKeys(usuario.getEmail());

        WebElement cmpSenha = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div[2]/div/div[1]/form/div[2]/div/input"));
        cmpSenha.click();
        cmpSenha.sendKeys(usuario.getSenha());

        WebElement btnAcessar = driver.findElement(By.cssSelector(".otUnI"));
        btnAcessar.click();
    }
}
