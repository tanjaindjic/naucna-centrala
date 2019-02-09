package master.naucnacentrala.model.elastic;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Document(indexName = RecenzijaIndexUnit.INDEX_NAME, type = RecenzijaIndexUnit.TYPE_NAME, shards = 1, replicas = 0)
public class RecenzijaIndexUnit {

    public static final String INDEX_NAME = "naucnicasopis_recenzija";
    public static final String TYPE_NAME = "recenzija";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(type = FieldType.Text, store = true)
    private String ime;

    @Field(type = FieldType.Text, store = true)
    private String prezime;

    @GeoPointField
    private GeoPoint lokacija;

    @Field(type = FieldType.Text, store = true)
    private String sadrzaj;

    public RecenzijaIndexUnit(Long id, String ime, String prezime, GeoPoint lokacija, String sadrzaj) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.lokacija = lokacija;
        this.sadrzaj = sadrzaj;
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
}
