/*
 * File: FindScheduleApplication.java
 */
package com.capital7software.ai.localsearch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main application class for the ICS 340 Local Search JavaFX application.
 * <p>
 * It uses Local Search as done in Section 4.8 of the Poole &amp; Mackworth textbook to determine a course schedule that
 * allows a student coming in with an AA degree in a completely non-technical subject to complete their Computer Science
 * degree at Metro State in n semesters.
 * <p>
 * It offers the choice of verbose or summary output.
 * <p>
 * Because we use Local Search, a different result is possible everytime you search for a solution :-D
 * <p>
 * See the LocalSearch class for more details on how the LocalSearch is actually performed.
 *
 * @author Vincent J. Palodichuk
 *
 */
public class FindScheduleApplication extends Application  implements EventHandler<ActionEvent> {
    private static final String APP_TITLE = "Find a Computer Science Schedule";
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final String MENU_FILE = "File";
    private static final String MENU_ACTIONS = "Actions";
    private static final String MENU_OPTIONS = "Options";
    private static final String MENU_HELP = "Help";
    private static final String ACTION_VERBOSE = "Verbose Output";
    private static final String ACTION_SUMMARY = "Summary Output";
    private static final String ACTION_AUTO_SAVE = "Auto Save Output";
    private static final String ACTION_CANCEL_SEARCH = "Cancel Local Search";
    private static final String ACTION_PERFORM_SEARCH = "Perform Local Search";
    private static final String ACTION_ABOUT = "About";
    private static final String ACTION_EXIT = "Exit";
    private static final String ACTION_CLEAR_OUTPUT = "Clear Output";
    private static final String ACTION_SAVE_OUTPUT = "Save Output...";
    private static final String MSG_EXECUTING_FMT = "%nPerforming Local Search";
    
    private final Label lblOutputMode = new Label();
    private final Label lblAutoSave = new Label();
    private final TextArea output = new TextArea();
    private final Menu fileMenu = new Menu(MENU_FILE);
    private final Menu actionsMenu = new Menu(MENU_ACTIONS);
    private final Menu optionsMenu = new Menu(MENU_OPTIONS);
    private final Menu helpMenu = new Menu(MENU_HELP);
    private ToolBar toolBar = null;
    private Stage stage = null;
    private boolean searching = false;
    private ExecutorService executor = null;

    private final Timeline searchTimer = new Timeline(new KeyFrame(Duration.millis(10), (event) -> {
    	if (isSearching()) {
    		output.appendText(".");
    	}
    }));
    
    synchronized public boolean isSearching() {
    	return searching;
    }
    
    synchronized private void beginSearch() {
        if (isSearching()) {
            return;
        }

        output.appendText(String.format(MSG_EXECUTING_FMT));
    	searching = true;
    	disableActions(true);
    	startTimer();
    }
    
    synchronized private void endSearch(String realResults, String realFileName) {
        if (!isSearching()) {
            return;
        }

    	searching = false;
    	disableActions(false);
    	stopTimer();

    	if (realFileName != null && !realFileName.trim().isEmpty()) {
            output.appendText("\n\n");
    	    output.appendText("Search results written to: " + realFileName);
        }

        if (realResults != null && !realResults.isEmpty()) {
            output.appendText("\n\n");
            output.appendText(realResults);
        }
    }
    
    private void startTimer() {
    	searchTimer.setCycleCount(Timeline.INDEFINITE);
    	searchTimer.play();
    }
    
    synchronized private void stopTimer() {
    	searchTimer.stop();
    }
    
    /**
     * Sets up the toolbar for this application.
     * @param actionEvent The event handler that will handle the toolbar actions.
     * @return the javafx.scene.control.ToolBar setup with buttons.
     */
    private ToolBar setupToolBar(EventHandler<ActionEvent> actionEvent) {
        ToolBar answer = null;
        
        Button save = new Button(ACTION_SAVE_OUTPUT);
        save.setOnAction(actionEvent);
        save.setId(ACTION_SAVE_OUTPUT);
        
        Button clear = new Button(ACTION_CLEAR_OUTPUT);
        clear.setOnAction(actionEvent);
        clear.setId(ACTION_CLEAR_OUTPUT);
        
        Button exit = new Button(ACTION_EXIT);
        exit.setOnAction(actionEvent);
        exit.setId(ACTION_EXIT);
        
        ToggleButton summary = new ToggleButton(ACTION_SUMMARY);
        summary.setOnAction(actionEvent);
        summary.setId(ACTION_SUMMARY);
        
        ToggleButton verbose = new ToggleButton(ACTION_VERBOSE);
        verbose.setOnAction(actionEvent);
        verbose.setId(ACTION_VERBOSE);
        
        verbose.setToggleGroup(summary.getToggleGroup());
        
        ToggleButton autoSave = new ToggleButton(ACTION_AUTO_SAVE);
        autoSave.setOnAction(actionEvent);
        autoSave.setId(ACTION_AUTO_SAVE);
        
        Button search = new Button(ACTION_PERFORM_SEARCH);
        search.setOnAction(actionEvent);
        search.setId(ACTION_PERFORM_SEARCH);
        
        answer = new ToolBar(save, clear, new Separator(Orientation.HORIZONTAL), exit,
        new Separator(Orientation.HORIZONTAL), summary, verbose, autoSave, new Separator(Orientation.HORIZONTAL), search);
        
        summary.setSelected(true);
        autoSave.setSelected(true);
        
        return answer;
    }
    
