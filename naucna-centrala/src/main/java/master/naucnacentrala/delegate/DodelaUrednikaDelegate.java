package master.naucnacentrala.delegate;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DodelaUrednikaDelegate  implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("USAO U DODELU UREDNIKA NAUCNE OBLASTI NOVOG RADA DELEGATE");
        Rad r = radService.getRad(Long.parseLong(execution.getVariable("radId").toString()));
        Casopis c = r.getCasopis();
        String urednikNaucneOblasti = c.getGlavniUrednik().getUsername();
        for(Map.Entry<String, Korisnik> entry : c.getUredniciNaucnihOblasti().entrySet()){
            if(entry.getKey().equals(r.getNaucnaOblast().name())){
                urednikNaucneOblasti = entry.getValue().getUsername();
                break;
            }

        }
        execution.setVariable("urednikNaucneOblasti", urednikNaucneOblasti);
        System.out.println("DODELJEN TASK UREDNIKU: " + urednikNaucneOblasti);

    }
}
