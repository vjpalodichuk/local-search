/*
 * File: VariableDomain.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import java.util.List;

/**
 * The VariableDomain class provides an interface to return a random value from the domain of values for the variable
 * or it can return all of the values in the domain for the variable.
 * @author Vincent J. Palodichuk
 */
public interface VariableDomain {

    SearchVariable getRandomValue(SearchVariable variable);

    List<SearchVariable> getValues(SearchVariable variable);

    List<SearchVariable> getAllValues();
}
