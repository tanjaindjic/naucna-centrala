package master.naucnacentrala.controller;

import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.model.dto.RecenzijaDTO;
import master.naucnacentrala.model.enums.Rezultat;
import master.naucnacentrala.model.enums.StatusRada;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.service.KorisnikService;
import master.naucnacentrala.service.RadService;
import master.naucnacentrala.service.RecenzentService;
import master.naucnacentrala.service.RecenzijaService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recenzent")
public class RecenzentController {

    @Autowired
    private RecenzijaService recenzijaService;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private RecenzentService recenzentService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RadService radService;

    @Value("${camunda.objavaRadaProcessKey}")
    private String objavaRadaProcessKey;

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping(value = "/{username}/recenzije", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Recenzija> getRecenzije(@PathVariable String username){
        Korisnik k =  korisnikService.getKorisnikByUsername(username);
        return recenzijaService.findByRecenzentAndRezultat(k);
    }

    @PostMapping(value = "/{username}/recenzije/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> setRecenzije(@PathVariable String username, @PathVariable Long id, @RequestBody RecenzijaDTO recenzijaDTO){
        System.out.println("USAO U PROCES OBJAVE RADA -  ODLUKA RECENZENTA: " + username);
        System.out.println("Primio: " + recenzijaDTO.toString());
        Rad rad = radService.getRad(recenzijaDTO.getRadId());
        Korisnik k =  korisnikService.getKorisnikByUsername(username);
        Recenzija r = recenzijaService.findById(id);
        r.setRezultat(Rezultat.valueOf(recenzijaDTO.getRezultat()));
        r.setKomentar(recenzijaDTO.getKomentar());
        recenzijaService.save(r);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey(objavaRadaProcessKey)
                .variableValueEquals("radId", String.valueOf(recenzijaDTO.getRadId()))
                .singleResult();

        List<String> komentari = (List<String>) runtimeService.getVariable(pi.getId(), "komentari");
        komentari.add(recenzijaDTO.getKomentar());
        runtimeService.setVariable(pi.getId(),"komentari", komentari);

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee(username).singleResult();
        System.out.println("ZAVRSAVA TASK: " + task.getName());
        formService.submitTaskForm(task.getId(), null);

        return new ResponseEntity<>("Recenzija uspešno poslata.",HttpStatus.OK);
    }
}
