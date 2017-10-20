package br.unicamp.bookstore.service;

import br.unicamp.bookstore.Configuracao;
import br.unicamp.bookstore.dao.DadosDeEntregaDAO;
import br.unicamp.bookstore.model.PrecoPrazo;
import br.unicamp.bookstore.model.Produto;
import br.unicamp.bookstore.model.TipoEntregaEnum;

public class ConsultarFreteService {

  private Configuracao configuracao;
  private DadosDeEntregaDAO dadosDeEntregaDAO;

  public PrecoPrazo buscar(Produto produto, TipoEntregaEnum entrega, String cep) throws Exception {
    String url = String.format("%s/CalcPrecoPrazo?%s", configuracao.getConsultaPrecoPrazoUrl(), 
    		"sCepDestino=" + cep + "&nCdServico=" + (entrega == null ? null : entrega.getCodigo()) + produto.toQueryString());
    PrecoPrazo precoPrazo = new RemoteService().getAndParseXml(url, PrecoPrazo.class);
    
    if (precoPrazo.hasError()) {
    	throw new RuntimeException(precoPrazo.getMsgErro());
    }
    
	dadosDeEntregaDAO.saveDadosDeEntrega(precoPrazo.getValorFrete(), precoPrazo.getPrazoEntrega());
    
    return precoPrazo;
  }

}
