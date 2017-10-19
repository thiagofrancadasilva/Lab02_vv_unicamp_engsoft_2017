package br.unicamp.bookstore.model;

import javax.xml.bind.annotation.XmlElement;

public class Frete {

	@XmlElement(name = "cep")
	private String cep;

	@XmlElement(name = "valorFrete")
	private float valorFrete;

	@XmlElement(name = "tempoEntrega")
	private float tempoEntrega;
	
	@XmlElement(name = "erro")
	private String erro;

	public String getCep() {
		return cep;
	}

	public float getValorFrete() {
		return valorFrete;
	}
	
	public float getTempoEntrega() {
		return tempoEntrega;
	}
	
	public String getErro() {
		return erro;
	}

}
