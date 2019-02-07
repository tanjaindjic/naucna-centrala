package master.naucnacentrala.model.dto;

import master.naucnacentrala.model.enums.Status;

import java.util.HashMap;

public class SyncDTO {
    HashMap<Long, Status> transakcije;

    public SyncDTO(HashMap<Long, Status> transakcije) {
        this.transakcije = transakcije;
    }
}
