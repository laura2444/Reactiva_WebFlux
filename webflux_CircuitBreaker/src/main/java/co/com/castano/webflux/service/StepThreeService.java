package co.com.castano.webflux.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Service
public class StepThreeService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(StepThreeService.class);

    public StepThreeService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @CircuitBreaker(name = "stepThreeCircuitBreaker", fallbackMethod = "fallbackthree")
    public Mono<String> stepThreeResponse(String requestBody) {
        return webClient.post()
                .uri("http://step-three:8082/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(2))
                        .doBeforeRetry(signal -> logger.info("Reintentando paso 3, intento número {}", signal.totalRetries() + 1)));
    }

    public Mono<String> fallbackthree(String requestBody, Throwable t){
        logger.warn("Fallback para Step 3 ejecutado debido a: {}", t.toString());
        return Mono.just("este es un error del paso 3");
    }
}
