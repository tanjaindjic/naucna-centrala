package master.naucnacentrala.model.elastic;

import master.naucnacentrala.model.enums.NaucnaOblast;
import org.elasticsearch.common.geo.GeoPoint;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Collection;

@Document(indexName = RecenzijaIndexUnit.INDEX_NAME, type = RecenzijaIndexUnit.TYPE_NAME, shards = 1, replicas = 0)
public class RecenzijaIndexUnit {

    public static final String INDEX_NAME = "naucnicasopis_recenzija";
    public static final String TYPE_NAME = "recenzija";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(type = FieldType.Long, store = true)
    private Long idRecenzenta;

    @Field(type = FieldType.Text, store = true)
    private String ime;

    @Field(type = FieldType.Text, store = true)
    private String prezime;

    @GeoPointField
    private GeoPoint lokacija;

    @Field(type = FieldType.Text, store = true)
    private String sadrzaj;

    @Field(type = FieldType.Text, store = true)
    private String naucneOblasti;

    public RecenzijaIndexUnit(Long id, Long idRecenzenta, String ime, String prezime, GeoPoint lokacija, String sadrzaj, String naucneOblasti) {
        this.id = id;
        this.idRecenzenta = idRecenzenta;
        this.ime = ime;
        this.prezime = prezime;
        this.lokacija = lokacija;
        this.sadrzaj = sadrzaj;
        this.naucneOblasti = naucneOblasti;
    }

    public RecenzijaIndexUnit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public GeoPoint getLokacija() {
        return lokacija;
    }

    public void setLokacija(GeoPoint lokacija) {
        this.lokacija = lokacija;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public Long getIdRecenzenta() {
        return idRecenzenta;
    }

    public void setIdRecenzenta(Long idRecenzenta) {
        this.idRecenzenta = idRecenzenta;
    }

    public String getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(String naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }
}
