package edu.metrostate.ics340.vjp.localsearch;

import edu.metrostate.ics340.vjp.localsearch.constraints.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class ScheduleConflictList implements ConflictList {
    private static final long SEED;
    private static Random RANDOM;

    static {
        SEED = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        RANDOM = new Random(SEED);
    }

    Map<SearchVariable, Double> scores;
    List<Constraint> conflicts;

    /**
     * Initializes a new conflict list from the specified list of
     * constraints that are in conflict.
     *
     * @param conflicts the list of constraints that are in conflict.
     * @throws IllegalArgumentException indicates that conflicts is null.
     */
    public ScheduleConflictList(List<Constraint> conflicts) {
        if (conflicts == null) {
            throw new IllegalArgumentException("conflicts cannot be null.");
        }

        scores = new LinkedHashMap<>();
        this.conflicts = new ArrayList<>(conflicts.size());

        if (!conflicts.isEmpty()) {
            this.conflicts.addAll(conflicts);
        }
    }

    /**
     * Returns the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned.
     *
     * @return the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned.
     */
    @Override
    public int getNumberOfConflicts() {
        return conflicts.size();
    }

    /**
     * Returns an integer value that represents the score of the constraints that are in conflict. The higher the
     * score, the more sever the conflicts are. The conflict score may be higher than the number of constraints as
     * some conflicts may be weighted more than others.
     *
     * @return an integer value that represents the score of the constraints that are in conflict.
     */
    @Override
    public double getConflictsScore() {
        double answer = 0;

        for (SearchVariable variable : scores.keySet()) {
            answer += scores.get(variable);
        }

        return answer;
    }

    /**
     * Returns a map of the variables with conflicts and their score. Implementing classes may weight the constraint
     * violations. This means that the score for a variable may not be 1 to 1 with the number of conflicts that the
     * variable actually participates in.
     *
     * @return a map of the variables with conflicts and their score.
     */
    @Override
    public Map<SearchVariable, Double> getVariablesInConflictWithScores() {
        Map<SearchVariable, Double> answer = new LinkedHashMap<>();

        if (!scores.isEmpty()) {
            answer.putAll(scores);
        }

        return answer;
    }

    /**
     * Returns a variable that has the highest score within the list of constraints contained within this
     * ConflictList. If more than one variable have the greatest number of conflicts, then one of them is
     * randomly selected to be the one returned.
     *
     * @return a variable that has the most conflicts within the list of constraints contained within this
     * ConstraintList.
     */
    @Override
    public SearchVariable getVariableWithTheHighestScore() {
        SearchVariable answer = null;
        List<SearchVariable> maxConflicts = new ArrayList<>();

        double max = 0;

        for (SearchVariable key : scores.keySet()) {
            double violations = scores.get(key);

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

    /**
     * Returns true if this ConflictList is empty
     *
     * @return true if this ConflictList is empty
     */
    @Override
    public boolean isEmpty() {
        return conflicts.isEmpty();
    }

    /**
     * Returns the list of Constraints that are associated with this conflict list.
     *
     * @return
     */
    @Override
    public List<Constraint> getConflicts() {
        List<Constraint> answer = new ArrayList<>(conflicts.size());

        if (!conflicts.isEmpty()) {
            answer.addAll(conflicts);
        }

        return answer;
    }

    /**
     * Calculates the scores based on the current list of conflicts.
     */
    @Override
    public void scoreConflicts() {
        if (conflicts.isEmpty()) {
            return;
        }

        final double CONSTRAINT_SEMESTER_RESTRICTION_MULTIPLIER = 3.4;
        final double CONSTRAINT_COURSE_LIST_MULTIPLIER = 2.2;

        for (Constraint constraint : conflicts) {
            if (constraint instanceof Prerequisite) {
                Prerequisite prerequisite = (Prerequisite) constraint;
                processPrereqForConflicts(scores, prerequisite);
            } else if (constraint instanceof AbstractConstraintList) {
                ConstraintList cs = (ConstraintList) constraint;

                for (Constraint listConstraint : cs.getConflicts().getConflicts()) {
                    if (listConstraint instanceof Prerequisite) {
                        Prerequisite prerequisite = (Prerequisite) listConstraint;
                        processPrereqForConflicts(scores, prerequisite);
                    } else if (listConstraint instanceof SemesterRestriction) {
                        // Semester restriction violations count thrice!
                        SemesterRestriction sr = (SemesterRestriction) listConstraint;
                        processCourseForConflicts(scores, sr.getCourse(), CONSTRAINT_SEMESTER_RESTRICTION_MULTIPLIER);
                    }
                }
            } else if (constraint instanceof CourseListConstraint) {
                CourseListConstraint clc = (CourseListConstraint) constraint;

                for (ScheduledCourse course : clc.getConflicts()) {
                    // Course list violations count twice!
                    processCourseForConflicts(scores, course, CONSTRAINT_COURSE_LIST_MULTIPLIER);
                }
            } else if (constraint instanceof SemesterRestriction) {
                // Semester restriction violations count thrice!
                SemesterRestriction sr = (SemesterRestriction) constraint;
                processCourseForConflicts(scores, sr.getCourse(), CONSTRAINT_SEMESTER_RESTRICTION_MULTIPLIER);
            }
        }
    }

    /**
     * Returns the number of variables that are in conflict. scoreConflicts must be called first
     * in order for this method to return valid results.
     *
     * @return the number of variables that are in conflict. scoreConflicts must be called first
     * in order for this method to return valid results.
     */
    @Override
    public int getNumVariablesInConflict() {
        return scores.size();
    }

    /**
     * Returns another variable in conflict. If the specified variable is the only one in conflict it is simply
     * returned.
     *
     * @param variable the variable that we don't want to return if possible.
     * @return another variable in conflict. If the specified variable is the only one in conflict it is simply
     * returned.
     */
    @Override
    public SearchVariable getAnotherVariableInConflict(SearchVariable variable) {
        if (conflicts.isEmpty() || scores.size() == 1 || !scores.containsKey(variable)) {
            return variable;
        }

        SearchVariable answer = variable;

        for (SearchVariable sv : scores.keySet()) {
            if (!variable.equals(sv)) {
                answer = sv;
                break;
            }
        }

        return answer;
    }

    private void processPrereqForConflicts(Map<SearchVariable, Double> counts, Prerequisite prerequisite) {
        final double CONSTRAINT_PREREQUISITE_COURSE_MULTIPLIER = 1.0;
        final double CONSTRAINT_PREREQUISITE_PREREQUISITE_MULTIPLIER = 1.1;
        processCourseForConflicts(counts, prerequisite.getCourse(), CONSTRAINT_PREREQUISITE_COURSE_MULTIPLIER);
        processCourseForConflicts(counts, prerequisite.getPrerequisiteCourse(), CONSTRAINT_PREREQUISITE_PREREQUISITE_MULTIPLIER);
    }

    private void processCourseForConflicts(Map<SearchVariable, Double> counts, ScheduledCourse course, double times) {
        double count = 0.0;

        if (counts.containsKey(course)) {
            count = counts.get(course);
        }

        count += times;
        counts.put(course, count);
    }

}
