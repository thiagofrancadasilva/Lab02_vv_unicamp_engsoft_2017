package br.unicamp.bookstore.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class StatusEncomenda {

	@XmlElement(name = "tipo")
	private String tipo;

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "descricao")
	private String descricao;

	@XmlElement(name = "erro")
	private String erro;
	
	@XmlElement(name = "protocolo")
	private String protocolo;
	
	@XmlElement(name = "data")
	private String data;
	
	@XmlElement(name = "hora")
	private String hora;
	
	@XmlElement(name = "local")
	private String local;
	
	@XmlElement(name = "codigo")
	private String codigo;
	
	@XmlElement(name = "cidade")
	private String cidade;
	
	@XmlElement(name = "uf")
	private String uf;

	
	
	public String getTipo() {
		return tipo;
	}

	public String getStatus() {
		return status;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getErro() {
		return erro;
	}
	
	public String getProtocolo() {
		return protocolo;
	}

	public String getData() {
		return data;
	}


	public String getHora() {
		return hora;
	}


	public String getLocal() {
		return local;
	}

	public String getCodigo() {
		return codigo;
	}


	public String getCidade() {
		return cidade;
	}


	public String getUf() {
		return uf;
	}




}


