package edu.metrostate.ics340.vjp.localsearch;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class LocalSearchTest {

//    @Test
//    public void defaultConstructedLocalSearchIsNotNullAndPerformsAValidSearch() {
//        LocalSearchProblem lsp = new LocalSearchProblem();
//        LocalSearch ls = new LocalSearch(lsp.getVariables(), lsp, lsp.getConstraints());
//
//        assertNotNull(ls);
//
//        List<Map<SearchVariable, SearchVariable>> solutions = new ArrayList<>();
//        boolean done = false;
//        int count = 0;
//        int duplicateFound = 0;
//        int maxDuplicatesFound = 2;
//        int maxAssignments = 300;
//        int giveup = 0;
//
//        System.out.print("Performing Local Search with a maximum of " + maxAssignments + " assignments " +
//                "until 2 duplicate solutions are found.");
//
//        while (!done) {
//            final Map<SearchVariable, SearchVariable> search = ls.search(maxAssignments);
//
//            if (search != null) {
//
//                if (!solutions.contains(search)) {
//                    solutions.add(search);
//                    count++;
//                } else {
//                    if (++duplicateFound > maxDuplicatesFound) {
//                        done = true;
//                    }
//                }
//            } else {
//                ++giveup;
//            }
//        }
//
//        System.out.println("\nFound " + count + " solutions!");
//        System.out.println("Gave up " + giveup + " times while finding those solutions.");
//    }

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