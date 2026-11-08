package br.edu.ifce.cedro.stockcontrol.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Saida extends Estoque {
	private String retirante;
}
