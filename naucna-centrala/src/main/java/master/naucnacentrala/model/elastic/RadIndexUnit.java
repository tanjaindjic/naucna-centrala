package master.naucnacentrala.model.elastic;

import java.util.ArrayList;
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
	public static final String INDEX_NAME = "naucnirad";
	public static final String TYPE_NAME = "pdf";
	
	@Id
	private Long id;

	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String naslov;

	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String sadrzaj;

	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String autor;
	
	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private  String koautori;
	
	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String kljucniPojmovi;
	
	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String apstrakt;
	
	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private NaucnaOblast naucnaOblast;
	
	@Field(type = FieldType.Boolean, store = true)
	private boolean isOpenAccess;
	
	@Field(type = FieldType.text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String casopis;

	public RadIndexUnit(String naslov, String sadrzaj, String autor,  String koautori,
			 String kljucniPojmovi, String apstrakt, NaucnaOblast naucnaOblast, boolean isOpenAccess, String casopis) {
		super();
		this.naslov = naslov;
		this.sadrzaj = sadrzaj;
		this.autor = autor;
		this.koautori = koautori;
		this.kljucniPojmovi = kljucniPojmovi;
		this.apstrakt = apstrakt;
		this.naucnaOblast = naucnaOblast;
		this.isOpenAccess = isOpenAccess;
		this.casopis = casopis;
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

	public  String getKljucniPojmovi() {
		return kljucniPojmovi;
	}

	public void setKljucniPojmovi( String kljucniPojmovi) {
		this.kljucniPojmovi = kljucniPojmovi;
	}

	public String getApstrakt() {
		return apstrakt;
	}

	public void setApstrakt(String apstrakt) {
		this.apstrakt = apstrakt;
	}

	public NaucnaOblast getNaucnaOblast() {
		return naucnaOblast;
	}

	public void setNaucnaOblast(NaucnaOblast naucnaOblast) {
		this.naucnaOblast = naucnaOblast;
	}

	public boolean isOpenAccess() {
		return isOpenAccess;
	}

	public void setOpenAccess(boolean isOpenAccess) {
		this.isOpenAccess = isOpenAccess;
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
	
	
	

}
