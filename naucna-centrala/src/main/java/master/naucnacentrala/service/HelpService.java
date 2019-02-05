package master.naucnacentrala.service;

import master.naucnacentrala.model.dto.KupovinaDTO;
import org.springframework.http.ResponseEntity;

public interface HelpService {
    ResponseEntity saljiNaKP(KupovinaDTO kupovinaDTO);
}
