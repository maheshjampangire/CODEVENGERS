package com.codevengers.dashboards;

import com.codevengers.firebase_config.DataService;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.animation.*;
//import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ToDoPage {

    private DataService dataService;
    private Map<String, Task> tasks = new HashMap<>();

    public ToDoPage(DataService dataService) {
        this.dataService = dataService;
    }

    public VBox createToDoScene(Runnable logoutHandler, String userEmail) {
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

        ellipsisMenu.getItems().addAll(logoutMenuItem, exitMenuItem, aboutMenuItem);

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
        Text welcomeText = new Text("Hi, " + dataLabel.getText() + " Welcome In ToDo");
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeText.setFill(Color.SADDLEBROWN);

        // Add profile image and welcome text to the header
        header.getChildren().addAll(welcomeText);

        // Ensure ellipsis menu is at the right side of the header
        HBox headerContainer = new HBox(header, ellipsisBox);
        HBox.setHgrow(header, Priority.ALWAYS);
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        headerContainer.setSpacing(10);

        // Create a container for the tasks section
        VBox tasksContainer = new VBox();
        tasksContainer.setPadding(new Insets(20, 20, 20, 20));
        tasksContainer.setSpacing(10);
        tasksContainer.setMinWidth(50); // Set minimum width
        tasksContainer.setPrefWidth(150); // Set preferred width
        tasksContainer.setMinHeight(50); // Set minimum height
        tasksContainer.setPrefHeight(200); // Set preferred height

        // Fetch existing tasks from Firestore and display them
        try {
            DocumentSnapshot tasksData = dataService.getData("tasks", userEmail);
            Map<String, Object> tasksMap = tasksData.getData();
            if (tasksMap != null) {
                for (Map.Entry<String, Object> entry : tasksMap.entrySet()) {
                    Map<String, Object> taskData = (Map<String, Object>) entry.getValue();
                    String taskName = (String) taskData.get("name");
                    String taskDescription = (String) taskData.get("description");
                    String taskDays = (String) taskData.get("days"); // Retrieve task days
                    boolean taskCompleted = (boolean) taskData.get("completed");

                    VBox taskBox = createTaskBox(taskName, taskDescription, taskDays, taskCompleted, userEmail);
                    tasksContainer.getChildren().add(taskBox);

                    tasks.put(taskName, new Task(taskName, taskDescription, taskDays, taskCompleted));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Create a form for adding new tasks
        VBox addTaskForm = new VBox(10);
        addTaskForm.setPadding(new Insets(20));
        addTaskForm.setAlignment(Pos.CENTER);
        addTaskForm.setStyle("-fx-background-color: #2e2e2e; -fx-border-radius: 10; -fx-background-radius: 10;");
        addTaskForm.setMinWidth(70);

        TextField taskNameField = new TextField();
        taskNameField.setPromptText("Task Name");
        taskNameField.setStyle("-fx-background-color: #3e3e3e; -fx-text-fill: white; -fx-font-size: 14px;");

        TextField taskDescField = new TextField();
        taskDescField.setPromptText("Task Description");
        taskDescField.setStyle("-fx-background-color: #3e3e3e; -fx-text-fill: white; -fx-font-size: 14px;");

        TextField taskDayField = new TextField();
        taskDayField.setPromptText("Task Days");
        taskDayField.setStyle("-fx-background-color: #3e3e3e; -fx-text-fill: white; -fx-font-size: 14px;");

        Button addTaskButton = new Button("Add Task");
        addTaskButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");

        addTaskForm.getChildren().addAll(taskNameField, taskDescField, taskDayField, addTaskButton);

        addTaskButton.setOnAction(event -> {
            String taskName = taskNameField.getText();
            String taskDesc = taskDescField.getText();
            String taskDay = taskDayField.getText();
            if (!taskName.isEmpty() && !taskDesc.isEmpty() && !taskDay.isEmpty()) {
                VBox newTaskBox = createTaskBox(taskName, taskDesc, taskDay, false, userEmail);
                tasks.put(taskName, new Task(taskName, taskDesc, taskDay, false));
                tasksContainer.getChildren().add(newTaskBox);

                // Save the new task to Firestore
                try {
                    Map<String, Object> taskData = new HashMap<>();
                    taskData.put("name", taskName);
                    taskData.put("description", taskDesc);
                    taskData.put("days", taskDay);
                    taskData.put("completed", false); // Initial state

                    dataService.addData("tasks", userEmail, taskData);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                taskNameField.clear();
                taskDescField.clear();
                taskDayField.clear();
            }
        });

        // Main layout container
        VBox mainLayout = new VBox(200);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(headerContainer, tasksContainer, addTaskForm);
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Create gradient background
        Rectangle colors = new Rectangle();
        colors.setFill(new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[] {
                new Stop(0, Color.web("#0d47a1")), // Deep blue
                new Stop(1, Color.web("#8e24aa")) // Purple
        }));

        // Ensure gradient background fills the entire screen
        StackPane root = new StackPane();
        root.getChildren().add(colors);
        colors.widthProperty().bind(root.widthProperty());
        colors.heightProperty().bind(root.heightProperty());

        // Create animated circles
        Pane circles = new Pane();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            Circle circle = new Circle(150, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }

        // Ensure circles pane fills the entire screen
        circles.setPrefSize(root.getWidth(), root.getHeight());
        circles.prefWidthProperty().bind(root.widthProperty());
        circles.prefHeightProperty().bind(root.heightProperty());

        Timeline timeline = new Timeline();
        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(circle.translateXProperty(), random.nextDouble() * root.getPrefWidth()),
                            new KeyValue(circle.translateYProperty(), random.nextDouble() * root.getPrefHeight())),
                    new KeyFrame(new Duration(40000), // set end position at 40s
                            new KeyValue(circle.translateXProperty(), random.nextDouble() * root.getPrefWidth()),
                            new KeyValue(circle.translateYProperty(), random.nextDouble() * root.getPrefHeight())));
        }
        // play 40s of animation
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        root.getChildren().addAll(circles, mainLayout);

        // Create VBox to return
        VBox vbox = new VBox(root);
        vbox.setAlignment(Pos.CENTER);
        VBox.setVgrow(root, Priority.ALWAYS);
        return vbox;
    }

    private VBox createTaskBox(String taskName, String description, String taskDays, boolean completed,
            String userEmail) {
        VBox taskBox = new VBox(10);
        taskBox.setAlignment(Pos.CENTER);
        taskBox.setPadding(new Insets(10));
        taskBox.setStyle("-fx-background-color: " + (completed ? "#4CAF50;" : "#2e2e2e;") +
                " -fx-border-radius: 10; -fx-background-radius: 10;");

        Text taskNameText = new Text(taskName);
        taskNameText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        taskNameText.setFill(Color.WHITE);

        Text descriptionText = new Text(description);
        descriptionText.setFont(Font.font("Arial", 14));
        descriptionText.setFill(Color.GRAY);

        Text taskDaysText = new Text("Days: " + taskDays);
        taskDaysText.setFont(Font.font("Arial", 14));
        taskDaysText.setFill(Color.GRAY);

        taskBox.getChildren().addAll(taskNameText, descriptionText, taskDaysText);

        // Add click event to toggle task completion
        taskBox.setOnMouseClicked(event -> {
            // Toggle completion status
            Task task = tasks.get(taskName);
            if (task != null) {
                task.completed = !task.completed;
                taskBox.setStyle("-fx-background-color: " + (task.completed ? "#4CAF50;" : "#2e2e2e;") +
                        " -fx-border-radius: 10; -fx-background-radius: 10;");

                // Update Firestore with the new completion status
                try {
                    dataService.updateTaskCompletion("tasks", userEmail, taskName, task.completed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Add hover effect
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), taskBox);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), taskBox);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        taskBox.setOnMouseEntered(event -> scaleIn.play());
        taskBox.setOnMouseExited(event -> scaleOut.play());

        return taskBox;
    }

    private void displayAboutDialog() {
        // Implement your about dialog display logic here
    }

    private class Task {
        private String name;
        private String description;
        private String days; // Added task days
        private boolean completed;

        public Task(String name, String description, String days, boolean completed) {
            this.name = name;
            this.description = description;
            this.days = days;
            this.completed = completed;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getDays() {
            return days;
        }

        public boolean isCompleted() {
            return completed;
        }
    }
}