package com.imss.sivimss.dispoSalas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
