package co.com.castano.webflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrchestrationService {

    private final StepOneService stepOneService;
    private final StepTwoService stepTwoService;
    private final StepThreeService stepThreeService;
    private final WebClient webClient;


    @Autowired
    public OrchestrationService(StepOneService stepOneService, StepTwoService stepTwoService, StepThreeService stepThreeService,WebClient.Builder webClientBuilder) {
        this.stepOneService = stepOneService;
        this.stepTwoService = stepTwoService;
        this.stepThreeService = stepThreeService;
        this.webClient = webClientBuilder.baseUrl("http://localhost:9001").build(); // URL del servicio webhook

    }

    public Mono<String> startOrchestration(String requestBody) {
        Mono<String> stepOneResponse = stepOneService.stepOneResponse(requestBody);
        Mono<String> stepTwoResponse = stepTwoService.stepTwoResponse(requestBody);
        Mono<String> stepThreeResponse = stepThreeService.stepThreeResponse(requestBody);

        return Mono.zip(stepOneResponse, stepTwoResponse, stepThreeResponse)
                .map(tuple -> {
                    String step1Answer = extractAnswer(tuple.getT1());
                    String step2Answer = extractAnswer(tuple.getT2());
                    String step3Answer = extractAnswer(tuple.getT3());

                    callWebhook(String.format("Step1: %s - Step2: %s - Step3: %s", step1Answer, step2Answer, step3Answer));

                    return String.format(
                            "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, \"answer\": \"Step1: %s - Step2: %s - Step3: %s\"}]}",
                            step1Answer, step2Answer, step3Answer);
                });
    }

    //PARA ACCEDER AL WEBHOOK
    private void callWebhook(String message) {
        webClient.post()
                .uri("/api/webhook/receive")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    System.out.println("Respuesta del webhook: " + response);
                }, error -> {
                    System.err.println("Error al llamar al webhook: " + error.getMessage());
                });
    }


    // Función para extraer el valor de "answer" del body de la respuesta JSON
    private String extractAnswer(String jsonResponse) {
        return jsonResponse.replaceAll(".*\"answer\":\"([^\"]+)\".*", "$1");
    }
}

