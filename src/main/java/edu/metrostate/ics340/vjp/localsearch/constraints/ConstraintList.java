/*
 * File: ConstraintList.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A ConstraintList is an abstract base class for a type of Constraint where any, every, or none of the
 * constraints contained within it need to return true. See AnyConstraintList, EveryConstraintList,
 * and NoneConstraintList for more details.
 */
public abstract class ConstraintList implements Constraint {
    protected List<Constraint> constraints;

    /**
     * Initializes an empty list of constraints.
     *
     */
    public ConstraintList() {
        this(null);
    }

    /**
     * Initializes a list of constraints from the specified list of constraints.
     *
     * @param constraints the list of constraints to initialize this list from.
     */
    public ConstraintList(List<Constraint> constraints) {
        this.constraints = new ArrayList<>();

        if (constraints != null) {
            this.constraints.addAll(constraints);
        }
    }

    /**
     * Removes all of the constraints that have been added to this list.
     */
    public void clear() {
        constraints.clear();
    }

    /**
     * Returns the number of constraints that have been added to this list.
     *
     * @return the number of constraints that have been added to this list.
     */
    public int getNumConstraints() {
        return constraints.size();
    }


    public List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    protected List<Constraint> getRealConstraints() {
        return constraints;
    }

    public boolean add(Constraint constraint) {
        boolean answer = false;

        if (!constraints.contains(constraint)) {
            answer = constraints.add(constraint);
        }

        return answer;
    }

    /**
     * Returns the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned.
     *
     * @return the number of conflicts this course has with it's prerequisites that have been taken.
     * If there are no conflicts, then zero is returned.
     */
    abstract public int getNumberOfConflicts();

    /**
     * Returns the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned. Counts all constraint violations as a conflict.
     *
     * @return the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned. Counts all constraint violations as a conflict.
     */
    protected int getNumberOfConflictsAll() {
        int answer = 0;

        for (Constraint constraint : getRealConstraints()) {
            if (!constraint.isSatisfied()) {
                answer++;
            }
        }

        return answer;
    }

    /**
     * Returns the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned. Counts all constraint satisfactions as a conflict.
     *
     * @return the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned. Counts all constraint satisfactions as a conflict.
     */
    protected int getNumberOfConflictsNone() {
        int answer = 0;

        for (Constraint constraint : getRealConstraints()) {
            if (constraint.isSatisfied()) {
                answer++;
            }
        }

        return answer;
    }

    /**
     * Returns the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned. Counts any constraint violations as a conflict
     * if there isn't at least one satisfying constraint.
     *
     * @return the number of conflicts this course has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned. Counts any constraint violations as a conflict
     * if there isn't at least one satisfying constraint
     */
    protected int getNumberOfConflictsAny() {
        int answer = 0;

        for (Constraint constraint : getRealConstraints()) {
            if (!constraint.isSatisfied()) {
                answer++;
            } else {
                answer = 0;
                break;
            }
        }

        return answer;
    }

    /**
     * Returns the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned.
     *
     * @return the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned.
     */
    abstract public List<Constraint> getConflicts();

    /**
     * Returns the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned. Counts all constraint violations as a conflict.
     *
     * @return the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned. Counts all constraint violations as a conflict.
     */
    protected List<Constraint> getConflictsAll() {
        List<Constraint> answer = new ArrayList<>();

        for (Constraint constraint : getRealConstraints()) {
            if (!constraint.isSatisfied()) {
                answer.add(constraint);
            }
        }

        return answer;
    }

    /**
     * Returns the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned. Counts all constraint satisfactions as a conflict.
     *
     * @return the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned. Counts all constraint satisfactions as a conflict.
     */
    protected List<Constraint> getConflictsNone() {
        List<Constraint> answer = new ArrayList<>();

        for (Constraint constraint : getRealConstraints()) {
            if (constraint.isSatisfied()) {
                answer.add(constraint);
            }
        }

        return answer;
    }

    /**
     * Returns the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned. Counts any constraint violations as a conflict if there isn't at
     * least one satisfying constraint.
     *
     * @return the list of constraints that have conflicts with this course. If there are no conflicts,
     * then an empty list is returned. Counts any constraint violations as a conflict if there isn't at
     * least one satisfying constraint.
     */
    protected List<Constraint> getConflictsAny() {
        List<Constraint> answer = new ArrayList<>();

        for (Constraint constraint : getRealConstraints()) {
            if (!constraint.isSatisfied()) {
                answer.add(constraint);
            } else {
                answer.clear();
                break;
            }
        }

        return answer;
    }

    /**
     * Returns true if this constraint list has at least one conflict.
     *
     * @return true if this constraint list has at least one conflict.
     */
    abstract public boolean hasConflict();

    /**
     * Returns true if this constraint list has at least one conflict.
     * Counts all constraint violations as a conflict.
     *
     * @return true if this constraint list has at least one conflict.
     * Counts all constraint violations as a conflict.
     */
    protected boolean hasConflictAll() {
        boolean answer = false;

        for (Constraint constraint : getRealConstraints()) {
            if (!constraint.isSatisfied()) {
                answer = true;
                break;
            }
        }

        return answer;
    }

    /**
     * Returns true if this course has at least one conflict.
     * Counts all constraint satisfactions as a conflict.
     *
     * @return true if this course has at least one conflict.
     * Counts all constraint satisfactions as a conflict.
     */
    protected boolean hasConflictNone() {
        boolean answer = false;

        for (Constraint constraint : getRealConstraints()) {
            if (constraint.isSatisfied()) {
                answer = true;
                break;
            }
        }

        return answer;
    }

    /**
     * Returns true if this course has at least one conflict.
     * Counts any constraint violations as a conflict if there isn't at least one satisfying constraint.
     *
     * @return true if this course has at least one conflict.
     * Counts any constraint violations as a conflict if there isn't at least one satisfying constraint.
     */
    protected boolean hasConflictAny() {
        boolean answer = false;

        for (Constraint constraint : getRealConstraints()) {
            if (!constraint.isSatisfied()) {
                answer = true;
            } else {
                answer = false; // Stop if at least one constraint is satisfied.
                break;
            }
        }

        return answer;

    }

    /**
     * Returns true if this constraint has been satisfied; otherwise false is returned.
     *
     * @return true if this constraint has been satisfied; otherwise false is returned.
     */
    @Override
    public final boolean isSatisfied() {
        return !hasConflict();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConstraintList {");
        sb.append("type = ").append(this.getClass().getSimpleName());
        sb.append(", isSatisfied = ").append(isSatisfied());
        sb.append(", numConstraints = ").append(getNumConstraints());
        sb.append(", numberOfConflicts = ").append(getNumberOfConflicts());
        sb.append(", conflicts = ").append(getConflicts());
        sb.append(", constraints = ").append(getRealConstraints());
        sb.append('}');
        return sb.toString();
    }
}
