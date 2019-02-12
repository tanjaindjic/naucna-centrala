package master.naucnacentrala;

import org.camunda.bpm.engine.impl.util.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties({FileStorageProperties.class})
public class NaucnaCentralaApplication {

	public static void main(String[] args) throws IOException, JSONException {
		SpringApplication.run(NaucnaCentralaApplication.class, args);




	}

}

