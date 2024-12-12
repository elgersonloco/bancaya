package com.challenge.bancaya.soap;

import com.bancaya.pokemon.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PokemonEndpointTest {


    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonEndpoint pokemonEndpoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pokemonEndpoint = new PokemonEndpoint(restTemplate);

    }

    @Test
    void getAbilities() {
        GetAbilitiesRequest request = new GetAbilitiesRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName("pikachu");

        String jsonResponse = "{ \"abilities\": [ { \"ability\": { \"name\": \"static\" }, \"is_hidden\": false, \"slot\": 1 } ] }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetAbilitiesResponse response = pokemonEndpoint.getAbilities(request);


        assertNotNull(response);
        assertEquals(1, response.getResponse().getAbilities().size());
        assertEquals("static", response.getResponse().getAbilities().get(0).getName());
    }

    @Test
    void getBaseExperience() {

        GetBaseExperienceRequest request = new GetBaseExperienceRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName("pikachu");

        String jsonResponse = "{ \"base_experience\": 112 }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetBaseExperienceResponse response = pokemonEndpoint.getBaseExperience(request);


        assertNotNull(response);
        assertEquals(112, response.getResponse().getBaseExperience().intValue());
    }

    @Test
    void getHeldItems() {

        GetHeldItemsRequest request = new GetHeldItemsRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName("pikachu");

        String jsonResponse = "{ \"held_items\": [ { \"item\": { \"name\": \"oran-berry\" }, \"version_details\": [ { \"rarity\": 50 } ] } ] }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetHeldItemsResponse response = pokemonEndpoint.getHeldItems(request);


        assertNotNull(response);
        assertEquals(1, response.getResponse().getHeldItems().size());
        assertEquals("oran-berry", response.getResponse().getHeldItems().get(0).getItemName());
        assertEquals(50, response.getResponse().getHeldItems().get(0).getRarity().intValue());
    }

    @Test
    void getId() {

        GetIdRequest request = new GetIdRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName("pikachu");

        String jsonResponse = "{ \"id\": 25 }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);


        GetIdResponse response = pokemonEndpoint.getId(request);


        assertNotNull(response);
        assertEquals(25, response.getResponse().getId().intValue());
    }

    @Test
    void getLocationArea() {

        GetLocationAreaEncountersRequest request = new GetLocationAreaEncountersRequest();
        request.setRequest(new PokemonRequestType());
        request.getRequest().setPokemonName("pikachu");

        String locationAreaUrl = "{\"location_area_encounters\": \"https://pokeapi.co/api/v2/pokemon/25/encounters\"}";
        String jsonResponse2 = "[ { \"location_area\": { \"name\": \"kanto-route-2-south-towards-viridian-city\" } } ]";
        ResponseEntity<String> responseFindByName = ResponseEntity.ok(locationAreaUrl);

        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseFindByName);

        ResponseEntity<String> locationAreaResponseEntity = ResponseEntity.ok(jsonResponse2);

        when(restTemplate.exchange(eq("https://pokeapi.co/api/v2/pokemon/25/encounters"), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(locationAreaResponseEntity);

        GetLocationAreaEncountersResponse response = pokemonEndpoint.getLocationArea(request);


        assertNotNull(response);
        assertEquals(1, response.getResponse().getLocationAreaEncounters().size());
        assertEquals("kanto-route-2-south-towards-viridian-city", response.getResponse().getLocationAreaEncounters().get(0));
    }
}