    /**
     * Sets up the menus for this application.
     * @param actionEvent The event handler that will handle the menu actions.
     * @return the javafx.scene.control.MenuBar setup with buttons.
     */
    private MenuBar setupMenu(EventHandler<ActionEvent> actionEvent) {
        /* File menu items: */
        MenuItem save = new MenuItem(ACTION_SAVE_OUTPUT);
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(actionEvent);
        save.setId(ACTION_SAVE_OUTPUT);
        
        MenuItem clear = new MenuItem(ACTION_CLEAR_OUTPUT);
        clear.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        clear.setOnAction(actionEvent);
        clear.setId(ACTION_CLEAR_OUTPUT);
        
        MenuItem exit = new MenuItem(ACTION_EXIT);
        exit.setAccelerator(KeyCombination.keyCombination("Alt+F4"));
        exit.setOnAction(actionEvent);
        exit.setId(ACTION_EXIT);
        
        fileMenu.getItems().addAll(save, new SeparatorMenuItem(), clear, new SeparatorMenuItem(), exit);
        
        /* Actionss menu items: */
        MenuItem search = new MenuItem(ACTION_PERFORM_SEARCH);
        search.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        search.setOnAction(actionEvent);
        search.setId(ACTION_PERFORM_SEARCH);
        
        actionsMenu.getItems().addAll(search, new SeparatorMenuItem());        
        
        /* Options menu items: */
        CheckMenuItem summary = new CheckMenuItem (ACTION_SUMMARY);
        summary.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        summary.setOnAction(actionEvent);
        summary.setId(ACTION_SUMMARY);
        
        CheckMenuItem verbose = new CheckMenuItem (ACTION_VERBOSE);
        verbose.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        verbose.setOnAction(actionEvent);
        verbose.setId(ACTION_VERBOSE);
        
        CheckMenuItem autoSave = new CheckMenuItem (ACTION_AUTO_SAVE);
        autoSave.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
        autoSave.setOnAction(actionEvent);
        autoSave.setId(ACTION_AUTO_SAVE);
        
        optionsMenu.getItems().addAll(summary, verbose, autoSave);
        
        /* Help menu items: */
        MenuItem about = new MenuItem(ACTION_ABOUT);
        about.setAccelerator(KeyCombination.keyCombination("Ctrl+F1"));
        about.setOnAction(actionEvent);
        about.setId(ACTION_ABOUT);
        
        helpMenu.getItems().addAll(about);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, actionsMenu, optionsMenu, helpMenu);
        
        summary.setSelected(true);
        autoSave.setSelected(true);
        
