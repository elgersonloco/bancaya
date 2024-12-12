package com.challenge.bancaya.cucumber.steps;

import com.bancaya.pokemon.GetBaseExperienceRequest;
import com.bancaya.pokemon.GetBaseExperienceResponse;
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

public class GetExpBaseSteps {

    private final TestWrapper testWrapper;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonEndpoint pokemonEndpoint;

    public GetExpBaseSteps() {
        this.testWrapper = new TestWrapper();
    }

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Given("Obtener la experiencia base de un pokemon llamado {string}")
    public void obtenerLaExperienciaBaseDeUnPokemonLlamado(String pokemonName) {

        GetBaseExperienceRequest request = new GetBaseExperienceRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName(pokemonName);

        testWrapper.setGetBaseExperienceRequest(request);

    }

    @When("Se procesa la solitud de obtener la experiencia base del pokemon")
    public void seProcesaLaSolitudDeObtenerLaExperienciaBaseDelPokemon() {

        String jsonResponse = "{ \"base_experience\": 112 }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetBaseExperienceResponse response = pokemonEndpoint.
                getBaseExperience(testWrapper.getGetBaseExperienceRequest());

        testWrapper.setGetBaseExperienceResponse(response);

    }


    @Then("Se debe retornar la experiencia base del pokemon")
    public void seDebeRetornarLaLaExperienciaBaseDelPokemon() {

        assertNotNull(testWrapper.getGetBaseExperienceResponse());
        assertEquals(112, testWrapper.getGetBaseExperienceResponse()
                .getResponse().getBaseExperience().intValue());
    }


}
