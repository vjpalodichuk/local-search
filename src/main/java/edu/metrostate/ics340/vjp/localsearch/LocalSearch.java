/*
 * File: LocalSearch.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import edu.metrostate.ics340.vjp.localsearch.constraints.ConstraintList;

import java.util.*;

public class LocalSearch {
    private Map<SearchVariable, SearchVariable> variables;
    private ConstraintList constraints;
    private VariableDomain domain;
    private StringBuffer log;

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

    public String getLog() {
        return log.toString();
    }

    public void clearLog() {
        log = new StringBuffer();
    }

    public Map<SearchVariable, Object> search() {
        // Clear any pre-existing results.
        clear();
        boolean done = false;
        int iterations = 0;

        logIt(getVerboseVariableHeaders());
        int domainSize = domain.getAllValues().size();

        int tabuHelped = 0;

        while (!done) {
            // Start off with a Random assignment of values.
            randomTotalAssignment();
            ++iterations;

            logIt(getVerboseVariableValues());

            int variableIterations = 0;
            List<SearchVariable> variableValues = new ArrayList<>(domainSize);
            SearchVariable lastVariable = null;

            // We keep walking as long as we are making progress. We stop making progress when the variable with the
            // most conflicts doesn't improve with each iteration. We use a small tabu list to know what values have
            // been tried and when it is time to pull the plug and do a Random Restart. The size of the tabu list is
            // the same size as the domain for the variable. That way if the variable with the most conflicts is still
            // the same as the previous one and all values have been tried we can jump to a new search space.
            // Every time the variable with the most conflicts changes, the tabu list is reset.
            while (!constraints.isSatisfied() && variableIterations < domainSize) {
                int currentScore = constraints.getNumberOfConflicts();

                SearchVariable variable = constraints.getVariableWithTheMostConflicts();
                SearchVariable value = domain.getRandomValue(variable);
                SearchVariable oldValue = (SearchVariable)variable.getValue();
                int variableDomainSize = domain.getValues(variable).size();

                // We will reset and keep walking as long as we keep improving. That is, we improve when
                // the variable with the most conflicts changes before the current variable exhausts all
                // values in the domain.
                if (!variable.equals(lastVariable)) {
                    variableValues.clear();
                    lastVariable = variable;
                    variableIterations = 0;
                    variableValues.add(oldValue);
                }

                if (variableValues.size() < variableDomainSize) {
                    // Skip any values we have already tried for this variable
                    // and try to get a new value if possible.
                    int tries = 0;
                    while (variableValues.contains(value) && tries < variableDomainSize) {
                        value = domain.getRandomValue(variable);
                        tries++;
                        tabuHelped++;
                    }

                    if (!variableValues.contains(value)) {
                        variable.setValue(value);
                        variables.put(variable, value);
                        variableValues.add(value);

                        logIt(getVerboseVariableValues());
                        ++iterations;
                    }
                }
                ++variableIterations;
            }
            done = constraints.isSatisfied();
        }

        Map<SearchVariable, Object> answer = new LinkedHashMap<>();

        for (SearchVariable variable : variables.keySet()) {
            answer.put(variable.clone(), variables.get(variable));

        }

        logIt();

        return answer;
    }

    public void clear() {
        // Initialize the variable map
        for (SearchVariable variable : variables.keySet()) {
            this.variables.put(variable, null);
        }
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

    public String getSummary() {
        StringBuilder sb = new StringBuilder();

        List<SearchVariable> values = domain.getAllValues();

        sb.append("Local Search Summary:\n\n");

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
