package com.imss.sivimss.dispoSalas.service;

import com.imss.sivimss.dispoSalas.util.DatosRequest;
import com.imss.sivimss.dispoSalas.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.text.ParseException;

public interface VerificarSalasService {
    Response<?> buscarSalasPorVelatorio(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> registrarEntrada(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> registrarSalida(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> consultaContratante(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> consultaDetalleDia(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> consultaSalasMes(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> consultaAlertas(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> renovarEntrada(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException, ParseException;
}
