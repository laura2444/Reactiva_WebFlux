package co.com.castano.webhook.controller;

import co.com.castano.webhook.service.WebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/webhook")

public class WebHookController {

    private final WebHookService webhookService;

    @Autowired
    public WebHookController(WebHookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/receive")
    public Mono<String> receiveMessage(@RequestBody String orcResult) {
        return webhookService.process(orcResult);
    }
}
