package edu.metrostate.ics340.vjp.localsearch;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LocalSearchTest {
    @Test
    public void defaultConstructedLocalSearchIsNotNull() {
        LocalSearchProblem lsp = new LocalSearchProblem();
        LocalSearch ls = new LocalSearch(lsp.getVariables(), lsp, lsp.getConstraints());

        assertNotNull(ls);

        assertNotNull(ls.search());

        System.out.println(ls.getLog());

        System.out.println(ls.getSummary());
    }
}