package master.naucnacentrala.service.serviceImpl;

import master.naucnacentrala.model.Casopis;
import master.naucnacentrala.model.Kupovina;
import master.naucnacentrala.model.Pretplata;
import master.naucnacentrala.model.Rad;
import master.naucnacentrala.model.dto.EntitetPlacanjaDTO;
import master.naucnacentrala.model.dto.KupovinaDTO;
import master.naucnacentrala.model.dto.PaymentRequestDTO;
import master.naucnacentrala.model.dto.PaymentResponseDTO;
import master.naucnacentrala.model.enums.Status;
import master.naucnacentrala.model.korisnici.Korisnik;
import master.naucnacentrala.repository.KupovinaRepository;
import master.naucnacentrala.service.CasopisService;
import master.naucnacentrala.service.HelpService;
import master.naucnacentrala.service.KorisnikService;
import master.naucnacentrala.service.RadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.util.Calendar;
import java.util.Date;

@Service
public class HelpServiceImpl implements HelpService {

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private CasopisService casopisService;

    @Autowired
    private KupovinaRepository kupovinaRepository;

    @Autowired
    private RadService radService;

    @Value("${naucnaCentrala.url}")
    private String ncUrl;

    @Value("${koncentrator.url}")
    private String kpUrl;


    @Override
    public ResponseEntity saljiNaKP(KupovinaDTO kupovinaDTO) {
        System.out.println(kupovinaDTO.toString());
        Korisnik k = korisnikService.getKorisnikByUsername(kupovinaDTO.getUsername());
        if(k==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Casopis c;
        Rad r = null;
        Boolean isRad = false;

        if(kupovinaDTO.getCasopisId()!=null)
            c = casopisService.getCasopis(kupovinaDTO.getCasopisId());
        else return new ResponseEntity(HttpStatus.BAD_REQUEST);

        if(c==null)
             return new ResponseEntity(HttpStatus.BAD_REQUEST);

        if(kupovinaDTO.getRadId()!=null) {
            r = radService.getRad(kupovinaDTO.getRadId());
            isRad = true;
        }
        if(isRad && r==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        Kupovina kupovina = new Kupovina(k, c, r, Status.C, kupovinaDTO.getPretplata(), kupovinaDTO.getCena());
        kupovina = kupovinaRepository.save(kupovina);
        PaymentRequestDTO paymentRequestDTO = getEntitetPlacanja(kupovinaDTO, c, r, kupovina);
        RestTemplate rt = new RestTemplate();
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session)->true);
        try {
            ResponseEntity<PaymentResponseDTO> res = rt.postForEntity(kpUrl, paymentRequestDTO, PaymentResponseDTO.class);
            if(res.getStatusCode().is2xxSuccessful()){
                return updateKupovina(res, kupovina, k);
            }else return new ResponseEntity(HttpStatus.BAD_REQUEST);

        }catch(Exception e){
            System.out.println("Greska kod slanja zahteva na KP");
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    private ResponseEntity updateKupovina(ResponseEntity<PaymentResponseDTO> res, Kupovina kupovina, Korisnik k) {
        PaymentResponseDTO response = res.getBody();

        if(response.getStatus().equals(Status.U)) {
            kupovina.setStatus(Status.U);
            kupovinaRepository.save(kupovina);

            if (kupovina.getPretplata()) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 1);
                Date sada = cal.getTime();
                Pretplata pretplata = new Pretplata(kupovina.getCasopis(), sada);
                k.getPretplaceniCasopisi().add(pretplata);
                korisnikService.addKorisnik(k);

            } else {
                if (kupovina.getRad()!=null)
                    k.getPlaceniRadovi().add(kupovina.getRad());
                else k.getPlaceniCasopisi().add(kupovina.getCasopis());
                korisnikService.addKorisnik(k);
            }

            return res;

        }else if(response.getStatus().equals(Status.N)) {

            kupovina.setStatus(Status.N);
            kupovinaRepository.save(kupovina);
            return res;

        } else return res;
    }

    private PaymentRequestDTO getEntitetPlacanja(KupovinaDTO kupovinaDTO, Casopis c, Rad r, Kupovina kupovina) {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();

        EntitetPlacanjaDTO nc = new EntitetPlacanjaDTO();
        nc.setIdentifikacioniKod("tanjatanja");
        nc.setNadredjeni(null);

        EntitetPlacanjaDTO casopis = new EntitetPlacanjaDTO();
        casopis.setIdentifikacioniKod(c.getIdentifikacioniKod());
        casopis.setNadredjeni(nc);

        Boolean kupujeCasopis = r!=null;
        EntitetPlacanjaDTO rad = null;
        if(kupujeCasopis){
            rad = new EntitetPlacanjaDTO();
            rad.setIdentifikacioniKod(r.getIdentifikacioniKod());
            rad.setNadredjeni(casopis);
            paymentRequestDTO.setEntitetPlacanja(rad);
        }else paymentRequestDTO.setEntitetPlacanja(casopis);

    paymentRequestDTO.setErrorURL(ncUrl + "#!/paymentError");
        paymentRequestDTO.setFailedURL(ncUrl + "#!/paymentFailed");
        paymentRequestDTO.setSuccessURL(ncUrl + "#!/paymentSuccess");
        paymentRequestDTO.setIznos(kupovina.getCena());
        paymentRequestDTO.setMaticnaTransakcija(kupovina.getId());
        paymentRequestDTO.setPretplata(kupovinaDTO.getPretplata());
        return paymentRequestDTO;
    }
}
