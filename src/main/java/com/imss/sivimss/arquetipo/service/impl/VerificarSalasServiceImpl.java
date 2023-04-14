package com.imss.sivimss.arquetipo.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imss.sivimss.arquetipo.beans.Salas;
import com.imss.sivimss.arquetipo.exception.BadRequestException;
import com.imss.sivimss.arquetipo.model.UsuarioDto;
import com.imss.sivimss.arquetipo.model.request.RegistrarEntradaSalaModel;
import com.imss.sivimss.arquetipo.service.VerificarSalasService;
import com.imss.sivimss.arquetipo.util.AppConstantes;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.ProviderServiceRestTemplate;
import com.imss.sivimss.arquetipo.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class VerificarSalasServiceImpl implements VerificarSalasService {
    Salas salas = new Salas();
    @Value("${endpoints.dominio-consulta}")
    private String urlDominioConsulta;
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;
    Gson json = new Gson();

    @Override
    public Response<?> buscarSalasPorVelatorio(DatosRequest request, Authentication authentication) throws IOException {
        return providerRestTemplate.consumirServicio(salas.buscarSalas(request).getDatos(), urlDominioConsulta + "/generico/consulta",
                authentication);
    }

    @Override
    public Response<?> registrarEntrada(DatosRequest request, Authentication authentication) throws IOException {
        RegistrarEntradaSalaModel registroEntrada = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), RegistrarEntradaSalaModel.class);
        UsuarioDto usuarioDto = json.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
       if(validarEstatusODS(String.valueOf(registroEntrada.getIdOds()), authentication)){
           Response<?> response = providerRestTemplate.consumirServicio(salas.registrarEntrada(registroEntrada, usuarioDto).getDatos(), urlDominioConsulta + "/generico/crear", authentication);
           if (response.getCodigo() == 200) {
               providerRestTemplate.consumirServicio(salas.modificarEstatusSala(registroEntrada.getIdTipoOcupacion(), registroEntrada.getIdSala(),"Entrada").getDatos(),
                       urlDominioConsulta + "/generico/actualizar", authentication);
               providerRestTemplate.consumirServicio(salas.modificarEstatusODS(String.valueOf(registroEntrada.getIdOds())).getDatos(),
                       urlDominioConsulta + "/generico/actualizar", authentication);
               return response;
           } else {
               throw new BadRequestException(HttpStatus.BAD_REQUEST, "Error al insertar");
           }
       }
        throw new BadRequestException(HttpStatus.BAD_REQUEST, "ODS con ID " + registroEntrada.getIdOds() + "No tiene estatus generado o en transito");
    }

    @Override
    public Response<?> registrarSalida(DatosRequest request, Authentication authentication) throws IOException {
        RegistrarEntradaSalaModel registroEntrada = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), RegistrarEntradaSalaModel.class);
        UsuarioDto usuarioDto = json.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
        Response<?> response = providerRestTemplate.consumirServicio(salas.registrarSalida(registroEntrada, usuarioDto).getDatos(), urlDominioConsulta + "/generico/crear", authentication);
        if (response.getCodigo() == 200) {
            providerRestTemplate.consumirServicio(salas.modificarEstatusSala(registroEntrada.getIdTipoOcupacion(), registroEntrada.getIdSala(),"Salida").getDatos(),
                    urlDominioConsulta + "/generico/actualizar", authentication);
            return response;
        } else {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Error al insertar");
        }
    }

    @Override
    public Response<?> consultaContratante(DatosRequest request, Authentication authentication) throws IOException {
        JsonParser parser = new JsonParser();
        JsonObject jO =  (JsonObject) parser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String folioODS = String.valueOf(jO.get("folioODS"));
        if(validarEstatusODSFolio(folioODS, authentication)){
        return providerRestTemplate.consumirServicio(salas.obtenerDatosContratanteFinado(folioODS).getDatos(), urlDominioConsulta + "/generico/consulta",
                authentication);
        }
        throw new BadRequestException(HttpStatus.BAD_REQUEST, "ODS con el folio " + folioODS + " No tiene estatus generado o en transito");
    }

    @Override
    public Response<?> consultaDetalleDia(DatosRequest request, Authentication authentication) throws IOException {
        Response<?> respuesta = providerRestTemplate.consumirServicio(salas.consultarDetalle(request).getDatos(), urlDominioConsulta + "/generico/consulta",
                authentication);
        return respuesta;
    }

    public Boolean validarEstatusODS(String idODS, Authentication authentication) throws IOException {
        Response<?> respuesta = providerRestTemplate.consumirServicio(salas.verEstatusODS(idODS).getDatos(), urlDominioConsulta + "/generico/consulta",
                authentication);
        if(respuesta.getDatos().toString().contains("CVE_ESTATUS=2") || respuesta.getDatos().toString().contains("CVE_ESTATUS=3")){
            return true;
        }
        return false;
    }

    public Boolean validarEstatusODSFolio(String idODS, Authentication authentication) throws IOException {
        Response<?> respuesta = providerRestTemplate.consumirServicio(salas.verEstatusODSFolio(idODS).getDatos(), urlDominioConsulta + "/generico/consulta",
                authentication);
        if(respuesta.getDatos().toString().contains("CVE_ESTATUS=2") || respuesta.getDatos().toString().contains("CVE_ESTATUS=3")){
            return true;
        }
        return false;
    }
}
