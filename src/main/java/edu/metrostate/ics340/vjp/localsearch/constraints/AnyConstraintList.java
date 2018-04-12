/*
 * File: AnyConstraintList.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import java.util.List;

/**
 * The AnyConstraintList is a ConstraintList for any type of Constraint where at least one of the
 * constraints contained within it needs to return true. See EveryConstraintList, NoneConstraintList for more details.
 */
public class AnyConstraintList extends AbstractConstraintList {
    /**
     * Returns the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned.
     *
     * @return the number of conflicts this course has with it's prerequisites that have been taken.
     * If there are no conflicts, then zero is returned.
     */
    @Override
    public int getNumberOfConflicts() {
        return getNumberOfConflictsAny();
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
        return getConflictsAny();
    }

    /**
     * Returns true if this constraint list has at least one conflict.
     *
     * @return true if this constraint list has at least one conflict.
     */
    @Override
    public boolean hasConflict() {
        return hasConflictAny();
    }

}
