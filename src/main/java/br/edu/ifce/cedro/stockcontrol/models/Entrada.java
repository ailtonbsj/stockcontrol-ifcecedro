package br.edu.ifce.cedro.stockcontrol.models;

import java.util.Date;
import javax.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Entrada extends Estoque {
	private String fornecedor;
	private Date validade;
}
