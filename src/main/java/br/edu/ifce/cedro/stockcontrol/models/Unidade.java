package br.edu.ifce.cedro.stockcontrol.models;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Unidade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String simbolo;
	private String descricao;

}