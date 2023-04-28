package com.imss.sivimss.arquetipo.controller;

import com.imss.sivimss.arquetipo.service.VerificarSalasService;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.LogUtil;
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
import java.util.logging.Level;

@Slf4j
@RestController
@RequestMapping("/")
public class VerificarSalasController {
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;
    @Autowired
    VerificarSalasService salas;
    @Autowired
    private LogUtil logUtil;

    private static final String ALTA = "alta";
    private static final String BAJA = "baja";
    private static final String MODIFICACION = "modificacion";
    private static final String CONSULTA = "consulta";

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("buscarSalas")
    public CompletableFuture<?> busquedaSalas(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.buscarSalasPorVelatorio(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("registrarEntrada")
    public CompletableFuture<?> registrarEntrada(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.registrarEntrada(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("registrarSalida")
    public CompletableFuture<?> registrarSalida(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.registrarSalida(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("busquedaDia")
    public CompletableFuture<?> detallePorDia(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.registrarSalida(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("consutaContratante")
    public CompletableFuture<?> consultaContratante(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.consultaContratante(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("consutaDetalle")
    public CompletableFuture<?> consultaDetalleDia(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.consultaDetalleDia(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("consutaCalendario")
    public CompletableFuture<?> busquedaSalasMes(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.consultaSalasMes(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("/descargar-reporte")
    public CompletableFuture<?> descargarReporte(@RequestBody DatosRequest request, Authentication authentication) throws IOException, ParseException {

        Response<?> response = salas.descargarDocumento(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("alertaSalas")
    public CompletableFuture<?> alertaSalas(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = salas.consultaAlertas(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

//    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
//    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
//    @TimeLimiter(name = "msflujo")
//    @PostMapping("alertaSalas")
//    public CompletableFuture<?> RenovarTiempoSala(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
//        Response<?> response = salas.consultaAlertas(request,authentication);
//        return CompletableFuture
//                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
//    }

    /**
     * fallbacks generico
     *
     * @return respuestas
     */
    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  CallNotPermittedException e) throws IOException {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", CONSULTA, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  RuntimeException e) throws IOException {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", CONSULTA, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  NumberFormatException e) throws IOException {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", CONSULTA, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }
}
