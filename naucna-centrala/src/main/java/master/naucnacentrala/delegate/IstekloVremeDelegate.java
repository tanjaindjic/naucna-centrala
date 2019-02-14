package master.naucnacentrala.delegate;

import master.naucnacentrala.model.Recenzija;
import master.naucnacentrala.service.RadService;
import master.naucnacentrala.service.RecenzijaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IstekloVremeDelegate implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Autowired
    private RecenzijaService recenzijaService;
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("USAO U ISTEKLO VREME DELEGATE");
        execution.setVariable("poruka", "Vreme za ispravku rada je isteklo, rad je odbijen.");
        recenzijaService.deleteRecenzijeByRadId(Long.parseLong(execution.getVariable("radId").toString()));
        radService.deleteRad(Long.parseLong(execution.getVariable("radId").toString()));
    }
}
