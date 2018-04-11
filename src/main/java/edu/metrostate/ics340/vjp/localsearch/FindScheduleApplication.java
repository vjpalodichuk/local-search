/*
 * File: FindScheduleApplication.java
 */
package edu.metrostate.ics340.vjp.localsearch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The main application class for the ICS 340 Local Search JavaFX application.
 *
 * It uses Local Search as done in Section 4.8 of the Poole &amp; Mackworth textbook to determine a course schedule that
 * allows a student coming in with an AA degree in a completely non-technical subject to complete their Computer Science
 * degree at Metro State in n semesters.
 *
 * It offers the choice of verbose or summary output.
 *
 * See the LocalSearch class for more details on how the LocalSearch is actually performed.
 *
 * @author Vincent J. Palodichuk
 *
 */
public class FindScheduleApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        if (Platform.isFxApplicationThread()) {
            Platform.runLater(() -> {
                if (primaryStage != null) {
                    Platform.exit();
                }
            });
        }
    }
}
