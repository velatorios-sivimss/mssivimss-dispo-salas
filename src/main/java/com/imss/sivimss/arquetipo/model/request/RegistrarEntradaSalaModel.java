package com.imss.sivimss.arquetipo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegistrarEntradaSalaModel {
    @JsonProperty
    private Integer idRegistro;
    @JsonProperty
    private int idSala;
    @JsonProperty
    private int idOds;
    @JsonProperty
    private int idTipoOcupacion;
    @JsonProperty
    private String fechaEntrada;
    @JsonProperty
    private String horaEntrada;
    @JsonProperty
    private String cantidadGasInicial;
    @JsonProperty
    private String descripcionMantenimiento;
    @JsonProperty
    private String fechaSalida;
    @JsonProperty
    private String horaSalida;
    @JsonProperty
    private String cantidadGasFinal;
    @JsonProperty
    private String nombreResponsable;

}
