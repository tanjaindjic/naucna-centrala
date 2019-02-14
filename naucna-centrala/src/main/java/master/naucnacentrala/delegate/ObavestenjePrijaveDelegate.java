package master.naucnacentrala.delegate;

import master.naucnacentrala.model.Casopis;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.model.korisnici.Urednik;
import master.naucnacentrala.service.CasopisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Component
public class ObavestenjePrijaveDelegate implements JavaDelegate {

    @Autowired
    private DelegateService delegateService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;


    @Value("${camunda.objavaRadaProcessKey}")
    private String objavaRadaProcessKey;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("USAO U OBAVESTENJE PRIJAVE NOVOG RADA DELEGATE");
        //startovati proces objave rada i dodeliti uredniku task pregledanja rada
        //uneti mejl adrese urednika i autora za dalja obavestenja
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("urednik", execution.getVariable("urednik").toString());
        mapa.put("autor", execution.getVariable("autor").toString());
        mapa.put("mejlovi", execution.getVariable("mejlovi"));
        mapa.put("radId", execution.getVariable("radId").toString());
        mapa.put("email", execution.getVariable("email").toString());
        mapa.put("recenzentList", new ArrayList());
        mapa.put("odgovor", "");
        mapa.put("komentari", new ArrayList<>());

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(objavaRadaProcessKey, mapa);

        System.out.println("ZAPOCET PROCES OBJAVE RADA: " + objavaRadaProcessKey);

        // TODO Auto-generated method stub
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

            //Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
