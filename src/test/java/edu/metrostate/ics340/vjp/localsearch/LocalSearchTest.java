package edu.metrostate.ics340.vjp.localsearch;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LocalSearchTest {
    @Test
    public void defaultConstructedLocalSearchIsNotNull() {
        LocalSearch ls = new LocalSearch();

        assertNotNull(ls);

        ls.search();
    }
}