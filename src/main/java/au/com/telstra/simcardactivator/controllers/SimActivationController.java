package au.com.telstra.simcardactivator.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import au.com.telstra.simcardactivator.dto.SimActivationRequest;
import au.com.telstra.simcardactivator.dto.SimActivationResponse;
import au.com.telstra.simcardactivator.entities.SimCard;

import org.springframework.web.reactive.function.client.WebClient;

import au.com.telstra.simcardactivator.repositories.SimCardRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/sim")
public class SimActivationController {

    Logger logger = Logger.getLogger(getClass().getName());

    private final WebClient.Builder webClientBuilder;
    private final SimCardRepository simCardRepository;

    // Constructor injection
    public SimActivationController(WebClient.Builder webClientBuilder, SimCardRepository simCardRepository) {
        this.webClientBuilder = webClientBuilder;
        this.simCardRepository = simCardRepository;
    }

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
        SimActivationResponse response = webClientBuilder.build()
                .post()
                .uri(actuatorUrl)
                .bodyValue(new SimActivationRequest(request.getIccid())) 
                .retrieve()
                .bodyToMono(SimActivationResponse.class)
                .block();
        
        logger.info("Post Request sent");


        // shoot a request and wait for the result
        if (response != null && response.isSuccess()) {
            logger.info("SIM card activation successful.");
        } else {
            logger.info("SIM card activation failed.");
        }


        SimCard newSimCardRecord = new SimCard(
            request.getIccid(),
            request.getCustomerEmail(),
            response != null && response.isSuccess()
        );
        simCardRepository.save(newSimCardRecord);
        logger.info("Saved new sim card record to database");
        
    }
}
