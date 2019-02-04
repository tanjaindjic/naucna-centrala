package master.naucnacentrala;

import javafx.scene.NodeBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.impl.util.json.JSONException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties({FileStorageProperties.class})
public class NaucnaCentralaApplication {

	public static void main(String[] args) throws IOException, JSONException {
		SpringApplication.run(NaucnaCentralaApplication.class, args);
		/*
		 * String parsedText = null; PDFParser parser; PDDocument pdDoc = null;
		 * COSDocument cosDoc = null; PDFTextStripper pdfStripper;
		 * 
		 * String filePath = "C:\\Users\\hrcak\\Desktop\\test1.pdf"; File pdf = new
		 * File(filePath); try { parser = new PDFParser(new RandomAccessFile(pdf,"r"));
		 * parser.parse(); cosDoc = parser.getDocument(); pdfStripper = new
		 * PDFTextStripper(); pdDoc = new PDDocument(cosDoc); parsedText =
		 * pdfStripper.getText(pdDoc); } catch (Exception e) { e.printStackTrace(); try
		 * { if (cosDoc != null) cosDoc.close(); if (pdDoc != null) pdDoc.close(); }
		 * catch (Exception e1) { e.printStackTrace(); } } byte[] input_file =
		 * Files.readAllBytes(Paths.get(filePath)); byte[] encodedBytes =
		 * Base64.getEncoder().encode(input_file); JSONObject obj = new JSONObject();
		 * obj.put("id", 1); JSONObject json = new JSONObject(); json.put("content",
		 * parsedText); json.put("reviewers", obj); json.put("author", "Википедија");
		 * json.put("title", "Историја латинице"); json.put("keywords",
		 * "латиница, писмо, историја"); json.put("scientific-areas", "лингвистика");
		 * json.put("description",
		 * "Латинична абецеда, такође позната и као римски алфабет, јесте најкориштенији алфабетски систем на свету."
		 * ); json.put("issuer", "Лингвист"); json.put("open-access", true); String
		 * message = json.toString(); System.out.println(message); RestClient restClient
		 * = RestClient.builder( new HttpHost("localhost", 9200, "http"), new
		 * HttpHost("localhost", 9300, "http")).build(); HttpEntity entity = new
		 * NStringEntity(message, ContentType.APPLICATION_JSON); Response indexResponse
		 * = restClient.performRequest("PUT","/naucnirad/pdf/2",Collections.<String,
		 * String>emptyMap(),entity);
		 * System.out.println(EntityUtils.toString(indexResponse.getEntity()));
		 */

		/*HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity entity = new StringEntity(message);
		HttpPost post = new HttpPost("http://localhost:9300/naucnirad/_doc/2");
		post.setEntity(entity);
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept","text/plain, application/json");
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		HttpClient client = clientBuilder.build();
		client.execute(post);*/
		/*Request request = new Request(
				"POST",
				"/naucnirad/_doc/_search/");
		RestClient restClient = RestClient.builder(
				new HttpHost("localhost", 9200, "http"),
				new HttpHost("localhost", 9300, "http")).build();
		Response response = restClient.performRequest(request);
		System.out.println(EntityUtils.toString(response.getEntity()));*/



	}

}

