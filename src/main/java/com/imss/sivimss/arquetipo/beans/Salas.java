package com.imss.sivimss.arquetipo.beans;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imss.sivimss.arquetipo.model.UsuarioDto;
import com.imss.sivimss.arquetipo.model.request.RegistrarEntradaSalaModel;
import com.imss.sivimss.arquetipo.util.AppConstantes;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@Slf4j
public class Salas {
    public DatosRequest buscarSalas(DatosRequest request) {
        JsonParser parser = new JsonParser();
        JsonObject jO =  (JsonObject) parser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String jsonVelat = String.valueOf(jO.get("idVelatorio"));
        String tipoSala = String.valueOf(jO.get("tipoSala"));
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String query = "select " +
                "SBS.ID_REGISTRO as idRegistroBitacora, " +
                "SS.ID_SALA as idSala, " +
                "SS.NOM_SALA as nombreSala, " +
                "SS.IND_DISPONIBILIDAD as indDisponibilidad, " +
                "case " +
                "SS.IND_DISPONIBILIDAD when 1 then 'Disponible' " +
                "when 2 then 'Ocupada' " +
                "when 3 then 'En mantenimiento' " +
                "end as estadoSala, " +
                "if(SBS.FEC_ENTRADA is null, " +
                "'', " +
                "SBS.FEC_ENTRADA) as fechaEntrada, " +
                "if(SBS.TIM_HORA_ENTRADA is null, " +
                "'', " +
                "SBS.TIM_HORA_ENTRADA) as horaEntrada " +
                "from " +
                "SVC_SALA SS " +
                "left join SVC_BITACORA_SALAS SBS on " +
                "SS.ID_SALA = SBS.ID_SALA " +
                "and ifnull(SBS.FEC_SALIDA,NOW()) >= NOW() " +
                "and ifnull(SBS.TIM_HORA_SALIDA,DATE_FORMAT(NOW( ), '%H:%i' )) >= DATE_FORMAT(NOW( ), '%H:%i' ) " +
                "where " +
                "SS.ID_VELATORIO = " + jsonVelat +
                " and SS.IND_TIPO_SALA = " + tipoSala;
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest registrarEntrada(RegistrarEntradaSalaModel registrarEntrada, UsuarioDto user){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper query = new QueryHelper("INSERT INTO SVC_BITACORA_SALAS");
        query.agregarParametroValues("ID_SALA", String.valueOf(registrarEntrada.getIdSala()));
        query.agregarParametroValues("ID_ORDEN_SERVICIO", String.valueOf(registrarEntrada.getIdOds()));
        query.agregarParametroValues("ID_TIPO_OCUPACION", String.valueOf(registrarEntrada.getIdTipoOcupacion()));
        query.agregarParametroValues("FEC_ENTRADA", "'" + registrarEntrada.getFechaEntrada() + "'");
        query.agregarParametroValues("TIM_HORA_ENTRADA", "'" + registrarEntrada.getHoraEntrada() + "'");
        query.agregarParametroValues("CAN_GAS_INICIAL", registrarEntrada.getCantidadGasInicial());
        query.agregarParametroValues("DESC_MANTENIMIENTO", "'" + registrarEntrada.getDescripcionMantenimiento() + "'");
        query.agregarParametroValues("NOM_RESPONSABLE", Objects.isNull(registrarEntrada.getNombreResponsable())? "" : registrarEntrada.getNombreResponsable());
        query.agregarParametroValues("CVE_ESTATUS", "1");
        query.agregarParametroValues("ID_USUARIO_ALTA", String.valueOf(user.getIdUsuario()));
        String qr = query.obtenerQueryInsertar();
        String encoded = DatatypeConverter.printBase64Binary(qr.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificarEstatusSala(int idTipoOcupacion , int idSala, String movimiento){
        if(movimiento == "Entrada"){
            DatosRequest dr = new DatosRequest();
            Map<String, Object> parametro = new HashMap<>();
            final QueryHelper q = new QueryHelper("UPDATE SVC_SALA");
            q.agregarParametroValues("IND_DISPONIBILIDAD", idTipoOcupacion > 1 ? "2" : "3");
            q.addWhere("ID_SALA = " + idSala);
            String query = q.obtenerQueryActualizar();
            String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
            parametro.put(AppConstantes.QUERY, encoded);
            dr.setDatos(parametro);
            return dr;
        }else{
            DatosRequest dr = new DatosRequest();
            Map<String, Object> parametro = new HashMap<>();
            final QueryHelper q = new QueryHelper("UPDATE SVC_SALA");
            q.agregarParametroValues("IND_DISPONIBILIDAD", "1");
            q.addWhere("ID_SALA = " + idSala);
            String query = q.obtenerQueryActualizar();
            String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
            parametro.put(AppConstantes.QUERY, encoded);
            dr.setDatos(parametro);
            return dr;
        }

    }

    public DatosRequest modificarEstatusODS(String folioODS){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO");
        q.agregarParametroValues("CVE_ESTATUS", "3");
        q.addWhere("ID_ORDEN_SERVICIO = " + folioODS);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
    public DatosRequest registrarSalida(RegistrarEntradaSalaModel registrarEntrada, UsuarioDto user){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVC_BITACORA_SALAS");
        q.agregarParametroValues("FEC_SALIDA", "'" + registrarEntrada.getFechaSalida() + "'");
        q.agregarParametroValues("TIM_HORA_SALIDA", "'" + registrarEntrada.getHoraSalida() + "'");
        q.agregarParametroValues("CAN_GAS_FINAL", registrarEntrada.getCantidadGasFinal());
        q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
        q.agregarParametroValues("ID_USUARIO_MODIFICA", String.valueOf(user.getIdUsuario()));
        q.addWhere("ID_REGISTRO = " + registrarEntrada.getIdRegistro());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest verEstatusODS(String folioODS) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String query = "SELECT SOS.CVE_ESTATUS FROM SVC_ORDEN_SERVICIO SOS WHERE SOS.ID_ORDEN_SERVICIO = " + folioODS + "" ;
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
    
    public DatosRequest obtenerDatosContratanteFinado(DatosRequest request){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        JsonParser parser = new JsonParser();
        JsonObject jO =  (JsonObject) parser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String folioODS = String.valueOf(jO.get("folioODS"));
        String query = "SELECT " +
                "SOS.ID_ORDEN_SERVICIO AS idODS, " +
                "CONCAT(SP.NOM_PERSONA, ' ' , SP.NOM_PRIMER_APELLIDO, ' ', SP.NOM_SEGUNDO_APELLIDO ) AS nombreContratante, " +
                "CONCAT(SP2.NOM_PERSONA, ' ' , SP2.NOM_PRIMER_APELLIDO, ' ', SP2.NOM_SEGUNDO_APELLIDO ) AS nombreFinado " +
                "FROM " +
                "SVC_ORDEN_SERVICIO SOS " +
                "INNER JOIN SVC_CONTRATANTE SC ON " +
                "SOS.ID_CONTRATANTE = SC.ID_CONTRATANTE " +
                "INNER JOIN SVC_PERSONA SP ON " +
                "SC.ID_PERSONA = SP.ID_PERSONA " +
                "LEFT JOIN SVC_FINADO SF ON " +
                "SOS.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO " +
                "LEFT JOIN SVC_PERSONA SP2 ON SP2.ID_PERSONA = SF.ID_PERSONA  " +
                "WHERE " +
                "SOS.CVE_FOLIO = '" + folioODS +"'";
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
    
    public DatosRequest consultarDetalle(DatosRequest request){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        JsonParser parser = new JsonParser();
        JsonObject jO =  (JsonObject) parser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String fechaConsulta = String.valueOf(jO.get("fechaConsulta"));
        String idSala = String.valueOf(jO.get("idSala"));
        String query = "SELECT " +
                "SBS.ID_REGISTRO AS idRegistro, " +
                "SBS.ID_SALA AS idSala, " +
                "SS.NOM_SALA AS nombreSala, " +
                "SBS.TIM_HORA_ENTRADA AS horaEntrada, " +
                "if(sbs.TIM_HORA_SALIDA is null, '', sbs.TIM_HORA_SALIDA) as horaSalida, " +
                "SOS.CVE_FOLIO, " +
                "( " +
                "SELECT " +
                "CONCAT(SP.NOM_PERSONA, ' ' , SP.NOM_PRIMER_APELLIDO, ' ', SP.NOM_SEGUNDO_APELLIDO ) " +
                "FROM " +
                "SVC_ORDEN_SERVICIO SOS " +
                "INNER JOIN SVC_CONTRATANTE SC ON " +
                "SOS.ID_CONTRATANTE = SC.ID_CONTRATANTE " +
                "INNER JOIN SVC_PERSONA SP ON " +
                "SC.ID_PERSONA = SP.ID_PERSONA " +
                "LEFT JOIN SVC_FINADO SF ON " +
                "SOS.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO " +
                "WHERE " +
                "SOS.CVE_FOLIO = SOS.CVE_FOLIO) AS nombreContratante, " +
                "( " +
                "SELECT " +
                "CONCAT(SP2.NOM_PERSONA, ' ' , SP2.NOM_PRIMER_APELLIDO, ' ', SP2.NOM_SEGUNDO_APELLIDO ) " +
                "FROM " +
                "SVC_ORDEN_SERVICIO SOS " +
                "INNER JOIN SVC_CONTRATANTE SC ON " +
                "SOS.ID_CONTRATANTE = SC.ID_CONTRATANTE " +
                "INNER JOIN SVC_PERSONA SP ON " +
                "SC.ID_PERSONA = SP.ID_PERSONA " +
                "LEFT JOIN SVC_FINADO SF ON " +
                "SOS.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO " +
                "LEFT JOIN SVC_PERSONA SP2 ON " +
                "SP2.ID_PERSONA = SF.ID_PERSONA " +
                "WHERE " +
                "SOS.CVE_FOLIO = SOS.CVE_FOLIO) AS nombreFinado " +
                "FROM " +
                "SVC_BITACORA_SALAS SBS " +
                "LEFT JOIN SVC_SALA SS ON " +
                "SBS.ID_SALA = SS.ID_SALA " +
                "LEFT JOIN SVC_ORDEN_SERVICIO SOS ON " +
                "SBS.ID_ORDEN_SERVICIO = SOS.ID_ORDEN_SERVICIO " +
                "WHERE " +
                "SBS.FEC_ENTRADA = " + fechaConsulta + " " +
                "AND SBS.ID_SALA = " + idSala;
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

}
