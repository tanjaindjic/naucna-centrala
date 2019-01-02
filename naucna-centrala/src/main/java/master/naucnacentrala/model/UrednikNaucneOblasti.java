package master.naucnacentrala.model;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Urednik;

@Entity
public class UrednikNaucneOblasti {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @OneToOne
    private Urednik urednik;
    
    @NotNull
    @OneToOne
    private Casopis casopis;
    
    @NotNull
    @Column(nullable = false)
	//@ElementCollection(targetClass = NaucnaOblast.class)
	@JoinTable(name = "naucnaOblast", joinColumns = @JoinColumn(name = "id"))
	@Enumerated(EnumType.STRING)
    private NaucnaOblast naucnaOblast;
	
}
