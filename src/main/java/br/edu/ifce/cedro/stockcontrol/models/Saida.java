package br.edu.ifce.cedro.stockcontrol.models;

import javax.persistence.Entity;

@Entity
public class Saida extends Estoque {
	private String retirante;

	public String getRetirante() {
		return retirante;
	}

	public void setRetirante(String retirante) {
		this.retirante = retirante;
	}
}
