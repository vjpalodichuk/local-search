/*
 * File: LocalSearch.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import edu.metrostate.ics340.vjp.localsearch.constraints.ConstraintList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * <p>
 * This particular LocalSearch class uses an Iterative Best Improvement strategy to ensure that a solution is found if
 * one exists. We start with a Random Start and if we don't have a solution, we go walking by first selecting a
 * variable and then randomly select a value from that variable's domain. If the variable value pair improves the
 * current assignment we accept the variable and value; otherwise we reject it. In both cases we start walking again.
 * We select the variable by the one that has the highest conflict score. The ConstraintList randomly decides
 * which variable to return if more than one variable are tied for the highest conflict score.
 * <p>
 * To ensure we don't keep trying the same variable value pair we keep a small tabu list
 * for the current variable of the values that were already tried. If we move to a new variable, the tabu list is
 * cleared and a new one started for the new variable. The size of the Tabu list is the size of the domain of possible
 * values for that variable.
 * <p>
 * If we get stuck on the same variable that has the most conflicts perform a Random Walk where we randomly select a
 * new variable from the list of variables with conflicts that is not our current variable. If we are unsuccessful in
 * selecting another variable we perform a Random Restart and begin again. Additionally, if we have not found a
 * solution by the time we have walked (domain size * number of variables) iterations, we perform a Random Restart.
 * <p>
 *
 * @author Vincent J. Palodichuk
 */
public class LocalSearch {
    private static final long SEED;
    private static Random RANDOM;

    static {
        SEED = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        RANDOM = new Random(SEED);
    }

    private Map<SearchVariable, SearchVariable> variables;
    private ConstraintList constraints;
    private VariableDomain domain;
    private StringBuffer log;

    /**
     * The constructor for the LocalSearch class initializes the search to be performed. The collection of search
     * variables, the domain for the variables, and the constraints of the variables are used to find a solution.
     * A solution is found when the assignment of values to the variables causes the constraint list to return true
     * when isSatisfied is activated.
     *
     * @param variables the collection of search variables to perform a local search on
     * @param domain the domain of possible values for each of the variables in the collection
     * @param constraints the list of constraints that need to be satisfied in order for the assignment
     *                    to be a solution.
     * @throws IllegalArgumentException if any of the parameters are null or invalid.
     */
    public LocalSearch(Collection<SearchVariable> variables, VariableDomain domain, ConstraintList constraints) {
        if (variables == null || variables.isEmpty()) {
            throw new IllegalArgumentException("variables cannot be null and cannot be empty.");
        }

        if (domain == null) {
            throw new IllegalArgumentException("domain cannot be null.");
        }

        if (constraints == null) {
            throw new IllegalArgumentException("constraints cannot be null.");
        }

        this.variables = new LinkedHashMap<>();

        for (SearchVariable variable : variables) {
            this.variables.put(variable, null);
        }

        this.domain = domain;
        this.constraints = constraints;

        log = new StringBuffer();
    }

    private void logIt() {
        logIt("\n");
    }

    private void logIt(String entry) {
        log.append(entry);
        log.append("\n");
    }

    /**
     * Returns the verbose log of activity since the log was last cleared.
     *
     * @return the verbose log of activity since the log was last cleared.
     */
    public String getLog() {
        return log.toString();
    }

    /**
     * Clears the verbose log.
     */
    public void clearLog() {
        log = new StringBuffer();
    }

    /**
     * Performs the local search on the variables. Please see the class description for the logic used to perform the
     * search. The return value is a complete assignment of the variables that satisfies all of the constraints.
     * If the local search problem does not contain a solution then this method will never return!
     *
     * @return the return value is a complete assignment of the variables that satisfies all of the constraints.
     * If there is no solution to the problem, this method will never return.
     */
    public Map<SearchVariable, SearchVariable> search() {
    	return search(0);
    }
    
