package br.edu.ifce.cedro.stockcontrol.model;

import javax.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Saida extends Estoque {
	private String retirante;
}
