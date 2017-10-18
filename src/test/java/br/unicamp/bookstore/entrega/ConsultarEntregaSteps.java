package br.unicamp.bookstore.entrega;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.github.tomakehurst.wiremock.WireMockServer;

import br.unicamp.bookstore.Configuracao;
import br.unicamp.bookstore.model.Endereco;
import br.unicamp.bookstore.model.StatusEncomenda;
import br.unicamp.bookstore.service.BuscaEnderecoService;
import br.unicamp.bookstore.service.ConsultarEntregaService;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;

public class ConsultarEntregaSteps {
	
	public WireMockServer wireMockServer;

	@Mock
	private Configuracao configuration;
 
	@InjectMocks
	private ConsultarEntregaService consultarEntregaService;

	private StatusEncomenda statusEncomenda;

	private String protocolo;

	private Throwable throwable;


	@Before
	public void setUp() {
		wireMockServer = new WireMockServer(9876);
		wireMockServer.start();
		MockitoAnnotations.initMocks(this);
		Mockito.when(configuration.getStatusEntregaUrl()).thenReturn("http://localhost:9876/ws");
		statusEncomenda = null;
		protocolo = null;
		throwable = null;
	}

	@After
	public void teardown() {
		wireMockServer.stop();
	}

	@Dado("^um PROTOCOLO valido:$")
	public void eu_possuo_um_CEP_valido(Map<String, String> map) throws Throwable {
		protocolo = map.get("protocolo");
		wireMockServer.stubFor(get(urlMatching("/ws/"+ protocolo + ".*")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "text/xml").withBodyFile("resultado-pesquisa-BuscaEndereco.xml")));
	}

	@Dado("^um PROTOCOLO nao existente:$")
	public void um_CEP_nao_existente(Map<String, String> map) throws Throwable {
		protocolo = map.get("cep");
		wireMockServer.stubFor(get(urlMatching("/ws/" + protocolo + ".*")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "text/xml").withBodyFile("resultado-pesquisa-BuscaEndereco_ERR.xml")));

	}

	@Dado("^um PROTOCOLO invalido:")
	public void um_CEP_invalido(Map<String, String> map) throws Throwable {
		protocolo = map.get("cep");
		wireMockServer.stubFor(get(urlMatching("/ws/" + protocolo + ".*"))
				.willReturn(aResponse().withStatus(400).withHeader("Content-Type", "text/xml")
						.withBodyFile("resultado-pesquisa-BuscaEndereco_BAD.xml")));
	}

	@Quando("^eu informo o PROTOCOLO na consulta de entrega$")
	public void eu_informo_o_CEP_na_busca_de_endereco() throws Throwable {
		throwable = catchThrowable(() -> this.statusEncomenda = consultarEntregaService.buscar(protocolo));
	}

	@Entao("^o resultado deve ser$")
	public void o_resultado_deve_ser_o_endereco(List<Map<String,String>> resultado)
			throws Throwable {
		assertThat(this.statusEncomenda.getTipo()).isEqualTo(resultado.get(0).get("Tipo"));
		assertThat(this.statusEncomenda.getStatus()).isEqualTo(resultado.get(0).get("Status"));
		assertThat(throwable).isNull();
	}

	@Entao("^o retorno deve conter um valor de erro igual a \"([^\"]*)\"$")
	public void o_retorno_deve_conter_um_valor_de_erro_igual_a(String erro) throws Throwable {
		assertThat(StatusEncomenda.getErro()).isEqualTo(erro);
		assertThat(throwable).isNull();
	}

	@E("O servico ViaCep nao esta respondendo$")
	public void o_servico_via_cep_nao_esta_respondendo() throws Throwable {
		wireMockServer.stubFor(get(urlMatching("/ws/.*")).willReturn(aResponse().withStatus(200)
				.withFixedDelay(6000).withBodyFile("resultado-pesquisa-BuscaEndereco_out.xml")));
	}

	@Entao("Uma excecao deve ser lancada com a mensagem de erro:$")
	public void uma_excecao_deve_ser_lancada_com_a_mensagem_de_erro(String message) throws Throwable {
		assertThat(throwable).hasMessage(message);
	}
}