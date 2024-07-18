package com.codevengers.home_page;

import com.codevengers.Controller.Landing_Page;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HomePage {
    private Stage stage;

    public HomePage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Create a GridPane for the home page
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        // Add daily habits
        Label habitsLabel = new Label("Daily Habits:");
        gridPane.add(habitsLabel, 0, 0);

        Label habit1 = new Label("1. Exercise");
        Label habit2 = new Label("2. Read");
        Label habit3 = new Label("3. Meditate");
        Label habit4 = new Label("4. Journal");

        gridPane.add(habit1, 0, 1);
        gridPane.add(habit2, 0, 2);
        gridPane.add(habit3, 0, 3);
        gridPane.add(habit4, 0, 4);

        // Create the back button
        Button backButton = new Button("Back");

        backButton.setOnAction(event -> {
            Landing_Page landingPage = new Landing_Page();
            landingPage.start(stage);
        });

        gridPane.add(backButton, 0, 5);

        // Create the scene and set it on the stage
        Scene scene = new Scene(gridPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Home Page");
        stage.show();
    }
}
