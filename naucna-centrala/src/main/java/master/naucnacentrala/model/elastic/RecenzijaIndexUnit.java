/*
package master.naucnacentrala.model.elastic;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import javax.persistence.*;

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

    @Field(type = FieldType.Text, store = true)
    private String grad;

    @Field(type = FieldType.Text, store = true)
    private String drzava;

    @GeoPointField
    private GeoPoint lokacija;

    @Field(type = FieldType.Text, store = true)
    private String sadrzaj;

    @Field(type = FieldType.Text, store = true)
    private String naucneOblasti;

    public RecenzijaIndexUnit(Long id, Long idRecenzenta, String ime, String prezime, String grad, String drzava, GeoPoint lokacija, String sadrzaj, String naucneOblasti) {
        this.id = id;
        this.idRecenzenta = idRecenzenta;
        this.ime = ime;
        this.prezime = prezime;
        this.grad = grad;
        this.drzava = drzava;
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

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }
}
*/
