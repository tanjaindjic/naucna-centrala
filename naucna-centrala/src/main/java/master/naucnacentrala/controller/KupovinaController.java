package master.naucnacentrala.controller;

import master.naucnacentrala.model.Kupovina;
import master.naucnacentrala.model.dto.KupovinaDTO;
import master.naucnacentrala.model.enums.Status;
import master.naucnacentrala.model.enums.SyncStatus;
import master.naucnacentrala.service.HelpService;
import master.naucnacentrala.service.KupovinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kupovina")
public class KupovinaController {

    @Autowired
    private HelpService helpService;

    @Autowired
    private KupovinaService kupovinaService;

    @PostMapping
    public ResponseEntity<SyncStatus> syncStatus(@RequestBody HashMap<Long, Status> transakcije){
        System.out.println("primio: " + transakcije.toString());
        return kupovinaService.update(transakcije);

    }

    @PostMapping(value = "/kupi")
    public ResponseEntity kupi(@RequestBody KupovinaDTO kupovinaDTO){

        return helpService.saljiNaKP(kupovinaDTO);

    }

}
