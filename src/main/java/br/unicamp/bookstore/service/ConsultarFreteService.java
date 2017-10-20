package br.unicamp.bookstore.service;

import br.unicamp.bookstore.Configuracao;
import br.unicamp.bookstore.model.Endereco;
import br.unicamp.bookstore.model.Frete;
import br.unicamp.bookstore.model.PrecoPrazo;
import br.unicamp.bookstore.model.Produto;
import br.unicamp.bookstore.model.TipoEntregaEnum;

public class ConsultarFreteService {

  private Configuracao configuracao;

  public PrecoPrazo buscar(Produto produto, TipoEntregaEnum entrega, String cep) throws Exception {
    String url = String.format("%s/CalcPrecoPrazo?%s", configuracao.getConsultaPrecoPrazoUrl(), 
    		"sCepDestino=" + cep + "&nCdServico=" + entrega.getCodigo() + produto.toQueryString());
    return new RemoteService().getAndParseXml(url, PrecoPrazo.class);
  }

}
