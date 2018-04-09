/*
 * File: Constraint.java
 */
package edu.metrostate.ics340.vjp.localsearch;

/**
 * This Constraint interface represents a functional constraint. That is, a constraint can be anything provided it is
 * capable of producing a boolean result that indicates if the constraint has been satisfied. The Constraint interface
 * is used to simplify the logic involved with searching for solutions to problems.
 */
public interface Constraint {
    /**
     * Returns true if this constraint has been satisfied; otherwise false is returned.
     *
     * @return true if this constraint has been satisfied; otherwise false is returned.
     */
    boolean isSatisfied();
}
