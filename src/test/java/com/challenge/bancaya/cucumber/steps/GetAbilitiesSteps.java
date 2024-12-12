package com.challenge.bancaya.cucumber.steps;

import com.bancaya.pokemon.GetAbilitiesRequest;
import com.bancaya.pokemon.GetAbilitiesResponse;
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


public class GetAbilitiesSteps {
    private final TestWrapper testWrapper;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonEndpoint pokemonEndpoint;

    public GetAbilitiesSteps() {
        this.testWrapper = new TestWrapper();
    }

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("Obtener habilidades de un pokemon llamado {string}")
    public void obtenerHabilidadesDeUnPokemonLlamadoString(String pokemonName) {
        System.out.println("Se necesita obtener las habilidades de un pokemon dado su nombre");

        GetAbilitiesRequest request = new GetAbilitiesRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName(pokemonName);

        testWrapper.setGetAbilitiesRequest(request);
    }

    @When("Se procesa la solitud de obtener las habilidades del pokemon")
    public void receive_pokemon_name() {
        System.out.println("Se recibe un nombre de pokemon en una petición");

        String jsonResponse = "{ \"abilities\": [ { \"ability\": { \"name\": \"static\" }, \"is_hidden\": false, \"slot\": 1 } ] }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetAbilitiesResponse response = pokemonEndpoint.getAbilities(testWrapper.getGetAbilitiesRequest());

        testWrapper.setGetAbilitiesResponse(response);

        assertNotNull(response);
        assertEquals(1, response.getResponse().getAbilities().size());
        assertEquals("static", response.getResponse().getAbilities().get(0).getName());
    }

    @Then("Se debe retornar las habilidades del pokemon")
    public void i_should_see_the_example_result() {
        System.out.println("I should see the example result");
        assertNotNull(testWrapper.getGetAbilitiesResponse());
        assertEquals(1, testWrapper.getGetAbilitiesResponse().getResponse().getAbilities().size());
        assertEquals("static", testWrapper.getGetAbilitiesResponse().getResponse().getAbilities().get(0).getName());
    }







}
