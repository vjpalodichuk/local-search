/*
 * File: ConstraintList.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.SearchVariable;

import java.util.*;

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

    /**
     * Returns an unmodifiable list of all the constraints contained within the list.
     *
     * @return an unmodifiable list of all the constraints contained within the list.
     */
    public List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    /**
     * Returns the actual list of all the constraints contained within the list.
     *
     * @return the actual list of all the constraints contained within the list.
     */
    protected List<Constraint> getRealConstraints() {
        return constraints;
    }

    /**
     * Adds a single constraint to the list of constraints. If the constraint already exists it is not added.
     *
     * @param constraint the constraint to add to the list. Cannot be null.
     *
     * @return true if the constraint was added to the list.
     * @throws IllegalArgumentException indicates that the constraint is null.
     */
    public boolean add(Constraint constraint) {
        if (constraint == null) {
            throw new IllegalArgumentException("constraint cannot be null");
        }

        boolean answer = false;

        if (!constraints.contains(constraint)) {
            answer = constraints.add(constraint);
        }

        return answer;
    }

    /**
     * Adds a Collection of constraints to the list of constraints. Only constraints that do not already exist in the
     * list are added.
     *
     * @param constraints the collection of constraints to add.
     * @return true if all of the constraints were added; otherwise false.
     * @throws IllegalArgumentException indicates that contraints is null or contains a null constraint.
     */
    public boolean addAll(Collection<Constraint> constraints) {
        if (constraints == null) {
            throw new IllegalArgumentException("constraints cannot be null.");
        }

        boolean answer = false;
        boolean atLeastOneFail = false;

        for (Constraint constraint : constraints) {
            answer = add(constraint);

            if (!answer) {
                atLeastOneFail = true;
            }
        }

        if (atLeastOneFail) {
            answer = false;
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

    private void processPrereqForConflicts(Map<SearchVariable, Integer> counts, Prerequisite prerequisite) {
        processCourseForConflicts(counts, prerequisite.getCourse());
        processCourseForConflicts(counts, prerequisite.getPrerequisiteCourse());
    }

    private void processCourseForConflicts(Map<SearchVariable, Integer> counts, ScheduledCourse course) {
        processCourseForConflicts(counts, course, 1);
    }

    private void processCourseForConflicts(Map<SearchVariable, Integer> counts, ScheduledCourse course, int times) {
        int count = 0;

        if (counts.containsKey(course)) {
            count = counts.get(course);
        }

        count += times;
        counts.put(course, count);
    }

    /**
     * Returns a map with the variables as the keys and their violation counts as the value.
     * Prerequisite violations are worth one, Course List violations are worth 2, and
     * Semester Restriction violations are worth 3.
     * @return
     */
    public Map<SearchVariable, Integer> getVariablesWithConflictCounts() {
        Map<SearchVariable, Integer> counts = new LinkedHashMap<>();

        for (Constraint constraint : getConflicts()) {
            boolean process = false;
            if (constraint instanceof Prerequisite) {
                Prerequisite prerequisite = (Prerequisite) constraint;
                processPrereqForConflicts(counts, prerequisite);
                process = true;
            } else if (constraint instanceof ConstraintList) {
                ConstraintList cs = (ConstraintList) constraint;

                for (Constraint listConstraint : cs.getConflicts()) {
                    if (listConstraint instanceof Prerequisite) {
                        Prerequisite prerequisite = (Prerequisite) listConstraint;
                        processPrereqForConflicts(counts, prerequisite);
                        process = true;
                    } else if (listConstraint instanceof SemesterRestriction) {
                        // Semester restriction violations count thrice!
                        SemesterRestriction sr = (SemesterRestriction) listConstraint;
                        processCourseForConflicts(counts, sr.getCourse(), 3);
                        process = true;
                    }
                }
            } else if (constraint instanceof CourseListConstraint) {
                CourseListConstraint clc = (CourseListConstraint) constraint;

                for (ScheduledCourse course : clc.getConflicts()) {
                    // Course list violations count twice!
                    processCourseForConflicts(counts, course, 2);
                    process = true;
                }
            } else if (constraint instanceof SemesterRestriction) {
                // Semester restriction violations count thrice!
                SemesterRestriction sr = (SemesterRestriction) constraint;
                processCourseForConflicts(counts, sr.getCourse(), 3);
                process = true;
            }
        }

        return counts;
    }

    public SearchVariable getVariableWithTheMostConflicts() {
        Map<SearchVariable, Integer> counts = getVariablesWithConflictCounts();
        SearchVariable answer = null;

        int max = 0;

        for (SearchVariable key : counts.keySet()) {
            int violations = counts.get(key);

            if (violations > max) {
                max = violations;
                answer = key;
            }
        }

        return answer;
    }

}
