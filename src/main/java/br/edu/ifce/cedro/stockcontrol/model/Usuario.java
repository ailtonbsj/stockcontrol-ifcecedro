package br.edu.ifce.cedro.stockcontrol.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "senha")
@EqualsAndHashCode(of = "id")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String login;

	private String nome;

	@Column(nullable = false)
	private String senha;

}
