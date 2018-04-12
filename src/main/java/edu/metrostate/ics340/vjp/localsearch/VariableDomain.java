/*
 * File: VariableDomain.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import java.util.List;

/**
 * The VariableDomain class provides an interface to return a random value from the domain of values for the variable
 * or it can return all of the values in the domain for the variable or all of the values for all of the variables
 * it is aware of. Implementing classes should be careful to structure the domain so that the size of possible values
 * is reasonable as it is used extensively by the LocalSearch classes in the framework.
 * @author Vincent J. Palodichuk
 */
public interface VariableDomain {

    /**
     * Returns a random value for the specified variable that is within the specified variable's domain of possible
     * values.
     *
     * @param variable the variable to get a random value for. Cannot be null.
     *
     * @return a random value for the specified variable that is within the specified variable's domain of possible
     * values.
     * @throws IllegalArgumentException indicates that variable is null.
     */
    SearchVariable getRandomValue(SearchVariable variable);

    /**
     * Returns a list of all possible values for the specified variable that is within the specified variable's
     * domain of possible values.
     *
     * @param variable the variable to get all of the values for. Cannot be null.
     *
     * @return a list of all possible values for the specified variable that is within the specified variable's
     * domain of possible values.
     * @throws IllegalArgumentException indicates that variable is null.
     */
    List<SearchVariable> getValues(SearchVariable variable);

    /**
     * Returns a list of all possible values for all variables that the domain knows about.
     *
     * @return a list of all possible values for all variables that the domain knows about.
     */
    List<SearchVariable> getAllValues();

    /**
     * Returns the size of the entire domain. If a domain is infinite, then it should return a reasonable value.
     *
     * @return the size of the entire domain. If a domain is infinite, then it should return a reasonable value.
     */
    int size();

    /**
     * Returns the size of the entire domain for the specified variable. If a domain is infinite, then it should
     * return a reasonable value.
     *
     * @return the size of the entire domain for the specified variable. If a domain is infinite, then it should
     * return a reasonable value.
     * @throws IllegalArgumentException indicates that variable is null.
     */
    int size(SearchVariable variable);
}
