package br.edu.ifce.cedro.stockcontrol.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "unidade")
@EqualsAndHashCode(of = "id")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;
	private Integer quantidade;
	private Integer minimo;

	@ManyToOne
	@JoinColumn(name = "unidade")
	private Unidade unidade;

}
