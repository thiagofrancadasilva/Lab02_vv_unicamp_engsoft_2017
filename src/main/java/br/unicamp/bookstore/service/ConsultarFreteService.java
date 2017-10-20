package br.unicamp.bookstore.service;

import br.unicamp.bookstore.Configuracao;
import br.unicamp.bookstore.model.Endereco;
import br.unicamp.bookstore.model.Frete;
import br.unicamp.bookstore.model.Produto;
import br.unicamp.bookstore.model.TipoEntregaEnum;

public class ConsultarFreteService {

  private Configuracao configuracao;

  public Frete buscar(Produto produto, TipoEntregaEnum entrga) throws Exception {
    String url = String.format("%s/%s/xml", configuracao.getBuscarEnderecoUrl(), null);
    return new RemoteService().getAndParseXml(url, Frete.class);
  }

}
