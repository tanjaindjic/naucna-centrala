package master.naucnacentrala.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Clanarina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Casopis casopis;

    @Column(nullable = false)
    private Date datumKupovine;

    public Clanarina() {
    }

    public Casopis getCasopis() {
        return casopis;
    }

    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }

    public Date getDatumKupovine() {
        return datumKupovine;
    }

    public void setDatumKupovine(Date datumKupovine) {
        this.datumKupovine = datumKupovine;
    }
}
