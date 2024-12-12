package com.challenge.bancaya.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("get_abilities.feature")
@SelectClasspathResource("get_base_experience.feature")
@SelectClasspathResource("get_held_items.feature")
@SelectClasspathResource("get_id.feature")
@SelectClasspathResource("get_location_area_encounters.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.challenge.bancaya.cucumber.steps")
public class CucumberTest {




}

