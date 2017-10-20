package br.unicamp.bookstore.frete;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.github.tomakehurst.wiremock.WireMockServer;

import br.unicamp.bookstore.Configuracao;
import br.unicamp.bookstore.dao.DadosDeEntregaDAO;
import br.unicamp.bookstore.model.Frete;
import br.unicamp.bookstore.model.PrecoPrazo;
import br.unicamp.bookstore.model.Produto;
import br.unicamp.bookstore.model.TipoEntregaEnum;
import br.unicamp.bookstore.service.ConsultarFreteService;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;

public class CalcularFreteSteps {

	public WireMockServer wireMockServer;
	
	private static final NumberFormat CURRENCY_PT_BR_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

	@Mock
	private Configuracao configuration;
	
	@Mock
	private DadosDeEntregaDAO dadosDeEntregaDAO;

	@InjectMocks
	private ConsultarFreteService consultarFreteService;

	private Frete frete;
	
	private PrecoPrazo precoPrazo;

	private String cep;
	
	Produto produto;
	
	TipoEntregaEnum entrega;

	private Throwable throwable;

	@Before
	public void setUp() {
		wireMockServer = new WireMockServer(9876);
		wireMockServer.start();
		MockitoAnnotations.initMocks(this);
		Mockito.when(configuration.getConsultaPrecoPrazoUrl()).thenReturn("http://localhost:9876/ws");
		Mockito.doNothing().when(dadosDeEntregaDAO).saveDadosDeEntrega(Mockito.anyDouble(), Mockito.anyInt());
		cep = null;
		produto = null;
		entrega = null;
		precoPrazo = null;
		throwable = null;
	}

	@After
	public void teardown() {
		wireMockServer.stop();
	}

	@Dado("^um CEP valido e dado do produto e tipo de entrega valido:$")
	public void eu_possuo_um_CEP_valido_e_produto_valido(Map<String, String> map) throws Throwable {
		cep = map.get("cep");
		produto = new Produto(Double.valueOf(map.get("peso")), 
				Double.valueOf(map.get("largura")),
				Double.valueOf(map.get("altura")),
				Double.valueOf(map.get("comprimento")));
		entrega = TipoEntregaEnum.valueOf(map.get("entrega"));
		wireMockServer.stubFor(get(urlMatching("/ws/.*")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "text/xml").withBodyFile("calcular-frete_"+ entrega.toString() +".xml")));
	}

	@Dado("^um CEP nao existente e dado do produto e tipo de entrega valido:$")
	public void um_CEP_nao_existente_e_produto_valido(Map<String, String> map) throws Throwable {
		cep = map.get("cep");
		produto = new Produto(Double.valueOf(map.get("peso")), 
				Double.valueOf(map.get("largura")),
				Double.valueOf(map.get("altura")),
				Double.valueOf(map.get("comprimento")));
		entrega = TipoEntregaEnum.valueOf(map.get("entrega"));
		wireMockServer.stubFor(get(urlMatching("/ws/.*")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "text/xml").withBodyFile("calcular-frete_ERR.xml")));

	}

	@Dado("^um CEP valido e dado do produto e tipo de entrega nao existente:$")
	public void eu_possuo_um_CEP_valido_e_produto_nao_existente(Map<String, String> map) throws Throwable {
		cep = map.get("cep");
		produto = new Produto(Double.valueOf(map.get("peso")), 
				Double.valueOf(map.get("largura")),
				Double.valueOf(map.get("altura")),
				Double.valueOf(map.get("comprimento")));
		entrega = TipoEntregaEnum.valueOf(map.get("entrega"));
		wireMockServer.stubFor(get(urlMatching("/ws/.*")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "text/xml").withBodyFile("calcular-frete_ERR.xml")));
	}

	@Dado("^um CEP invalido e dado do produto e tipo de entrega valido:$")
	public void um_CEP_invalido_e_produto_valido(Map<String, String> map) throws Throwable {
		cep = map.get("cep");
		produto = new Produto(Double.valueOf(map.get("peso")), 
				Double.valueOf(map.get("largura")),
				Double.valueOf(map.get("altura")),
				Double.valueOf(map.get("comprimento")));
		entrega = TipoEntregaEnum.valueOf(map.get("entrega"));
		wireMockServer.stubFor(get(urlMatching("/ws/.*")).willReturn(aResponse().withStatus(400)
				.withHeader("Content-Type", "text/xml").withBodyFile("calcular-frete_BAD.xml")));
	}

	@Dado("^um CEP valido e dado do produto e tipo de entrega invalido:$")
	public void eu_possuo_um_CEP_valido_e_produto_invalido(Map<String, String> map) throws Throwable {
		cep = map.get("cep");
		produto = new Produto(Double.valueOf(map.get("peso")), 
				Double.valueOf(map.get("largura")),
				Double.valueOf(map.get("altura")),
				Double.valueOf(map.get("comprimento")));
		entrega = TipoEntregaEnum.valueOf(map.get("entrega"));
		wireMockServer.stubFor(get(urlMatching("/ws/.*")).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "text/xml").withBodyFile("calcular-frete_out.xml")));
	}

	@Quando("^eu informo o CEP no calculo de frete$")
	public void eu_informo_o_CEP_no_calculo_de_frete() throws Throwable {
		throwable = catchThrowable(() -> this.precoPrazo = consultarFreteService.buscar(produto, entrega, cep));
	}

	@Entao("^O resultado deve ser o valor do frete e tempo de entrega:$")
	public void o_resultado_deve_ser_o_endereco(List<Map<String, String>> resultado) throws Throwable {
		assertThat(CURRENCY_PT_BR_FORMAT.format(this.precoPrazo.getValorFrete())).isEqualTo(resultado.get(0).get("Valor do Frete"));
		assertThat(this.precoPrazo.getPrazoEntrega().toString()).isEqualTo(resultado.get(0).get("Tempo"));
		assertThat(throwable).isNull();
	}

	@Entao("^o retorno deve conter um valor de erro igual a \"([^\"]*)\"$")
	public void o_retorno_deve_conter_um_valor_de_erro_igual_a(String erro) throws Throwable {
		assertThat(frete.getErro()).isEqualTo(erro);
		assertThat(throwable).isNull();
	}

	@E("O servico ViaCep nao esta respondendo$")
	public void o_servico_via_cep_nao_esta_respondendo() throws Throwable {
		wireMockServer.stubFor(get(urlMatching("/ws/.*")).willReturn(aResponse().withStatus(200).withFixedDelay(6000)
				.withBodyFile("calcular-frete_out.xml")));
	}

	@Entao("Uma excecao deve ser lancada com a mensagem de erro:$")
	public void uma_excecao_deve_ser_lancada_com_a_mensagem_de_erro(String message) throws Throwable {
		assertThat(throwable).hasMessage(message);
	}
}