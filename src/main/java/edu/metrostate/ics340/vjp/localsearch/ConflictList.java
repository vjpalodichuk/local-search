package edu.metrostate.ics340.vjp.localsearch;

import edu.metrostate.ics340.vjp.localsearch.constraints.Constraint;

import java.util.List;
import java.util.Map;

public interface ConflictList {
    /**
     * Returns the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned.
     *
     * @return the number of conflicts this list has with it's constraints.
     * If there are no conflicts, then zero is returned.
     */
    int getNumberOfConflicts();

    /**
     * Returns a double value that represents the score of the constraints that are in conflict. The higher the
     * score, the more sever the conflicts are. The conflict score may be higher than the number of constraints as
     * some conflicts may be weighted more than others.
     *
     * @return an integer value that represents the score of the constraints that are in conflict.
     */
    double getConflictsScore();

    /**
     * Returns a map of the variables with conflicts and their score. Implementing classes may weight the constraint
     * violations. This means that the score for a variable may not be 1 to 1 with the number of conflicts that the
     * variable actually participates in.
     *
     * @return
     */
    Map<SearchVariable, Double> getVariablesInConflictWithScores();

    /**
     * Returns a variable that has the most conflicts within the list of constraints contained within this
     * ConstraintList. If more than one variable have the greatest number of conflicts, then one of them is
     * randomly selected to be the one returned.
     *
     * @return a variable that has the most conflicts within the list of constraints contained within this
     * ConstraintList.
     */
    SearchVariable getVariableWithTheHighestScore();

    /**
     * Returns true if this ConflictList is empty
     *
     * @return true if this ConflictList is empty
     */
    public boolean isEmpty();

    /**
     * Returns the list of Constraints that are associated with this conflict list.
     * @return
     */
    public List<Constraint> getConflicts();
}
