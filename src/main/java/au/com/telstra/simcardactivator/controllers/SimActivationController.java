package au.com.telstra.simcardactivator.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import au.com.telstra.simcardactivator.dto.SimActivationRequest;
import au.com.telstra.simcardactivator.dto.SimActivationResponse;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.client.WebClient;



@RestController
@RequestMapping("/api/sim")
public class SimActivationController {

    @Autowired
    private WebClient.Builder webClientBuilder;


    @PostMapping("/activate-sim")
    public void activateSim(@RequestBody SimActivationRequest request) {
        String actuatorUrl = "http://localhost:8444/actuate";

        // Send POST request using WebClient to actuator
        Mono<SimActivationResponse> response = webClientBuilder.build()
                .post()
                .uri(actuatorUrl)
                .bodyValue(new SimActivationRequest(request.getIccid())) 
                .retrieve()
                .bodyToMono(SimActivationResponse.class);
        
        System.out.println("Post Request sent");

        // shoot a request and wait for the result
        response.subscribe(responseBody -> {
            if (responseBody.isSuccess()) {
                System.out.println("SIM card activation successful.");
            } else {
                System.out.println("SIM card activation failed.");
            }
        }, error -> {
            System.out.println("Error while contacting the actuator: " + error.getMessage());
        });
        // return response.flatMap(responseBody -> {
        //     if (responseBody.isSuccess()) {
        //         System.out.println("SIM card activation successful.");
        //         return Mono.just(ResponseEntity.ok("SIM card activation successful."));
        //     } else {
        //         System.out.println("SIM card activation failed.");
        //         return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SIM card activation failed."));
        //     }
        // }).onErrorResume(error -> {
        //     System.out.println("Error while contacting the actuator: " + error.getMessage());
        //     return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage()));
        // });
    }
}