        return menuBar;
    }

    /**
     * Called in the event handler to display information about this program.
     */
    public void aboutProgram() {
        output.appendText("\n\nAbout this program:\n");
        output.appendText("Metro State University\n");
        output.appendText("ICS 340\n");
        output.appendText("Local Search Programming Assignment\n");
        output.appendText("by Vincent J. Palodichuk\n");
    }

    /**
     * Displays the Save File Dialog box to select a file to save the output to.
     * If a file is selected from the Save File Dialog, the output it written to it.
     * 
     * @param stage The stage that owns the File Save Dialog box.
     */
    public void saveOutput(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output to File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"))); // user.dir is the directory the JVM was executed from.
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            saveOutputToFile(output.getText(), file);
        }
    }
    
    /**
     * Saves the output text to the specified file.
     *
     * @param text the text to write to the file
     * @param file the file to write the text to
     * @throws IllegalStateException Indicates there is no selected file.
     */
    public void saveOutputToFile(String text, File file) {
        if (file == null) {
            throw new IllegalStateException("file cannot be null.");
        }
        
        if (text == null) {
        	throw new IllegalArgumentException("text cannot be null.");
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
        	bw.write(text);
        } catch (IOException ex) {
        	Logger.getLogger(FindScheduleApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Disables or Enables the GUI controls for the available actions of this program.
     * 
     * @param disable If true, the GUI action controls are disabled. If false,
     * the GUI action controls are enabled.
     * 
     */
    public void disableActions(boolean disable) {
        if (actionsMenu != null) {
        	actionsMenu.getItems().forEach((item) -> {
        	    if (ACTION_PERFORM_SEARCH.equals(item.getId()) ||
                        ACTION_CANCEL_SEARCH.equals(item.getId())) {
        	        if (disable) {
        	            item.setText(ACTION_CANCEL_SEARCH);
                    } else {
                        item.setText(ACTION_PERFORM_SEARCH);
                    }
                } else {
        	        item.setDisable(disable);
                }
            });
        }

        if (toolBar != null) {        
            toolBar
                .getItems()
                .forEach((node) -> {
                if (node instanceof Button btn) {
                    if (Objects.equals(btn.getText(), ACTION_PERFORM_SEARCH) ||
                            Objects.equals(btn.getText(), ACTION_CANCEL_SEARCH)) {
                        if (disable) {
                            btn.setText(ACTION_CANCEL_SEARCH);
                        } else {
                            btn.setText(ACTION_PERFORM_SEARCH);
                        }
                    } else {
                        btn.setDisable(disable);
                    }
                }
            });
        }
    }
    
    /**
     * Setups the labels that the application uses to display information about
     * the current graph.
     */
    private void setupTextArea() {
        output.setEditable(false);
        output.setWrapText(true);

        output.setStyle("-fx-font-family: 'Courier New'");
        output.setPadding(new Insets(5, 5, 5, 5));
    }
    

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        stage = theStage;
        stage.setTitle(APP_TITLE);
        BorderPane root = new BorderPane();

        VBox vbox = new VBox();
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.setFill(Color.VIOLET);
        
        MenuBar menu = setupMenu(this);
        toolBar = setupToolBar(this);
        disableActions(false);
        
        setupTextArea();
        
        vbox.getChildren().addAll(menu, toolBar);
        root.setTop(vbox);
        root.setCenter(output);
        //output.appendText("Before you can perform any actions, please open a Graph file by clicking the File menu and selecting Open or press Ctrl + O.");
        stage.setOnCloseRequest((event) -> {
            shutdownExecutor();
            Platform.exit();
        });
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    
    private void toggleAutoSave(Object obj, boolean button) {
    	boolean selected = false;
    	
    	if (button) {
    		ToggleButton btn = (ToggleButton) obj;
    		
    		selected = btn.isSelected();
    	} else {
    		CheckMenuItem itm = (CheckMenuItem) obj;
    		
    		selected = itm.isSelected();
    	}
    		
		for (Node node : toolBar.getItems()) {
			if (Objects.equals(ACTION_AUTO_SAVE, node.getId())) {
				ToggleButton btn = (ToggleButton)node;
				
				btn.setSelected(selected);
			}
		}
		
		for (MenuItem node : optionsMenu.getItems()) {
			if (Objects.equals(ACTION_AUTO_SAVE, node.getId())) {
				CheckMenuItem itm = (CheckMenuItem)node;
				
				itm.setSelected(selected);
			}
		}
    }
    
    private void toggleSummary(Object obj, boolean button) {
    	boolean selected = false;
    	
    	if (button) {
    		ToggleButton btn = (ToggleButton) obj;
    		
    		selected = btn.isSelected();
    	} else {
    		CheckMenuItem itm = (CheckMenuItem) obj;
    		
    		selected = itm.isSelected();
    	}
    		
		for (Node node : toolBar.getItems()) {
			if (Objects.equals(ACTION_SUMMARY, node.getId())) {
				ToggleButton btn = (ToggleButton)node;
				
				btn.setSelected(selected);
			}
			if (Objects.equals(ACTION_VERBOSE, node.getId())) {
				ToggleButton btn = (ToggleButton)node;
				
				btn.setSelected(!selected);
			}
		}
		
		for (MenuItem node : optionsMenu.getItems()) {
			if (Objects.equals(ACTION_SUMMARY, node.getId())) {
				CheckMenuItem itm = (CheckMenuItem)node;
				
				itm.setSelected(selected);
			}
			if (Objects.equals(ACTION_VERBOSE, node.getId())) {
				CheckMenuItem itm = (CheckMenuItem)node;
				
				itm.setSelected(!selected);
			}
		}
    }
    
    private void toggleVerbose(Object obj, boolean button) {
    	boolean selected = false;
    	
    	if (button) {
    		ToggleButton btn = (ToggleButton) obj;
    		
    		selected = btn.isSelected();
    	} else {
    		CheckMenuItem itm = (CheckMenuItem) obj;
    		
    		selected = itm.isSelected();
    	}
    		
		for (Node node : toolBar.getItems()) {
			if (Objects.equals(ACTION_SUMMARY, node.getId())) {
				ToggleButton btn = (ToggleButton)node;
				
				btn.setSelected(!selected);
			}
			if (Objects.equals(ACTION_VERBOSE, node.getId())) {
				ToggleButton btn = (ToggleButton)node;
				
				btn.setSelected(selected);
			}
		}
		
		for (MenuItem node : optionsMenu.getItems()) {
			if (Objects.equals(ACTION_SUMMARY, node.getId())) {
				CheckMenuItem itm = (CheckMenuItem)node;
				
				itm.setSelected(!selected);
			}
			if (Objects.equals(ACTION_VERBOSE, node.getId())) {
				CheckMenuItem itm = (CheckMenuItem)node;
				
				itm.setSelected(selected);
			}
		}
    }
    
    private boolean summaryOutput() {
    	boolean answer = false;
    	
		for (Node node : toolBar.getItems()) {
			if (Objects.equals(ACTION_SUMMARY, node.getId())) {
				ToggleButton btn = (ToggleButton)node;
				
				answer = btn.isSelected();
			}
		}
		
		return answer;
    }
    
    private boolean autoSave() {
    	boolean answer = false;
    	
		for (Node node : toolBar.getItems()) {
			if (Objects.equals(ACTION_AUTO_SAVE, node.getId())) {
				ToggleButton btn = (ToggleButton)node;
				
				answer = btn.isSelected();
			}
		}
		
		return answer;
    }
    
    /**
     * Handles events for UI Controls such as MenuItems and Buttons.
     * @param actionEvent The event object that describes the event being handled.
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        boolean button = false;
        
        if (source instanceof Control || source instanceof MenuItem) {
            String id = "";
            
            if (source instanceof Control) {
                id = ((Control) source).getId();
                button = true;
            } else {
                id = ((MenuItem) source).getId();
            }

            switch (id) {
                case ACTION_SAVE_OUTPUT -> saveOutput(stage);
                case ACTION_CLEAR_OUTPUT -> output.clear();
                case ACTION_EXIT -> {
                    shutdownExecutor();
                    Platform.exit();
                }
                case ACTION_ABOUT -> aboutProgram();
                case ACTION_VERBOSE -> toggleVerbose(source, button);
                case ACTION_SUMMARY -> toggleSummary(source, button);
                case ACTION_AUTO_SAVE -> toggleAutoSave(source, button);
                case ACTION_PERFORM_SEARCH -> {
                    if (isSearching()) {
                        cancelLocalSearch();
                    } else {
                        executeLocalSearch();
                    }
                }
            }
        }
    }

    private void shutdownExecutor() {
        if (executor != null && !executor.isTerminated()) {
            executor.shutdownNow();
            try {
                executor.awaitTermination(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {
            }
            executor = null;
        }
    }

    private void cancelLocalSearch() {
        if (executor != null && !executor.isShutdown() && !executor.isTerminated()) {
            shutdownExecutor();
        }

        if (isSearching()) {
            endSearch("Local Search has been cancelled.", "");
        }

    }
    private void executeLocalSearch() {
        beginSearch();

        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            executor = Executors.newCachedThreadPool();
        }

        executor.submit(() -> {
            LocalSearchProblem lsp = new LocalSearchProblem();
            LocalSearch ls = new LocalSearch(lsp.getVariables(), lsp, lsp.getConstraints());
            String results = "";
            String fileName = "";

            ls.search(0);

            if (summaryOutput()) {
                results = ls.getSummary();
            } else {
                results = ls.getLog();
            }

            if (autoSave()) {
                try {
                    fileName = getAutoSaveFileName(fileName);

                    if (!Files.exists(Paths.get(fileName))) {
                        Files.createFile(Paths.get(fileName));
                    }

                    saveOutputToFile(results, Paths.get(fileName).toFile());

                } catch (IOException ex) {
                    Logger.getLogger(FindScheduleApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            final String realResults = results;
            final String realFileName = fileName;

            Platform.runLater(() -> {
                endSearch(realResults, realFileName);
            });
        });
    }

    private static String getAutoSaveFileName(String fileName) {
        String directory = System.getProperty("user.dir");
        String fullPath = "LocalSearch_On_" +
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() +
                ".txt";
        String ext = "";
        String name = "";

        int extIndex = fullPath.lastIndexOf('.');

        if (extIndex >= 0) {
            ext = fullPath.substring(extIndex);
        }

        int index = fullPath.lastIndexOf(File.separatorChar);

        if (index >= 0) {
            name = fullPath.substring(index + 1, extIndex >= 0 ? extIndex : fullPath.length());
        } else {
            fullPath = Paths.get(fullPath).getFileName().toString();

            extIndex = fullPath.lastIndexOf('.');

            if (extIndex >= 0) {
                ext = fullPath.substring(extIndex);
            }

            name = fullPath.substring(0, extIndex >= 0 ? extIndex : fullPath.length());
        }

        String outFileName = name + ext;

        fileName = directory + File.separatorChar + outFileName;
        return fileName;
    }

}
