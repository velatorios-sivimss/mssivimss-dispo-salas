package com.imss.sivimss.arquetipo.controller;

import com.imss.sivimss.arquetipo.service.VerificarSalasService;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.ProviderServiceRestTemplate;
import com.imss.sivimss.arquetipo.util.Response;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

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
    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("/descargar-reporte")
    public CompletableFuture<?> descargarReporte(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {

        Response<?> response = salas.descargarDocumento(request,authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    /**
     * fallbacks generico
     *
     * @return respuestas
     */
    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  CallNotPermittedException e) {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  RuntimeException e) {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  NumberFormatException e) {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }
}
