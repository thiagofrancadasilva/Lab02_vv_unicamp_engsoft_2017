package br.unicamp.bookstore.service;

import br.unicamp.bookstore.Configuracao;
import br.unicamp.bookstore.model.Endereco;
import br.unicamp.bookstore.model.StatusEncomenda;

public class ConsultarEntregaService {

  private Configuracao configuracao;

  public StatusEncomenda buscar(String protocolo) throws Exception {
    String url = String.format("%s/%s/xml", configuracao.getStatusEntregaUrl(), protocolo);
    return new RemoteService().getAndParseXml(url, StatusEncomenda.class);
  }

}

