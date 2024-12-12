package com.challenge.bancaya.cucumber.steps;

import com.bancaya.pokemon.GetIdRequest;
import com.bancaya.pokemon.GetIdResponse;
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

public class GetIdSteps {

    private final TestWrapper testWrapper;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonEndpoint pokemonEndpoint;

    public GetIdSteps() {
        this.testWrapper = new TestWrapper();
    }

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("Obtener el id de un pokemon llamado {string}")
    public void obtenerElIdDeUnPokemonLlamado(String pokemonName) {

        GetIdRequest request = new GetIdRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName(pokemonName);

        testWrapper.setGetIdRequest(request);
    }

    @When("Se procesa la solitud de obtener el id del pokemon")
    public void seProcesaLaSolitudDeObtenerElIdDelPokemon() {

        String jsonResponse = "{ \"id\": 25 }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetIdResponse response = pokemonEndpoint.getId(testWrapper.getGetIdRequest());

        testWrapper.setGetIdResponse(response);

    }

    @Then("Se debe retornar el id del pokemon")
    public void seDebeRetornarElIdDelPokemon() {

        assertNotNull(testWrapper.getGetIdResponse());
        assertEquals(25, testWrapper.getGetIdResponse().getResponse().getId().intValue());
    }
}
