package com.codevengers.dashboards;

import com.codevengers.firebase_config.DataService;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserPage {

    static String userName;
    private DataService dataService;
    private Map<String, Habit> habits = new HashMap<>();
    VBox vb;

    public UserPage(DataService dataService) {
        this.dataService = dataService;
        initializeSampleHabits();
    }

    private void initializeSampleHabits() {
        habits.put("Morning Exercise", new Habit("Morning Exercise", "30 minutes of workout", 30, false, 0.0));
        habits.put("WakeUp", new Habit("WakeUp", "WakeUp at 6:00 AM", 30, false, 0.0));
        habits.put("Meditation", new Habit("Meditation", "Meditate for 10 minutes", 10, false, 0.0));
        habits.put("Coding Practice", new Habit("Coding Practice", "Practice coding for 1 hour", 60, false, 0.0));
        habits.put("Drink Water", new Habit("Drink Water", "Drink at least 4L water daily", 30, false, 0.0));
    }

    public VBox createUserScene(Runnable logoutHandler, String userEmail) {
        Label dataLabel = new Label();

        try {
            DocumentSnapshot dataObject = dataService.getData("users", userEmail);

            String firstName = dataObject.getString("firstName"); // Retrieve first name here

            System.out.println("First name fetched: " + firstName);
            dataLabel.setText(firstName); // Set first name to label
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Create an ellipsis menu for options at the top right
        Button ellipsisButton = new Button("⋮");
        ellipsisButton.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-text-fill: white;");

        // Create a context menu for the ellipsis button
        ContextMenu ellipsisMenu = new ContextMenu();

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> System.exit(0)); // Exit application on click

        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setOnAction(event -> displayAboutDialog()); // Show about dialog on click

        MenuItem logoutMenuItem = new MenuItem("Logout");
        logoutMenuItem.setOnAction(event -> logoutHandler.run()); // Logout on click

        MenuItem myRewardsItem = new MenuItem("My Rewards");
        myRewardsItem.setOnAction(event -> displayRewardsDialog()); // My Rewards on click

        ellipsisMenu.getItems().addAll(logoutMenuItem, exitMenuItem, aboutMenuItem, myRewardsItem);

        ellipsisButton.setOnAction(event -> ellipsisMenu.show(ellipsisButton, Side.BOTTOM, 0, 0));

        HBox ellipsisBox = new HBox(ellipsisButton);
        ellipsisBox.setAlignment(Pos.CENTER_RIGHT);
        ellipsisBox.setPadding(new Insets(10));

        // Create a container for the header
        HBox header = new HBox();
        header.setPadding(new Insets(20, 20, 20, 20));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        // Create welcome text
        Text welcomeText = new Text("Hi, " + dataLabel.getText());
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeText.setFill(Color.LAVENDER);

        // Add profile image and welcome text to the header
        header.getChildren().addAll(welcomeText);

        // Ensure ellipsis menu is at the right side of the header
        HBox headerContainer = new HBox(header, ellipsisBox);
        HBox.setHgrow(header, Priority.ALWAYS);
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        headerContainer.setSpacing(10);

        // Create a container for the habits section
        GridPane habitsGrid = new GridPane();
        habitsGrid.setPadding(new Insets(20, 20, 20, 20));
        habitsGrid.setVgap(30); // Increased vertical gap between rows
        habitsGrid.setHgap(40); // Increased horizontal gap between columns

        // Add existing habits to the grid
        int habitCount = 0;
        for (Map.Entry<String, Habit> entry : habits.entrySet()) {
            Habit habit = entry.getValue();
            VBox habitBox = createHabitBox(habit.name, habit.description, habit.days, habit.completed, habit.progress);
            habitsGrid.add(habitBox, habitCount % 2, habitCount / 2);
            habitCount++;
        }

        // Fetch existing habits from Firestore and display them
        try {
            DocumentSnapshot habitsData = dataService.getData("habits", userEmail);
            Map<String, Object> habitsMap = habitsData.getData();
            if (habitsMap != null) {
                for (Map.Entry<String, Object> entry : habitsMap.entrySet()) {
                    Map<String, Object> habitData = (Map<String, Object>) entry.getValue();
                    String habitName = (String) habitData.get("name");
                    String habitDescription = (String) habitData.get("description");
                    int habitDays = (int) (long) habitData.get("days");
                    boolean habitCompleted = (boolean) habitData.get("completed");
                    double habitProgress = (double) habitData.get("progress");

                    VBox habitBox = createHabitBox(habitName, habitDescription, habitDays, habitCompleted,
                            habitProgress);
                    habitsGrid.add(habitBox, habitsGrid.getChildren().size() % 2, habitsGrid.getChildren().size() / 2);

                    habits.put(habitName,
                            new Habit(habitName, habitDescription, habitDays, habitCompleted, habitProgress));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Create a form for adding new habits
        VBox addHabitForm = new VBox(10);
        addHabitForm.setPadding(new Insets(20));
        addHabitForm.setAlignment(Pos.CENTER);
        addHabitForm.setStyle("-fx-background-color: #2e2e2e; -fx-border-radius: 10; -fx-background-radius: 10;");
        addHabitForm.setMinWidth(300);

        TextField habitNameField = new TextField();
        habitNameField.setPromptText("Habit Name");
        habitNameField.setStyle("-fx-background-color: #3e3e3e; -fx-text-fill: white; -fx-font-size: 14px;");

        TextField habitDescField = new TextField();
        habitDescField.setPromptText("Habit Description");
        habitDescField.setStyle("-fx-background-color: #3e3e3e; -fx-text-fill: white; -fx-font-size: 14px;");

        TextField habitDaysField = new TextField();
        habitDaysField.setPromptText("Number of Days");
        habitDaysField.setStyle("-fx-background-color: #3e3e3e; -fx-text-fill: white; -fx-font-size: 14px;");

        Button addHabitButton = new Button("Add Habit");
        addHabitButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");

        addHabitForm.getChildren().addAll(habitNameField, habitDescField, habitDaysField, addHabitButton);

        addHabitButton.setOnAction(event -> {
            String habitName = habitNameField.getText();
            String habitDesc = habitDescField.getText();
            int habitDays = Integer.parseInt(habitDaysField.getText());
            if (!habitName.isEmpty() && !habitDesc.isEmpty() && habitDays > 0) {
                VBox newHabitBox = createHabitBox(habitName, habitDesc, habitDays, false, 0.0);
                habits.put(habitName, new Habit(habitName, habitDesc, habitDays, false, 0.0));
                habitsGrid.add(newHabitBox, habitsGrid.getChildren().size() % 2, habitsGrid.getChildren().size() / 2);

                // Save the new habit to Firestore
                try {
                    Map<String, Object> habitData = new HashMap<>();
                    habitData.put("name", habitName);
                    habitData.put("description", habitDesc);
                    habitData.put("days", habitDays);
                    habitData.put("completed", false); // Initial state
                    habitData.put("progress", 0.0); // Initial progress

                    dataService.addData("habits", userEmail, habitData);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                habitNameField.clear();
                habitDescField.clear();
                habitDaysField.clear();
            }
        });

        // Create a button to navigate to the ToDoPage
        Button toDoPageButton = new Button("Go to ToDo Page");
        toDoPageButton.setStyle(
                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        toDoPageButton.setOnAction(event -> {
            ToDoPage toDoPage = new ToDoPage(dataService);
            Stage stage = (Stage) toDoPageButton.getScene().getWindow();
            stage.setScene(new Scene(toDoPage.createToDoScene(logoutHandler, userEmail), 800, 600));
        });

        // Main layout container
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(headerContainer, habitsGrid, addHabitForm, toDoPageButton);
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Set background with animated circles
        Pane backgroundPane = createBackgroundWithCircles();
        mainLayout.getChildren().add(0, backgroundPane);

        return mainLayout;
    }

    private Pane createBackgroundWithCircles() {
        Pane backgroundPane = new Pane();
        backgroundPane.setStyle("-fx-background-color: LIGHTGREY;");

        Scene scene = new Scene(backgroundPane, 1900, 1400); // Adjusted width to fit image and form

        // Add gradient background
        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[] {
                        new Stop(0, Color.web("#00008B")), // Deep Blue
                        new Stop(1, Color.web("#8A2BE2")), // BlueViolet (purplish)
                }));
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());
        backgroundPane.getChildren().add(colors);

        // Create animated circles
        Random random = new Random();
        HBox circles = new HBox();
        for (int i = 0; i < 30; i++) {
            Circle circle = new Circle(150, Color.web("white", 0.05));
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }
        backgroundPane.getChildren().add(circles);

        // Animate circles
        Timeline timeline = new Timeline();
        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, // set start position at 0
                            new KeyValue(circle.translateXProperty(), circle.getTranslateX()),
                            new KeyValue(circle.translateYProperty(), circle.getTranslateY())),
                    new KeyFrame(new Duration(40000), // set end position at 40s
                            new KeyValue(circle.translateXProperty(), random.nextDouble() * 800),
                            new KeyValue(circle.translateYProperty(), random.nextDouble() * 600)));
        }
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Add the content box to the root StackPane
        backgroundPane.getChildren();
        return backgroundPane;
    }

    private VBox createHabitBox(String habitName, String description, int days, boolean completed, double progress) {
        VBox habitBox = new VBox(10);
        habitBox.setAlignment(Pos.CENTER);
        habitBox.setPadding(new Insets(10));
        habitBox.setStyle("-fx-background-color: " + (completed ? "#4CAF50;" : "#2e2e2e;") +
                " -fx-border-radius: 10; -fx-background-radius: 10;");

        // Set the width and height of the habit box
        double boxWidth = 200; // Set your desired width here
        double boxHeight = 150; // Set your desired height here
        habitBox.setMinWidth(boxWidth);
        habitBox.setPrefWidth(boxWidth);
        habitBox.setMaxWidth(boxWidth);
        habitBox.setMinHeight(boxHeight);
        habitBox.setPrefHeight(boxHeight);
        habitBox.setMaxHeight(boxHeight);

        Text habitNameText = new Text(habitName);
        habitNameText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        habitNameText.setFill(Color.WHITE);

        Text descriptionText = new Text(description);
        descriptionText.setFont(Font.font("Arial", 14));
        descriptionText.setFill(Color.GRAY);

        Text daysText = new Text("Days: " + days);
        daysText.setFont(Font.font("Arial", 14));
        daysText.setFill(Color.GRAY);

        ProgressBar progressBar = new ProgressBar(progress);
        progressBar.setPrefWidth(150);

        habitBox.getChildren().addAll(habitNameText, descriptionText, daysText, progressBar);

        // Add hover effect
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), habitBox);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), habitBox);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        habitBox.setOnMouseEntered(event -> scaleIn.play());
        habitBox.setOnMouseExited(event -> scaleOut.play());

        // Toggle completion status on click
        habitBox.setOnMouseClicked(event -> {
            if (!completed) {
                double newProgress = habits.get(habitName).getProgress() + 1;
                habits.get(habitName).setProgress(newProgress);
                progressBar.setProgress(newProgress / days);

                if (newProgress >= days) {
                    habitBox.setStyle(
                            "-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-background-radius: 10;");
                    habits.get(habitName).setCompleted(true); // Update completed state in Habit object
                }

                // Update progress in Firestore
                try {
                    dataService.updateHabitProgress("habits", userName, habitName, newProgress / days);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return habitBox;
    }

    private void displayAboutDialog() {
        // Implement your about dialog display logic here
    }

    private void displayRewardsDialog() {
        // Implement your rewards dialog display logic here
    }

    private class Habit {
        private String name;
        private String description;
        private int days;
        private boolean completed;
        private double progress;

        public Habit(String name, String description, int days, boolean completed, double progress) {
            this.name = name;
            this.description = description;
            this.days = days;
            this.completed = completed;
            this.progress = progress;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getDays() {
            return days;
        }

        public boolean isCompleted() {
            return completed;
        }

        public double getProgress() {
            return progress;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public void setProgress(double progress) {
            this.progress = progress;
        }
    }
}