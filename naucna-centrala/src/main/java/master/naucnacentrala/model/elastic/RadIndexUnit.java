package master.naucnacentrala.model.elastic;

import javax.persistence.Id;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import org.springframework.data.elasticsearch.annotations.GeoPointField;


@Document(indexName = RadIndexUnit.INDEX_NAME, type = RadIndexUnit.TYPE_NAME, shards = 1, replicas = 0)
public class RadIndexUnit {
	public static final String INDEX_NAME = "naucnirad";
	public static final String TYPE_NAME = "pdf";

	@Id
	private Long id;


	@Field(type = FieldType.Boolean, store = true)
	private boolean openAccess;

	@Field(type = FieldType.Text, store = true)
	private String naslov;

	@Field(type = FieldType.Text, store = true)
	private String sadrzaj;

	@Field(type = FieldType.Text, store = true)
	private String autor;
	
	@Field(type = FieldType.Text, store = true)
	private  String koautori;
	
	@Field(type = FieldType.Text, store = true)
	private String kljucnipojmovi;
	
	@Field(type = FieldType.Text, store = true)
	private String apstrakt;
	
	@Field(type = FieldType.Text, store = true)
	private String naucnaoblast;

	@Field(type = FieldType.Text, store = true)
	private String casopis;

	@Field(type = FieldType.Long, store = true)
	private Long casopisId;

	@GeoPointField
	private GeoPoint lokacija;

	@Override
	public String toString() {
		return "RadIndexUnit{" +
				"id=" + id +
				", naslov='" + naslov + '\'' +
				", sadrzaj='" + sadrzaj + '\'' +
				", autor='" + autor + '\'' +
				", koautori='" + koautori + '\'' +
				", kljucniPojmovi='" + kljucnipojmovi + '\'' +
				", apstrakt='" + apstrakt + '\'' +
				", naucnaOblast=" + naucnaoblast +
				", openAccess=" + openAccess +
				", casopis='" + casopis + '\'' +
				'}';
	}

	public RadIndexUnit(Long id, String naslov, String sadrzaj, String autor, String koautori,
						String kljucnipojmovi, String apstrakt, String naucnaoblast, boolean openAccess, String casopis, Long casopisId, GeoPoint lokacija) {
		super();
		this.id = id;
		this.naslov = naslov;
		this.sadrzaj = sadrzaj;
		this.autor = autor;
		this.koautori = koautori;
		this.kljucnipojmovi = kljucnipojmovi;
		this.apstrakt = apstrakt;
		this.naucnaoblast = naucnaoblast;
		this.openAccess = openAccess;
		this.casopis = casopis;
		this.casopisId = casopisId;
		this.lokacija = lokacija;
	}

	public RadIndexUnit() {
		super();
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public  String getKoautori() {
		return koautori;
	}

	public void setKoautori( String koautori) {
		this.koautori = koautori;
	}

	public  String getKljucnipojmovi() {
		return kljucnipojmovi;
	}

	public void setKljucnipojmovi( String kljucnipojmovi) {
		this.kljucnipojmovi = kljucnipojmovi;
	}

	public String getApstrakt() {
		return apstrakt;
	}

	public void setApstrakt(String apstrakt) {
		this.apstrakt = apstrakt;
	}

	public String getNaucnaoblast() {
		return naucnaoblast;
	}

	public void setNaucnaoblast(String naucnaoblast) {
		this.naucnaoblast = naucnaoblast;
	}

	public boolean isOpenAccess() {
		return openAccess;
	}

	public void setOpenAccess(boolean isOpenAccess) {
		this.openAccess = isOpenAccess;
	}

	public String getCasopis() {
		return casopis;
	}

	public void setCasopis(String casopis) {
		this.casopis = casopis;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public Long getCasopisId() {
		return casopisId;
	}

	public void setCasopisId(Long casopisId) {
		this.casopisId = casopisId;
	}


	public GeoPoint getLokacija() {
		return lokacija;
	}

	public void setLokacija(GeoPoint lokacija) {
		this.lokacija = lokacija;
	}
}
