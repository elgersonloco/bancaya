package com.challenge.bancaya.cucumber.context;

import com.challenge.bancaya.cucumber.models.TestWrapper;

public class TestContext {
    private static TestWrapper testWrapper;

    public static TestWrapper getTestWrapper() {
        if (testWrapper == null) {
            testWrapper = new TestWrapper();
        }
        return testWrapper;
    }

    public static void reset() {
        testWrapper = null;
    }
}
