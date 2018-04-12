/*
 * File: NoneConstraintList.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ConflictList;

/**
 * The NoneConstraintList is a ConstraintList for any type of Constraint where none of the
 * constraints contained within it can return true. The NoneConstraintList is currently not used by this problem.
 * See AnyConstraintList, EveryConstraintList for more details.
 */
public class NoneConstraintList extends AbstractConstraintList {
    /**
     * Returns the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned.
     *
     * @return the number of conflicts this course has with it's prerequisites that have been taken.
     * If there are no conflicts, then zero is returned.
     */
    @Override
    public int getNumberOfConflicts() {
        return getNumberOfConflictsNone();
    }

    /**
     * Returns the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned.
     *
     * @return the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned.
     */
    @Override
    public ConflictList getConflicts() {
        return getConflictsNone();
    }

    /**
     * Returns true if this constraint list has at least one conflict.
     *
     * @return true if this constraint list has at least one conflict.
     */
    @Override
    public boolean hasConflict() {
        return hasConflictNone();
    }
}
