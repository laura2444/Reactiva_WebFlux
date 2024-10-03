package co.com.castano.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BatchService {

    private final WebClient webClient;

    @Autowired
    public BatchService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9000/api/steps").build();
    }

    @Scheduled(fixedRate = 120000) // 120000 ms = 2m
    public void Process() {
        String json = """
        {
            "data": [
                {
                    "header": {
                        "id": "12345",
                        "type": "StepsGiraffeRefrigerator"
                    },
                    "enigma": "steps"
                }
            ]
        }
        """;

        // Llama al servicio de orquestación
        Mono<String> response = webClient.post()
                .uri("/orchestration")
                .bodyValue(json)
                .retrieve()
                .bodyToMono(String.class); //Convierte la respuesta en un Mono<String>

        response.subscribe(result -> {
            System.out.println("Resultado de la orquestación: " + result);
        }, error -> {
            // Manejo de errores
            System.err.println("Error durante la orquestación: " + error.getMessage());
        });
    }
}


/*
*
Flujo del Servicio
- Cada 2 minutos, se ejecuta el método Process()
- Se crea el json que contiene los datos de la solicitud
-Se hace una llamada POST al endpoint /orchestration del servicio de webclient
-Cuando se recibe la respuesta se imprime el resultado o un mensaje de error si la solicitud falla

* */