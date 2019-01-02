package master.naucnacentrala.model.korisnici;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Koautor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String ime;

	@NotNull
	@Column(nullable = false)
	private String prezime;

	@NotNull
	@Column(nullable = false)
	private String grad;

	@NotNull
	@Column(nullable = false)
	private String drzava;

	@NotNull
	@Column(nullable = false)
	private String email;
}
