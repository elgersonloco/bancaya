package com.challenge.bancaya.cucumber.steps;

import com.bancaya.pokemon.GetHeldItemsRequest;
import com.bancaya.pokemon.GetHeldItemsResponse;
import com.bancaya.pokemon.PokemonRequestType;
import com.challenge.bancaya.cucumber.models.TestWrapper;
import com.challenge.bancaya.soap.PokemonEndpoint;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class GetHeldItemsSteps {

    private final TestWrapper testWrapper;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonEndpoint pokemonEndpoint;

    public GetHeldItemsSteps() {
        this.testWrapper = new TestWrapper();
    }

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Given("Obtener los items de un pokemon llamado {string}")
    public void obtenerLosItemsDeUnPokemonLlamado(String pokemonName) {

        GetHeldItemsRequest request = new GetHeldItemsRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName(pokemonName);

        testWrapper.setGetHeldItemsRequest(request);

    }

    @When("Se procesa la solitud de obtener los items del pokemon")
    public void seProcesaLaSolitudDeObtenerLosItemsDelPokemon() {

        String jsonResponse = "{ \"held_items\": [ { \"item\": { \"name\": \"oran-berry\" }, \"version_details\": [ { \"rarity\": 50 } ] } ] }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetHeldItemsResponse response = pokemonEndpoint.getHeldItems(testWrapper.getGetHeldItemsRequest());

        testWrapper.setGetHeldItemsResponse(response);

    }

    @Then("Se debe retornar los items del pokemon")
    public void seDebeRetornarLosItemsDelPokemon() {
        assertNotNull(testWrapper.getGetHeldItemsResponse());
        assertEquals(1, testWrapper.getGetHeldItemsResponse().getResponse().getHeldItems().size());
        assertEquals("oran-berry", testWrapper.getGetHeldItemsResponse().getResponse().getHeldItems().get(0).getItemName());
        assertEquals(50, testWrapper.getGetHeldItemsResponse().getResponse().getHeldItems().get(0).getRarity().intValue());

    }
}
