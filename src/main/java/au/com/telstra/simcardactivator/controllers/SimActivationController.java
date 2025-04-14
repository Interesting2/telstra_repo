package au.com.telstra.simcardactivator.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import au.com.telstra.simcardactivator.dto.SimActivationRequest;
import au.com.telstra.simcardactivator.dto.SimActivationResponse;
import au.com.telstra.simcardactivator.entities.SimCard;

import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import au.com.telstra.simcardactivator.repositories.SimCardRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/sim")
public class SimActivationController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private SimCardRepository simCardRepository;

    @GetMapping("/query")
    public ResponseEntity<SimCard> getSimCardRecordById(@RequestParam Long simCardId) {
        Optional<SimCard> matchedSimCardRecord = simCardRepository.findById(simCardId);

        if (matchedSimCardRecord.isPresent()) {
            SimCard simCard = matchedSimCardRecord.get();
            return ResponseEntity.ok(simCard);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        
        
    }

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
            boolean success = responseBody.isSuccess();

            if (success) {
                System.out.println("SIM card activation successful.");
            } else {
                System.out.println("SIM card activation failed.");
            }
            SimCard newSimCardRecord = new SimCard(
                request.getIccid(),
                request.getCustomerEmail(),
                success
            );
            simCardRepository.save(newSimCardRecord); // save it to database
            System.out.println("Saved new sim card record to database");
        }, error -> {
            System.out.println("Error while contacting the actuator: " + error.getMessage());
        });
    }
}
