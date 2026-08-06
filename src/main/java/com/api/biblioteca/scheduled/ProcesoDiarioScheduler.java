package com.api.biblioteca.scheduled;

import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.api.biblioteca.models.Prestamo;
import com.api.biblioteca.services.MultaService;
import com.api.biblioteca.services.PrestamoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcesoDiarioScheduler {

    private final PrestamoService prestamoService;
    private final MultaService multaService;

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Mexico_City")
    public void proceso(){
        log.info("PROCESO DIARIO COMENZADO ACTULIZAMOS PRESTAMOS Y MULTAS.");

        List<Prestamo> prestamosVencidos = prestamoService.actualizarPrestamosEstadoVencido();
        
        if(!prestamosVencidos.isEmpty()){
            multaService.generarMultas(prestamosVencidos);
        }else{
            log.info("NO HAY PRESTAMOS VENCIDOS.");
        }

        multaService.actualizarDiasRetraso();

        log.info("PROCESO FINALIZADO CORRECTAMENTE.");
    }
}
