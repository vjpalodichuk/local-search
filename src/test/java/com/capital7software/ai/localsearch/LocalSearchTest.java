package com.capital7software.ai.localsearch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LocalSearchTest {

    @Test
    public void defaultConstructedLocalSearchIsNotNullAndPerformsAValidSearch() {
        LocalSearchProblem lsp = new LocalSearchProblem();
        LocalSearch ls = new LocalSearch(lsp.getVariables(), lsp, lsp.getConstraints());

        assertNotNull(ls);

        List<Map<Object, SearchVariable>> solutions = new ArrayList<>();
        int count = 0;
        int maxAssignments = 300;
        int giveup = 0;

        System.out.print("Performing Local Search with a maximum of " + maxAssignments + " assignments " +
                "until at least 1 solution is found.");

        while (count <= 0) {
            final Map<Object, SearchVariable> search = ls.search(maxAssignments);

            if (search != null) {

                if (!solutions.contains(search)) {
                    solutions.add(search);
                    count++;
                }
            } else {
                ++giveup;
            }
        }

        System.out.println("\nFound " + count + " solution!");
        System.out.println("Gave up " + giveup + " times while finding that solution.");
    }

    @Disabled("Disabled as it takes a while to find 2 duplicate solutions!")
    @Test
    public void searchUntilTwoDuplicateSolutionsAreFound() {
        LocalSearchProblem lsp = new LocalSearchProblem();
        LocalSearch ls = new LocalSearch(lsp.getVariables(), lsp, lsp.getConstraints());

        assertNotNull(ls);

        List<Map<Object, SearchVariable>> solutions = new ArrayList<>();
        boolean done = false;
        int count = 0;
        int duplicateFound = 0;
        int maxDuplicatesFound = 2;
        int maxAssignments = 300;
        int giveup = 0;

        System.out.print("Performing Local Search with a maximum of " + maxAssignments + " assignments " +
                "until 2 duplicate solutions are found.");

        while (!done) {
            final Map<Object, SearchVariable> search = ls.search(maxAssignments);

            if (search != null) {

                if (!solutions.contains(search)) {
                    solutions.add(search);
                    count++;
                } else {
                    if (++duplicateFound > maxDuplicatesFound) {
                        done = true;
                    }
                }
            } else {
                ++giveup;
            }
        }

        System.out.println("\nFound " + count + " solutions!");
        System.out.println("Gave up " + giveup + " times while finding those solutions.");
    }

    @Test
    public void simpleTest() {
        LocalSearchProblem lsp = new LocalSearchProblem();
        LocalSearch ls = new LocalSearch(lsp.getVariables(), lsp, lsp.getConstraints());

        assertNotNull(ls);

        final Map<Object, SearchVariable> search = ls.search();

        assertNotNull(search);

        System.out.println(ls.getLog());
        System.out.println();
        System.out.println(ls.getSummary());

        ls.clearLog();
    }
}