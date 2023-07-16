package bugbank.tests;

import bugbank.domain.Transacao;
import bugbank.domain.Usuario;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static bugbank.pages.GenericsPage.*;
import static bugbank.pages.GenericsPage.btnfecharModal;
import static bugbank.pages.HomePage.*;
import static bugbank.pages.LoginPage.btnRegistrar;
import static bugbank.pages.RegistroPage.*;
import static bugbank.pages.TransferenciaPage.*;
import static bugbank.pages.TransferenciaPage.btnVoltar;
import static bugbank.utils.GenericsUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferenciaEntreNovasContasTests {
	WebDriver driver = new ChromeDriver();

	@BeforeEach
	void setUp(){
		driver.get("https://bugbank.netlify.app/");
	}

	@AfterEach
	void tearDown(){
		driver.quit();
	}

	@Test
	void testE2E() {

		Faker faker = new Faker(new Locale("pt_BR"));
		String name, email, senha;

		name = faker.name().fullName();
		email = trataEmail(name)+"@mail.com";
		senha = "Abd@1235";
		Usuario usuario1 = new Usuario(name, email, senha);

		name = faker.name().fullName();
		email = trataEmail(name)+"@mail.com";
		Usuario usuario2 = new Usuario(name, email, senha);

		registrarUsuario(driver, usuario1, true);
		registrarUsuario(driver, usuario2, true);
		usuario1 = realizarLogin(driver, usuario1);
		atualizarSaldo(driver, usuario1);
		Transacao transacao = realizarTransferencia(driver, usuario1, usuario2);
		validarExtratoTransferente(driver, transacao);
		validarExtratoRecebedor(driver, usuario2, transacao);
	}

	public void registrarUsuario(WebDriver driver, Usuario usuario, boolean temSaldo) {
		btnRegistrar(driver).click();

		cmpEmailRegistro(driver).click();
		cmpEmailRegistro(driver).clear();
		cmpEmailRegistro(driver).sendKeys(usuario.getEmail());

		cmpNomeRegistro(driver).click();
		cmpNomeRegistro(driver).clear();
		cmpNomeRegistro(driver).sendKeys(usuario.getNome());

		cmpSenhaRegistro(driver).click();
		cmpSenhaRegistro(driver).clear();
		cmpSenhaRegistro(driver).sendKeys(usuario.getSenha());

		cmpConfirmSenhaRegistro(driver).click();
		cmpConfirmSenhaRegistro(driver).clear();
		cmpConfirmSenhaRegistro(driver).sendKeys(usuario.getSenha());

		List<WebElement> labels = labelsClasseChkDefineSaldoRegistro(driver);
		for(WebElement label: labels )
		{
			String classes = label.getAttribute("class");
			if((temSaldo) && (classes.contains("kIwoPV"))) {
				chkDefineSaldoRegistro(driver).click();
			}
		}

		btnCadastrar(driver).click();

		esperaPorElementoById(driver, "modalText",10);

		String textoModal = txtMotal(driver).getText();
		assertEquals("A conta", textoModal.substring(0, 7));
		assertEquals("foi criada com sucesso", textoModal.substring(textoModal.length() - 22));

		String stringConta = textoModal.split(" ")[2];
		int numeroConta = Integer.parseInt(stringConta.split("-")[0]), digitoConta = Integer.parseInt(stringConta.split("-")[1]);
		usuario.setNumeroConta(numeroConta);
		usuario.setDigitoConta(digitoConta);

		btnfecharModal(driver).click();
	}

	public Usuario realizarLogin(WebDriver driver, Usuario usuarioLogado) {

		login (driver, usuarioLogado);

		esperaPorElementoById(driver, "textName",10);

		String textoSaudacao = pSaudacao(driver).getText();
		assertEquals("Olá "+usuarioLogado.getNome()+",", textoSaudacao);

		String textoConta= pConta(driver).getText();
		assertEquals("Conta digital: "+ usuarioLogado.retornaContaTratada(), textoConta);

		return usuarioLogado;
	}

	public void atualizarSaldo(WebDriver driver, Usuario usuario) {
		String textoSpan = spanValorSaldo(driver).getText();
		String saldoTratado = textoSpan.replace("R$ ", "").replace(",00","").replace(".","");
		usuario.setSaldo(Float.parseFloat(saldoTratado));
	}

	public Transacao realizarTransferencia(WebDriver driver, Usuario usuarioLogado, Usuario usuarioAReceberTransferencia) {

		double numRandom = (Math.random() * usuarioLogado.getSaldo()) + 1;

		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		String formattedNumber = decimalFormat.format(numRandom);

		if (formattedNumber.charAt(formattedNumber.length() - 1) == '0') {
			numRandom += 0.01;
		}

		BigDecimal bd = new BigDecimal(numRandom).setScale(2, RoundingMode.HALF_EVEN);

		Transacao transacao = new Transacao(usuarioAReceberTransferencia, bd.doubleValue(), "Teste");

		btnTransferencia(driver).click();

		esperaPorElementoByName(driver, "accountNumber",10);

		cmpConta(driver).click();
		cmpConta(driver).sendKeys(String.valueOf(transacao.getRecebedor().getNumeroConta()));

		cmpDigitoConta(driver).click();
		cmpDigitoConta(driver).sendKeys(String.valueOf(transacao.getRecebedor().getDigitoConta()));

		cmpValorTranferencia(driver).click();
		cmpValorTranferencia(driver).sendKeys(String.valueOf(transacao.getValor()));

		cmpDescricaoTranferencia(driver).click();
		cmpDescricaoTranferencia(driver).sendKeys(String.valueOf(transacao.getDescricao()));

		btnTransferir(driver).click();

		esperaPorElementoById(driver, "modalText",10);

		String textoModal = txtMotal(driver).getText();
		assertEquals("Transferencia realizada com sucesso", textoModal);

		btnfecharModal(driver).click();

		btnVoltar(driver).click();

		return transacao;
	}

	public void validarExtratoTransferente(WebDriver driver, Transacao transacao) {

		btnExtrato(driver).click();
		extrato(driver, transacao, "Transferência enviada");
	}

	public void validarExtratoRecebedor(WebDriver driver, Usuario usuario, Transacao transacao) {
		WebElement btnSair = driver.findElement(By.id("btnExit"));
		btnSair.click();

		login (driver, usuario);

		WebElement btnExtrato = driver.findElement(By.id("btn-EXTRATO"));
		btnExtrato.click();

		extrato(driver, transacao, "Transferência recebida");
	}

}
