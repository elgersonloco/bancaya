package com.challenge.bancaya.soap;

import com.bancaya.pokemon.*;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Endpoint
    public class PokemonEndpoint {

    private final RestTemplate restTemplate;
    private static final String NAMESPACE_URI = "http://pokemon.service.com/schema";
    private static final String POKEMON_API_URL = "https://pokeapi.co/api/v2/pokemon/";

    public PokemonEndpoint(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAbilitiesRequest")
    @ResponsePayload
    public GetAbilitiesResponse getAbilities(@RequestPayload GetAbilitiesRequest request) {
        ResponseEntity<String> exchange = restTemplate.exchange(POKEMON_API_URL
                        + request.getRequest().getPokemonName().toLowerCase(),
                HttpMethod.GET,
                null,
                String.class);

        List<Map<String, Object>> abilitiesList = JsonPath.read(exchange.getBody(), "$.abilities[*]");


        GetAbilitiesResponse getAbilitiesResponse = new GetAbilitiesResponse();


        GetAbilitiesResponseType abilitiesResponseType = new GetAbilitiesResponseType();
        for (Map<String, Object> abilityMap : abilitiesList) {
            AbilityType abilityType = new AbilityType();
            Map<String, Object> abilityDetails = (Map<String, Object>) abilityMap.get("ability");
            abilityType.setName((String) abilityDetails.get("name"));
            abilityType.setIsHidden((Boolean) abilityMap.get("is_hidden"));
            abilityType.setSlot(new BigInteger(abilityMap.get("slot").toString()));

            abilitiesResponseType.getAbilities().add(abilityType);
            getAbilitiesResponse.setResponse(abilitiesResponseType);

        }

        return getAbilitiesResponse;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBaseExperienceRequest")
    @ResponsePayload
    public GetBaseExperienceResponse getBaseExperience(@RequestPayload GetBaseExperienceRequest request) {

        ResponseEntity<String> exchange = restTemplate.exchange(POKEMON_API_URL
                        + request.getRequest().getPokemonName().toLowerCase(),
                HttpMethod.GET,
                null,
                String.class);

        Integer baseExp = JsonPath.read(exchange.getBody(), "$.base_experience");
        GetBaseExperienceResponseType baseExperienceResponseType = new GetBaseExperienceResponseType();
        baseExperienceResponseType.setBaseExperience(new BigInteger(baseExp.toString()));
        GetBaseExperienceResponse baseExperienceResponse = new GetBaseExperienceResponse();
        baseExperienceResponse.setResponse(baseExperienceResponseType);

        return baseExperienceResponse;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getHeldItemsRequest")
    @ResponsePayload
    public GetHeldItemsResponse getHeldItems(@RequestPayload GetHeldItemsRequest request) {

        ResponseEntity<String> exchange = restTemplate.exchange(POKEMON_API_URL
                        + request.getRequest().getPokemonName().toLowerCase(),
                HttpMethod.GET,
                null,
                String.class);

        List<Map<String, Object>> itemList = JsonPath.read(exchange.getBody(), "$.held_items[*]");
        GetHeldItemsResponseType heldItemsResponseType = new GetHeldItemsResponseType();
        GetHeldItemsResponse heldItemsResponse = new GetHeldItemsResponse();
        for (Map<String, Object> itemMap : itemList) {

            Map<String, Object> item = (Map<String, Object>) itemMap.get("item");
            List<Map<String, Object>> versionDetails = (List<Map<String, Object>>) itemMap.get("version_details");

            for (Map<String, Object> versionDetail : versionDetails) {
                HeldItemType heldItem = new HeldItemType();
                heldItem.setItemName(item.get("name").toString());
                heldItem.setRarity(new BigInteger(versionDetail.get("rarity").toString()));
                heldItemsResponseType.getHeldItems().add(heldItem);
            }

            heldItemsResponse.setResponse(heldItemsResponseType);
        }

        return heldItemsResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getIdRequest")
    @ResponsePayload
    public GetIdResponse getId(@RequestPayload GetIdRequest request) {

        ResponseEntity<String> exchange = restTemplate.exchange(POKEMON_API_URL
                        + request.getRequest().getPokemonName().toLowerCase(),
                HttpMethod.GET,
                null,
                String.class);

        Integer baseExp = JsonPath.read(exchange.getBody(), "$.id");
        GetIdResponse idResponse = new GetIdResponse();
        GetIdResponseType idResponseType = new GetIdResponseType();
        idResponseType.setId(new BigInteger(baseExp.toString()));

        idResponse.setResponse(idResponseType);

        return idResponse;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getLocationAreaEncountersRequest")
    @ResponsePayload
    public GetLocationAreaEncountersResponse getLocationArea(@RequestPayload GetLocationAreaEncountersRequest request) {

        ResponseEntity<String> exchange = restTemplate.exchange(POKEMON_API_URL
                        + request.getRequest().getPokemonName().toLowerCase(),
                HttpMethod.GET,
                null,
                String.class);

        String locationAreaUrl = JsonPath.read(exchange.getBody(), "$.location_area_encounters");

        ResponseEntity<String> locationAreaExchange = restTemplate.exchange(locationAreaUrl, HttpMethod.GET,
                null,
                String.class);


        GetLocationAreaEncountersResponse locationAreaEncountersResponse = new GetLocationAreaEncountersResponse();
        GetLocationAreaEncountersResponseType locationAreaEncountersResponseType = new GetLocationAreaEncountersResponseType();

        List<String> locationAreas = JsonPath.read(locationAreaExchange.getBody(), "$[*].location_area.name");
        for (String locationArea : locationAreas) {
            locationAreaEncountersResponseType.getLocationAreaEncounters().add(locationArea);
        }
        locationAreaEncountersResponse.setResponse(locationAreaEncountersResponseType);
        return locationAreaEncountersResponse;
    }


}
