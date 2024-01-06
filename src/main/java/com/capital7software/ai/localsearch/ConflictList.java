/*
 * File: ConflictList.java
 */
package com.capital7software.ai.localsearch;

import com.capital7software.ai.localsearch.constraints.Constraint;

import java.util.List;
import java.util.Map;

/**
 * The ConflictList class provides data about a list of conflicts. It is capable of providing the total number of
 * conflicts, the score of all of the conflicts, a list of SearchVariable and their individual score, and many other
 * useful properties.
 *
 * @author Vincent J. Palodichuk
 */
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
     * @return a map of the variables with conflicts and their score.
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
    boolean isEmpty();

    /**
     * Returns the list of Constraints that are associated with this conflict list.
     *
     * @return the list of Constraints that are associated with this conflict list.
     */
    List<Constraint> getConflicts();

    /**
     * Calculates the scores based on the current list of conflicts.
     */
    void scoreConflicts();

    /**
     * Returns the number of variables that are in conflict. scoreConflicts must be called first
     * in order for this method to return valid results.
     *
     * @return the number of variables that are in conflict. scoreConflicts must be called first
     * in order for this method to return valid results.
     */
    int getNumVariablesInConflict();

    /**
     * Returns a variable in conflict at random.
     *
     * @return a variable in conflict at random.
     */
    SearchVariable getRandomVariableInConflict();

    /**
     * Returns another variable in conflict. If the specified variable is the only one in conflict it is simply
     * returned.
     *
     * @param variable the variable that we don't want to return if possible.
     *
     * @return another variable in conflict. If the specified variable is the only one in conflict it is simply
     * returned.
     */
    SearchVariable getAnotherVariableInConflict(SearchVariable variable);
}
