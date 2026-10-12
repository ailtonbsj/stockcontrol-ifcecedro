package br.edu.ifce.cedro.stockcontrol.models;

import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "produto")
@EqualsAndHashCode(of = "id")
public class Estoque {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer quantidade;
	private Date data;
	private String observacao;

	@ManyToOne
	@JoinColumn(name = "produto")
	private Produto produto;

}
