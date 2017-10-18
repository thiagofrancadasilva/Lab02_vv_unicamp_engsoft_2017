package br.unicamp.bookstore.entrega;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = { "pretty", "html:target/cucumber" },
        glue = "br.unicamp.bookstore.entrega",
        features = "classpath:features/ConsultarEntrega.feature"
)

public class RunConsultarEntregaTest {

}
