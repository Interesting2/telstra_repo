package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.dto.SimActivationRequest;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

import au.com.telstra.simcardactivator.entities.SimCard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.assertj.core.api.Assertions.assertThat;


@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    @Autowired
    private TestRestTemplate restTemplate;

    private String iccid;

    
    @Given("I have a Sim Card with iccid is {string}")
    public void sim_card_iccid_is(String iccid) {
        this.iccid = iccid;
    }

    @When("I ask whether the Sim Card is activated")
    public void i_ask_whether_sim_card_is_activated() {
        SimActivationRequest simActivationRequest = new SimActivationRequest(this.iccid);
        restTemplate.postForEntity(
            "http://localhost:8080/api/sim/activate-sim", simActivationRequest, Void.class
        );
    }

    @Then("querying the Sim Card with id {string} should show active {string}")
    public void query_sim_card_with_id_should_show_active(String idString, String isActiveString) {
        Long simCardId = Long.parseLong(idString);
        boolean expectedIsActive = Boolean.parseBoolean(isActiveString);

        ResponseEntity<SimCard> response = restTemplate.getForEntity(
            "http://localhost:8080/api/sim/query?simCardId=" + simCardId,
            SimCard.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        SimCard simCard = response.getBody();
        assertThat(simCard).isNotNull();
        assertThat(simCard.isActive()).isEqualTo(expectedIsActive);
    }



}