    /**
     * Performs the local search on the variables. Please see the class description for the logic used to perform the
     * search. If maxIterations is non-zero then, regardless if a solution is found, the search will abort if
     * maxIterations is reached. If no solution is found because maxIterations has been reached, null is returned;
     * otherwise the return value is a complete assignment of the variables that satisfies all of the constraints.
     * If the local search problem does not contain a solution and maxIterations is 0, then this method will never
     * return!
     *
     * @param maxIterations if non-zero then, regardless if a solution is found, the search will abort if
     *                      maxIterations is reached. If equal to 0, the search will continue until a solution is found.
     * @return if no solution is found, null is returned; otherwise the return value is a complete
     * assignment of the variables that satisfies all of the constraints. If there is no solution to the problem, this
     * method will never return unless maxIterations is non-zero.
     */
    public Map<SearchVariable, SearchVariable> search(int maxIterations) {
        // Clear any pre-existing results.
        clear();
        boolean done = false;
        int iterations = 0;

        logIt(getVerboseVariableHeaders());
        int domainSize = domain.getAllValues().size();
        int maxWalk = domainSize * variables.size();

        List<SearchVariable> searchVariables = new ArrayList<>(variables.size());
        searchVariables.addAll(variables.keySet());

        while (!done && (iterations < maxIterations || maxIterations == 0)) {
            // Start off with a Random assignment of values.
            randomTotalAssignment();
            ++iterations;

            logIt(getVerboseVariableValues());

            int variableIterations = 1;
            List<SearchVariable> variableValues = new ArrayList<>(domainSize);
            SearchVariable lastVariable = null;
            int walkIterations = 1;

            // We keep walking as long as we are making progress. We stop making progress when the variable with the
            // most conflicts doesn't improve with each iteration. We use a small tabu list to know what values have
            // been tried and when it is time to pull the plug and do a Random Restart. The size of the tabu list is
            // the same size as the domain for the variable. That way if the variable with the most conflicts is still
            // the same as the previous one and all values have been tried we can jump to a new search space.
            // Every time the variable with the most conflicts changes, the tabu list is reset.
            while (!constraints.isSatisfied() && walkIterations < maxWalk) {
                int currentScore = constraints.getNumberOfConflicts();
                SearchVariable variable = constraints.getVariableWithTheMostConflicts();

                // Are we stuck?
                if (variableIterations >= domainSize) {
                    int tries = 0;

                    // Try to jiggle us out of here without having to resort to a restart by selecting another
                    // variable with a conflict.
                    List<SearchVariable> conflicts = new ArrayList<>();
                    conflicts.addAll(constraints.getVariablesWithConflictCounts().keySet());
                    while (variable.equals(lastVariable) && tries < 100) {
                        variable = conflicts.get(RANDOM.nextInt(conflicts.size()));
                        tries++;
                    }

                    if (variable.equals(lastVariable)) {
                        // If we are unable to get a new variable with a conflict then
                        // bail out of the while loop to get a complete new assignment
                        break;
                    }
                }

                SearchVariable value = domain.getRandomValue(variable);
                SearchVariable oldValue = (SearchVariable)variable.getValue();
                int variableDomainSize = domain.getValues(variable).size();

                // We will reset and keep walking as long as we keep improving. That is, we improve when
                // the variable with the most conflicts changes before the current variable exhausts all
                // values in the domain.
                if (!variable.equals(lastVariable)) {
                    variableValues.clear();
                    lastVariable = variable;
                    variableIterations = 1;
                    variableValues.add(oldValue);
                }

                if (variableValues.size() < variableDomainSize) {
                    // Skip any values we have already tried for this variable
                    // and try to get a new value if possible.
                    int tries = 0;
                    while (variableValues.contains(value) && tries < variableDomainSize) {
                        value = domain.getRandomValue(variable);
                        tries++;
                    }

                    // Have we tried this value for this particular neighbor node yet?
                    if (!variableValues.contains(value)) {
                        variable.setValue(value);
                        variables.put(variable, value);
                        variableValues.add(value);

                        int score = constraints.getNumberOfConflicts();

                        // Is it an improvement? An improvement is a lower score or, the same score but we are no longer
                        // the variable with the most conflicts :-D
                        if (score < currentScore) {// ||
                                //(score == currentScore && !variable.equals(constraints.getVariableWithTheMostConflicts()))) {
                            logIt(getVerboseVariableValues());
                        } else {
                            // Reject and go back to the old value and start again at the next iteration.
                            variable.setValue(oldValue);
                            variables.put(variable, oldValue);
                        }
                    }
                }
                ++iterations;
                ++variableIterations;
                ++walkIterations;
            }
            done = constraints.isSatisfied();
        }

        logIt();

        Map<SearchVariable, SearchVariable> answer = null;

        if (done) {
            answer = copyCurrentAssignment();
        }

        return answer;
    }

    /**
     * Resets the variables to their initial unassigned state.
     */
    public void clear() {
        // Initialize the variable map
        for (SearchVariable variable : variables.keySet()) {
            this.variables.put(variable, null);
            variable.clearValue();
        }
    }

    private Map<SearchVariable, SearchVariable> copyCurrentAssignment() {
        Map<SearchVariable, SearchVariable> answer = new LinkedHashMap<>();

        for (SearchVariable variable : variables.keySet()) {
            answer.put(variable.clone(), variables.get(variable).clone());

        }

        return answer;
    }

    private String getVerboseVariableHeaders() {
        StringBuilder sb = new StringBuilder();
        StringBuilder dashed = new StringBuilder();
        for (SearchVariable variable : variables.keySet()) {
            sb.append(" ");
            sb.append(variable.getName());
            dashed.append("----");
        }
        sb.append(" ");
        sb.append("Score");
        sb.append("");
        dashed.append("-------");
        sb.append("\n");
        sb.append(dashed);
        return sb.toString();
    }

    private String getVerboseVariableValues() {
        StringBuilder sb = new StringBuilder();

        for (SearchVariable variable : variables.values()) {
            sb.append("  ");
            sb.append(variable.getValueAsString());
            sb.append(" ");
        }

        sb.append("  ");
        sb.append(constraints.getNumberOfConflicts());
        sb.append(" ");

        return sb.toString();
    }

    /**
     * Returns a summary of the solution from the current assignment. If the current assignment is not a solution,
     * then the summary output states there is no solution and asks that you perform a search.
     *
     * @return a summary of the solution from the current assignment. If the current assignment is not a solution,
     * then the summary output states there is no solution and asks that you perform a search.
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();

        List<SearchVariable> values = domain.getAllValues();

        sb.append("Local Search Summary:\n\n");

        if (constraints.isSatisfied()) {
            for (SearchVariable valueVariable : values) {
                sb.append(valueVariable.getName());
                sb.append(":");
                for (SearchVariable variable : variables.keySet()) {
                    if (Objects.equals(variable.getValue(), valueVariable)) {
                        sb.append(" ");
                        sb.append(variable.getName());
                    }
                }
                sb.append("\n\n");
            }
        } else {
            sb.append("No Solution. Please try to perform a search.\n\n");
        }

        return sb.toString();
    }

    private void randomTotalAssignment() {
        for (SearchVariable key : variables.keySet()) {
            SearchVariable value = domain.getRandomValue(key);
            key.setValue(value);
            variables.put(key, value);
        }
    }

}
