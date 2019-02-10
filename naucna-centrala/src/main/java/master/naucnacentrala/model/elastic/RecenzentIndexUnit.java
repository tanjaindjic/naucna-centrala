
package master.naucnacentrala.model.elastic;

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import master.naucnacentrala.model.enums.NaucnaOblast;

@Document(indexName = RecenzentIndexUnit.INDEX_NAME, type = RecenzentIndexUnit.TYPE_NAME, shards = 1, replicas = 0)
public class RecenzentIndexUnit {
	public static final String INDEX_NAME = "naucnicasopis_recenzent";
	public static final String TYPE_NAME = "recenzent";

	@Id
	private Long id;

	@Field(type = FieldType.Text, store = true)
	private String ime;

	@Field(type = FieldType.Text, store = true)
	private String prezime;

	@GeoPointField
	private GeoPoint lokacija;

	@Field(type = FieldType.Text, store = true)
	private String naucneOblasti;

    public RecenzentIndexUnit(Long id, String ime, String prezime, GeoPoint lokacija, String naucneOblasti) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.lokacija = lokacija;
        this.naucneOblasti = naucneOblasti;
    }

    public RecenzentIndexUnit() {
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

    public String getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(String naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }
}

