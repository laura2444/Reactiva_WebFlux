package co.com.castano.webflux.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class StepOneService {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(StepOneService.class);

    public StepOneService(WebClient.Builder webClientBuilder){
        this.webClient= webClientBuilder.build();
    }

    @CircuitBreaker(name = "stepOneCircuitBreaker", fallbackMethod = "fallback")
    public Mono<String> stepOneResponse(String requestBody) {
        return webClient.post()
                .uri("http://localhost:8080/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(2))
                        .doBeforeRetry(signal -> logger.info("Reintentando paso 1, intento número {}", signal.totalRetries() + 1)));
    }

    public Mono<String> fallback(String requestBody, Throwable t){
        logger.warn("Fallback para Step 1 ejecutado debido a: {}", t.toString());
        return Mono.just("error paso 1");
    }


}
