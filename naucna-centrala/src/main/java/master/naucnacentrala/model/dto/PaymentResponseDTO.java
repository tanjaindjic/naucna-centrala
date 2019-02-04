package master.naucnacentrala.model.dto;


import master.naucnacentrala.model.enums.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PaymentResponseDTO {
	
	@NotNull
	private Long maticnaTransakcija;
	
	@NotNull
	private Status status;
	
	@Size(max = 1500)
	private String poruka;

	public PaymentResponseDTO() {
		super();
	}

	public PaymentResponseDTO(@NotNull Long maticnaTransakcija, @NotNull Status status, @Size(max = 1500) String poruka) {
		super();
		this.maticnaTransakcija = maticnaTransakcija;
		this.status = status;
		this.poruka = poruka;
	}

	@Override
	public String toString() {
		return "PaymentResponseDTO{" +
				"maticnaTransakcija=" + maticnaTransakcija +
				", status=" + status +
				", poruka='" + poruka + '\'' +
				'}';
	}

	public Long getMaticnaTransakcija() {
		return maticnaTransakcija;
	}

	public void setMaticnaTransakcija(Long maticnaTransakcija) {
		this.maticnaTransakcija = maticnaTransakcija;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}
	
}
