/*
 * File: EveryConstraintList.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import java.util.List;

/**
 * The EveryConstraintList is a ConstraintList for any type of Constraint where all of the
 * constraints contained within it need to return true. See AnyConstraintList, NoneConstraintList for more details.
 */
public class EveryConstraintList extends AbstractConstraintList {
    /**
     * Initializes an empty list of constraints.
     */
    public EveryConstraintList() {
    }

    /**
     * Initializes a list of constraints from the specified list of constraints.
     *
     * @param constraints the list of constraints to initialize this list from.
     */
    public EveryConstraintList(List<Constraint> constraints) {
        super(constraints);
    }

    /**
     * Returns the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned.
     *
     * @return the number of conflicts this course has with it's prerequisites that have been taken.
     * If there are no conflicts, then zero is returned.
     */
    @Override
    public int getNumberOfConflicts() {
        return getNumberOfConflictsAll();
    }

    /**
     * Returns the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned.
     *
     * @return the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned.
     */
    @Override
    public List<Constraint> getConflicts() {
        return getConflictsAll();
    }

    /**
     * Returns true if this constraint list has at least one conflict.
     *
     * @return true if this constraint list has at least one conflict.
     */
    @Override
    public boolean hasConflict() {
        return hasConflictAll();
    }
}
