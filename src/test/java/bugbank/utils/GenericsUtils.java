package bugbank.utils;

import bugbank.domain.Transacao;
import bugbank.domain.Usuario;

import org.openqa.selenium.WebDriver;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static bugbank.pages.ExtratoPage.*;
import static bugbank.pages.GenericsPage.*;
import static bugbank.pages.LoginPage.*;
import static org.junit.jupiter.api.Assertions.*;

public class GenericsUtils {

    public static void extrato(WebDriver driver, Transacao transacao, String tipoTransacao)
    {
        esperaPorElementoByCssSelector(driver,"p#textDateTransaction",10);

        String dataUltimaTransacao = datasTransacoes(driver).get(datasTransacoes(driver).size()-1).getText();

        String pattern = "dd/MM/yyyy";
        String dataAtual = new SimpleDateFormat(pattern).format(new Date());

        assertEquals(dataAtual, dataUltimaTransacao);

        String tipoUltimaTransacao = tipoTransacoes(driver).get(tipoTransacoes(driver).size()-1).getText();
        assertEquals(tipoTransacao, tipoUltimaTransacao);

        String descricaoUltimaTransacao = descricaoTransacoes(driver).get(descricaoTransacoes(driver).size()-1).getText();
        assertEquals(transacao.getDescricao(), descricaoUltimaTransacao);

        String valorUltimaTransacao = valorTransacoes(driver).get(valorTransacoes(driver).size()-1).getText();
        String classes = valorTransacoes(driver).get(valorTransacoes(driver).size()-1).getAttribute("class");

        if(tipoTransacao.equals("Transferência enviada")) {
            assertTrue(classes.contains("gvXfbP"));
            assertEquals("-R$ "+Double.toString(transacao.getValor()).replace(".", ","), valorUltimaTransacao);

            String cor = classeValorTransacao(driver).getCssValue("color");
            assertEquals("rgba(255, 0, 0, 1)", cor);
        } else if (tipoTransacao.equals("Transferência recebida")) {
            assertTrue(classes.contains("7n8vh8"));
            assertEquals("R$ "+Double.toString(transacao.getValor()).replace(".", ","), valorUltimaTransacao);
        }
    }

    public static void login (WebDriver driver, Usuario usuario)
    {
        cmpEmailLogin(driver).click();
        cmpEmailLogin(driver).sendKeys(usuario.getEmail());

        cmpSenhaLogin(driver).click();
        cmpSenhaLogin(driver).sendKeys(usuario.getSenha());

        btnAcessar(driver).click();
    }

    public static String trataEmail(String str) {
        str = str.replaceAll("\\s+","").replaceAll("\\.","").toLowerCase();

        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
