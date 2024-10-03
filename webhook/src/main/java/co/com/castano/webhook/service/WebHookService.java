package co.com.castano.webhook.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WebHookService {
    public Mono<String> process(String orcResult) {
        return Mono.fromCallable(() -> {
            String responseExc = "Step1: Open the regrigerator - Step2: Put the giraffe in - Step3: Close de door";

            if (orcResult.contains(responseExc)) {
                System.out.println("Recibido el mensaje del orquestador: " + orcResult);
                return "recibido el mensaje del orquestador";
            } else {
                System.err.println("Respuesta inesperada: " + orcResult);
                return "Respuesta no válida, anomalia en orquestador";
            }
        });
    }
}
