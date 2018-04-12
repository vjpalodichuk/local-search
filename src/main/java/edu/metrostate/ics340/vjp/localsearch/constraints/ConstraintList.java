/*
 * File: ConstraintList.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ConflictList;

import java.util.Collection;
import java.util.List;

/**
 * A ConstraintList is an interface for a type of Constraint where any, every, or none of the
 * constraints contained within it need to return true. See AnyConstraintList, EveryConstraintList,
 * and NoneConstraintList for more details. A ConstraintList is used by LocalSearch to determine when it has found a
 * solution to the problem.
 */
public interface ConstraintList extends Constraint {
    /**
     * Removes all of the constraints that have been added to this list.
     */
    void clear();

    /**
     * Returns the number of constraints that have been added to this list.
     *
     * @return the number of constraints that have been added to this list.
     */
    int getNumConstraints();

    /**
     * Returns an unmodifiable list of all the constraints contained within the list.
     *
     * @return an unmodifiable list of all the constraints contained within the list.
     */
    List<Constraint> getConstraints();

    /**
     * Adds a single constraint to the list of constraints. If the constraint already exists it is not added.
     *
     * @param constraint the constraint to add to the list. Cannot be null.
     *
     * @return true if the constraint was added to the list.
     * @throws IllegalArgumentException indicates that the constraint is null.
     */
    boolean add(Constraint constraint);

    /**
     * Adds a Collection of constraints to the list of constraints. Only constraints that do not already exist in the
     * list are added.
     *
     * @param constraints the collection of constraints to add.
     * @return true if all of the constraints were added; otherwise false.
     * @throws IllegalArgumentException indicates that contraints is null or contains a null constraint.
     */
    boolean addAll(Collection<Constraint> constraints);

    /**
     * Returns ConflictList of constraints that have conflicts. If there are no conflicts,
     * then an empty list is returned.
     *
     * @return the list of constraints that have conflicts. If there are no conflicts,
     * then an empty list is returned.
     */
    ConflictList getConflicts();

    /**
     * Returns the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned.
     *
     * @return the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned.
     */
    int getNumberOfConflicts();

    /**
     * Returns true if this constraint list has at least one conflict.
     *
     * @return true if this constraint list has at least one conflict.
     */
    boolean hasConflict();

    /**
     * Returns true if this constraint has been satisfied; otherwise false is returned.
     *
     * @return true if this constraint has been satisfied; otherwise false is returned.
     */
    @Override
    boolean isSatisfied();

}
