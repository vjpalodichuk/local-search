/*
 * File: SearchVariable.java
 */
package edu.metrostate.ics340.vjp.localsearch;

public interface SearchVariable extends Cloneable {
    void setValue(Object value);

    Object getValue();

    String getName();

    String getValueName();

    String getValueAsString();

    SearchVariable clone();
}
