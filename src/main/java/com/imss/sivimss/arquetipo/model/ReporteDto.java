package com.imss.sivimss.arquetipo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDto {
	
	private String idVelatorio;
	private String indTipoSala;
	private String mes;
	private String anio;
	private String condition;
	private String rutaNombreReporte;
	private String tipoReporte;

}
