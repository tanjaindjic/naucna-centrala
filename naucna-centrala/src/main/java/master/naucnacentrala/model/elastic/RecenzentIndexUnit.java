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
	public static final String INDEX_NAME = "naucnacentrala";
	public static final String TYPE_NAME = "recenzent";

	@Id
	private Long id;

	@Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String ime;

	@Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	private String prezime;

	@GeoPointField
	private GeoPoint lokacija;

	@Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
	@ElementCollection(targetClass = NaucnaOblast.class)
	@Enumerated(EnumType.STRING)
	private Collection<NaucnaOblast> naucneOblasti;

}
