package com.imss.sivimss.arquetipo.service;

import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface VerificarSalasService {
    Response<?> buscarSalasPorVelatorio(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> registrarEntrada(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> registrarSalida(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> consultaContratante(DatosRequest request, Authentication authentication) throws IOException;
}
