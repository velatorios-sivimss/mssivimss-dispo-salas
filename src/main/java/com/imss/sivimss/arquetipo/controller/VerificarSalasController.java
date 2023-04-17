package com.imss.sivimss.arquetipo.controller;

import com.imss.sivimss.arquetipo.service.VerificarSalasService;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.ProviderServiceRestTemplate;
import com.imss.sivimss.arquetipo.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/")
public class VerificarSalasController {
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;
    @Autowired
    VerificarSalasService salas;

    @PostMapping("buscarSalas")
    public Response<?> busquedaSalas(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        return salas.buscarSalasPorVelatorio(request,authentication);
    }
    @PostMapping("registrarEntrada")
    public Response<?> registrarEntrada(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        return salas.registrarEntrada(request,authentication);
    }
    @PostMapping("registrarSalida")
    public Response<?> registrarSalida(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        return salas.registrarSalida(request,authentication);
    }

    @PostMapping("busquedaDia")
    public Response<?> detallePorDia(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        return salas.registrarSalida(request,authentication);
    }

    @PostMapping("consutaContratante")
    public Response<?> consultaContratante(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        return salas.consultaContratante(request,authentication);
    }
    @PostMapping("consutaDetalle")
    public Response<?> consultaDetalleDia(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        return salas.consultaDetalleDia(request,authentication);
    }

    @PostMapping("consutaCalendario")
    public Response<?> busquedaSalasMes(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        return salas.consultaSalasMes(request,authentication);
    }
}
