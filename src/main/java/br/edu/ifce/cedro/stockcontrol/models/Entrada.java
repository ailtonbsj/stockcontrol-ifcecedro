package br.edu.ifce.cedro.stockcontrol.models;

import javax.persistence.Entity;

@Entity
public class Entrada extends Estoque {
	private String fornecedor;

	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
}
