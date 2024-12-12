package com.challenge.bancaya.cucumber.steps;

import com.bancaya.pokemon.GetLocationAreaEncountersRequest;
import com.bancaya.pokemon.GetLocationAreaEncountersResponse;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetLocationSteps {

    private final TestWrapper testWrapper;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonEndpoint pokemonEndpoint;

    public GetLocationSteps() {
        this.testWrapper = new TestWrapper();
    }

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("Obtener los lugares donde se encuentra un pokemon llamado {string}")
    public void obtenerLosLugaresDondeSeEncuentraUnPokemonLlamado(String pokemonName) {

        GetLocationAreaEncountersRequest request = new GetLocationAreaEncountersRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName(pokemonName);

        testWrapper.setGetLocationAreaEncountersRequest(request);

    }

    @When("Se procesa la solitud de obtener los lugares donde se encuentra el pokemon")
    public void seProcesaLaSolitudDeObtenerLosLugaresDondeSeEncuentraElPokemon() {

        String locationAreaUrl = "{\"location_area_encounters\": \"https://pokeapi.co/api/v2/pokemon/25/encounters\"}";
        String jsonResponse2 = "[ { \"location_area\": { \"name\": \"kanto-route-2-south-towards-viridian-city\" } } ]";
        ResponseEntity<String> responseFindByName = ResponseEntity.ok(locationAreaUrl);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseFindByName);

        ResponseEntity<String> locationAreaResponseEntity = ResponseEntity.ok(jsonResponse2);

        when(restTemplate.exchange(eq("https://pokeapi.co/api/v2/pokemon/25/encounters"),
                any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(locationAreaResponseEntity);

        GetLocationAreaEncountersResponse response = pokemonEndpoint
                .getLocationArea(testWrapper.getGetLocationAreaEncountersRequest());

        testWrapper.setGetLocationAreaEncountersResponse(response);

    }

    @Then("Se debe retornar los lugares donde se encuentra el pokemon")
    public void seDebeRetornarLosLugaresDondeSeEncuentraElPokemon() {

        assertNotNull(testWrapper.getGetLocationAreaEncountersResponse());
        assertEquals(1, testWrapper.getGetLocationAreaEncountersResponse()
                .getResponse().getLocationAreaEncounters().size());
        assertEquals("kanto-route-2-south-towards-viridian-city",
                testWrapper.getGetLocationAreaEncountersResponse().getResponse().getLocationAreaEncounters().get(0));
    }
}
