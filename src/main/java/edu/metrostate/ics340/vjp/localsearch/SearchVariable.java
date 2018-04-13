/*
 * File: SearchVariable.java
 */
package edu.metrostate.ics340.vjp.localsearch;

/**
 * The SearchVariable interface is one of the course interfaces that LocalSearch uses to operate on problems.
 * The SearchVariable defines a simple interface so that the LocalSearch class doesn't have to concern itself with
 * the specific types of variables and values bieng used. SearchVariables are ubiquitous in that they are both a
 * variable and can be used as a value. In fact, the LocalSearch class depends on the SearchVariable instance being
 * able to accept SearchVariables as values. The VariableDomain demonstrates this requirement.
 */
public interface SearchVariable extends Cloneable {
    /**
     * Returns a unique ID that identifies this SearchVariable.
     *
     * @return a unique ID that identifies this SearchVariable
     */
    Object getUniqueID();

    /**
     * Set the value of this variable to the specified value. Value cannot be null!
     * @param value the new value for this variable.
     */
    void setValue(Object value);

    /**
     * Returns the value of this variable, which may be null if no value has been set.
     * @return
     */
    Object getValue();

    /**
     * Removes any value that has been set on this variable.
     */
    void clearValue();

    /**
     * Gets the name of the variable. Implementing classes should provide some kind of short name as it is
     * used in the Summary and Verbose logs of the LocalSearch class.
     *
     * @return a String value that represents the name of this variable. If the variable has not name, then the empty
     * string is returned.
     */
    String getName();

    /**
     * Gets the name of the value assigned to the variable. This varies by implementing class but it is typically the
     * property of the SearchVariable that the value is applied to.
     *
     * @return the name of the value assigned to the variable. If the variable has no name, the emtpy
     * string is returned.
     */
    String getValueName();

    /**
     * Returns the value of this variable as a string.
     *
     * @return the value of this variable as a string.
     */
    String getValueAsString();

    /**
     * Clones the variable such that changes to the clone do not affect this variable.
     *
     * @return a cloned variable such that changes to the clone do not affect this variable.
     */
    SearchVariable clone();
}
