package master.naucnacentrala.model.elastic;

import java.util.Collection;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import master.naucnacentrala.model.enums.NaucnaOblast;
import master.naucnacentrala.model.korisnici.Koautor;
import master.naucnacentrala.model.korisnici.Korisnik;

@Document(indexName = RadIndexUnit.INDEX_NAME, type = RadIndexUnit.TYPE_NAME, shards = 1, replicas = 0)
public class RadIndexUnit {
	public static final String INDEX_NAME = "naucnacentrala";
	public static final String TYPE_NAME = "rad";
	
	@Id
	private Long id;
	
	@Field(type = FieldType.Text, store = true)
	private String naslov;
	
	@Field(type = FieldType.Text, store = true)
	private String sadrzaj;
	
	@Field(type = FieldType.Text, store = true)
	private Korisnik autor;
	
	@Field(type = FieldType.Nested, store = true)
	private Collection<Koautor> koautori;
	
	@Field(type = FieldType.Text, store = true)
	private String[] kljucniPojmovi;
	
	@Field(type = FieldType.Text, store = true)
	private String apstrakt;
	
	@Field(type = FieldType.Text, store = true)
	private NaucnaOblast naucnaOblast;
	
	@Field(type = FieldType.Boolean, store = true)
	private boolean isOpenAccess;
	
	@Field(type = FieldType.Text, store = true)
	private String casopis;
	

}
