package bugbank.tests;

import bugbank.domain.Usuario;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Locale;

import static bugbank.utils.GenericsUtils.*;

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
		email = name.replaceAll("\\s+","").replaceAll("\\.","").toLowerCase()+"@mail.com";
		senha = "Abd@1235";
		Usuario usuario1 = new Usuario(name, email, senha);

		name = faker.name().fullName();
		email = name.replaceAll("\\s+","").replaceAll("\\.","").toLowerCase()+"@mail.com";
		Usuario usuario2 = new Usuario(name, email, senha);

		registrarUsuario(driver, usuario1, true);
		registrarUsuario(driver, usuario2, true);

		realizarLogin(driver, usuario1, usuario2);
	}

}
