package master.naucnacentrala.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Pretplata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Casopis casopis;

    @Column(nullable = false)
    private Date vaziDo;

    public Pretplata() {
    }

    public Pretplata(Casopis casopis, Date vaziDo) {
        this.casopis = casopis;
        this.vaziDo = vaziDo;
    }

    public Casopis getCasopis() {
        return casopis;
    }

    public void setCasopis(Casopis casopis) {
        this.casopis = casopis;
    }

    public Date getVaziDo() {
        return vaziDo;
    }

    public void setVaziDo(Date vaziDo) {
        this.vaziDo = vaziDo;
    }
}
