package master.naucnacentrala.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.service.CasopisService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ObavestenjeDelegate implements JavaDelegate {
	
	@Autowired
	private DelegateService delegateService;
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		//startovati proces objave rada i dodeliti uredniku task pregledanja rada
		//uneti mejl adrese urednika i autora za dalja obavestenja
		// TODO Auto-generated method stub
		System.out.println("Usao u obavestenje delegate");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("pig.inc.ns@gmail.com","tanjaindjic");
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("pig.inc.ns@gmail.com"));
			List<String> mejlovi = (ArrayList<String>) execution.getVariable("mejlovi");
			Address[] adrese = new Address[mejlovi.size()];
			for (int i = 0; i < mejlovi.size(); i++) {
				adrese[i] = new InternetAddress(mejlovi.get(i));
			}

			message.setRecipients(Message.RecipientType.TO, adrese);
			message.setSubject("Naučna centrala - obaveštenje");
			message.setText(execution.getVariable("poruka").toString());

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
