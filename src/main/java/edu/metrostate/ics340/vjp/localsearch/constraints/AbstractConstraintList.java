/*
 * File: AbstractConstraintList.java
 */
package edu.metrostate.ics340.vjp.localsearch.constraints;

import edu.metrostate.ics340.vjp.localsearch.ScheduledCourse;
import edu.metrostate.ics340.vjp.localsearch.SearchVariable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * An AbstractConstraintList is an abstract base class for a type of Constraint where any, every, or none of the
 * constraints contained within it need to return true. See AnyConstraintList, EveryConstraintList,
 * and NoneConstraintList for more details. A ConstraintList is used by LocalSearch to determine when it has found a
 * solution to the problem.
 */
public abstract class AbstractConstraintList implements ConstraintList {
    private static final long SEED;
    private static Random RANDOM;

    static {
        SEED = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        RANDOM = new Random(SEED);
    }

    protected List<Constraint> constraints;

    /**
     * Initializes an empty list of constraints.
     *
     */
    public AbstractConstraintList() {
        this(null);
    }

    /**
     * Initializes a list of constraints from the specified list of constraints.
     *
     * @param constraints the list of constraints to initialize this list from.
     */
    public AbstractConstraintList(List<Constraint> constraints) {
        this.constraints = new ArrayList<>();

        if (constraints != null) {
            this.constraints.addAll(constraints);
        }
    }

    /**
     * Removes all of the constraints that have been added to this list.
     */
    @Override
    public void clear() {
        constraints.clear();
    }

    /**
     * Returns the number of constraints that have been added to this list.
     *
     * @return the number of constraints that have been added to this list.
     */
    @Override
    public int getNumConstraints() {
        return constraints.size();
    }

    /**
     * Returns an unmodifiable list of all the constraints contained within the list.
     *
     * @return an unmodifiable list of all the constraints contained within the list.
     */
    @Override
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
    @Override
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
    @Override
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
     * Returns the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned. Counts all constraint violations as a conflict.
     *
     * @return the number of conflicts this list has with it's constraints.
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
     * Returns the number of conflicts this list has with it's constraints that have been taken.
     * If there are no conflicts, then zero is returned. Counts all constraint satisfactions as a conflict.
     *
     * @return the number of conflicts this list has with it's constraints.
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
     * Returns the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned. Counts any constraint violations as a conflict
     * if there isn't at least one satisfying constraint.
     *
     * @return the number of conflicts this list has with it's constraints.
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
     * Returns the list of constraints that have conflicts with this list. If there are no conflicts,
     * then an empty list is returned. Counts all constraint violations as a conflict.
     *
     * @return the list of constraints that have conflicts with this list. If there are no conflicts,
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
     * Returns the list of constraints that have conflicts with this list. If there are no conflicts,
     * then an empty list is returned. Counts all constraint satisfactions as a conflict.
     *
     * @return the list of constraints that have conflicts with this list. If there are no conflicts,
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
     * Returns the list of constraints that have conflicts with this list. If there are no conflicts,
     * then an empty list is returned. Counts any constraint violations as a conflict if there isn't at
     * least one satisfying constraint.
     *
     * @return the list of constraints that have conflicts with this list. If there are no conflicts,
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
     * Returns true if this list has at least one conflict.
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
     * Returns true if this list has at least one conflict.
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
     * Returns an integer value that represents the score of the constraints that are in conflict. The higher the
     * score, the more sever the conflicts are. The conflict score may be higher than the number of constraints as
     * some conflicts may be weighted more than others.
     *
     * @return an integer value that represents the score of the constraints that are in conflict.
     */
    @Override
    public int getConflictsScore() {
        int answer = 0;

        final Map<SearchVariable, Integer> variablesInConflictWithScores = getVariablesInConflictWithScores();

        for (SearchVariable variable : variablesInConflictWithScores.keySet()) {
            answer += variablesInConflictWithScores.get(variable);
        }

        return answer;
    }

    /**
     * Returns a map with the variables as the keys and their violation score as the value.
     * Prerequisite violations are worth one, Course List violations are worth 2, and
     * Semester Restriction violations are worth 3.
     *
     * @return map with the variables as the keys and their violation score as the value.
     * Prerequisite violations are worth one, Course List violations are worth 2, and
     * Semester Restriction violations are worth 3.
     */
    @Override
    public Map<SearchVariable, Integer> getVariablesInConflictWithScores() {
        Map<SearchVariable, Integer> counts = new LinkedHashMap<>();

        for (Constraint constraint : getConflicts()) {
            boolean process = false;
            if (constraint instanceof Prerequisite) {
                Prerequisite prerequisite = (Prerequisite) constraint;
                processPrereqForConflicts(counts, prerequisite);
                process = true;
            } else if (constraint instanceof AbstractConstraintList) {
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

    /**
     * Returns a variable that has the most conflicts within the list of constraints contained within this
     * ConstraintList. If more than one variable have the greatest number of conflicts, then one of them is
     * randomly selected to be the one returned.
     *
     * @return a variable that has the most conflicts within the list of constraints contained within this
     * ConstraintList.
     */
    @Override
    public SearchVariable getVariableWithTheHighestScore() {
        Map<SearchVariable, Integer> counts = getVariablesInConflictWithScores();
        SearchVariable answer = null;
        List<SearchVariable> maxConflicts = new ArrayList<>();

        int max = 0;

        for (SearchVariable key : counts.keySet()) {
            int violations = counts.get(key);

            if (violations > max) {
                max = violations;
                maxConflicts.clear();
                maxConflicts.add(key);
                answer = key;
            } else if (violations == max && !maxConflicts.contains(key)) {
                maxConflicts.add(key);
            }
        }

        // If just one variable is the max we return it.
        // Otherwise we randomly select one to return.
        if (maxConflicts.size() == 1) {
            answer = maxConflicts.get(0);
        } else if (maxConflicts.size() > 1) {
            answer = maxConflicts.get(RANDOM.nextInt(maxConflicts.size()));
        }

        return answer;
    }

}
