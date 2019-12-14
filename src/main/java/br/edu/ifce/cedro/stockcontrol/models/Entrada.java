package br.edu.ifce.cedro.stockcontrol.models;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Entrada extends Estoque {
	private String fornecedor;
	private Date validade;

	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public Date getValidade() {
		return validade;
	}
	public void setValidade(Date validade) {
		this.validade = validade;
	}
}
