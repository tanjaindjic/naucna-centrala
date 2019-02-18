package master.naucnacentrala.delegate;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.enums.Rezultat;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.RecenzijaRepository;
import master.naucnacentrala.service.KorisnikService;
import master.naucnacentrala.service.RadService;
import master.naucnacentrala.service.RecenzijaService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class PrekidRecenziranjaDelegate implements JavaDelegate{


    @Autowired
    private RecenzijaService recenzijaService;


    @Autowired
    private RecenzijaRepository recenzijaRepository;

    @Autowired
    private RadService radService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private  RuntimeService runtimeService;
    @Autowired
    private KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("USAO U PREKID RECENZIRANJA DELEGATE");
        List<Recenzija> recenzije = recenzijaService.findByRadId(Long.parseLong(execution.getVariable("radId").toString()));

        for(Recenzija r : recenzije){
            if(r.getRezultat().equals(Rezultat.NOVO)) {
                recenzijaRepository.delete(r);
                List<Task> task = taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).taskAssignee(r.getRecenzent().getUsername()).list();
                for(Task t : task) {
                    System.out.println("ZAVRSAVA TASK: " + t.getName());
                    formService.submitTaskForm(t.getId(), null);
                }
            }
        }

        recenzije = recenzijaService.findByRadId(Long.parseLong(execution.getVariable("radId").toString()));
        List vecDodati = new ArrayList<>();
        for(Recenzija r : recenzije){
            vecDodati.add(r.getRecenzent().getUsername());
        }

        System.out.println("Recenzija ostalo: " + recenzije.size());
        System.out.println("Ostali recenzenti: " + vecDodati.toString());
        runtimeService.setVariable(execution.getProcessInstanceId(),"recenzentList", vecDodati);

        if(recenzije.size()>=2)
            return;

        Rad r = radService.getRad(Long.parseLong(execution.getVariable("radId").toString()));
        r.setStatusRada(StatusRada.DODELA_RECENZENATA);
        radService.addRad(r);
        String email = korisnikService.getKorisnikByUsername((execution.getVariable("urednikNaucneOblasti").toString())).getEmail();
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
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Naučna centrala - obaveštenje");
            message.setText("Poštovani,!" +
                    "\n\n " + "Potrebno je dodeliti novog recenzenta.");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    //ukloniti recenziju koja je istekla i poslati mejl uredniku
}
