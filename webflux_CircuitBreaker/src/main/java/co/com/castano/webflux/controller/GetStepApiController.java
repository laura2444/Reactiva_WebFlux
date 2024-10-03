package co.com.castano.webflux.controller;

import co.com.castano.webflux.service.OrchestrationService;
import co.com.castano.webflux.service.StepOneService;
import co.com.castano.webflux.service.StepThreeService;
import co.com.castano.webflux.service.StepTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/steps")
public class GetStepApiController {

    private final StepOneService stepOneService;
    private final StepTwoService stepTwoService;
    private final StepThreeService stepThreeService;
    private final OrchestrationService orchestrationService;

    @Autowired
    public GetStepApiController(StepOneService stepOneService, StepTwoService stepTwoService,
                                StepThreeService stepThreeService, OrchestrationService orchestrationService) {
        this.stepOneService = stepOneService;
        this.stepTwoService = stepTwoService;
        this.stepThreeService = stepThreeService;
        this.orchestrationService = orchestrationService;
    }



    @PostMapping("/step-one")
    public Mono<String> callStepOne(@RequestBody String requestBody) {
        return stepOneService.stepOneResponse(requestBody);
    }

    @PostMapping("/step-two")
    public Mono<String> callStepTwo(@RequestBody String requestBody) {
        return stepTwoService.stepTwoResponse(requestBody);
    }

    @PostMapping("/step-three")
    public Mono<String> callStepThree(@RequestBody String requestBody) {
        return stepThreeService.stepThreeResponse(requestBody);
    }

    @PostMapping("/orchestration")
    public Mono<String> startOrchestration(@RequestBody String requestBody) {
        return orchestrationService.startOrchestration(requestBody);
    }
}
