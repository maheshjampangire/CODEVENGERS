package com.codevengers.initialpage;

import com.codevengers.Controller.Landing_Page;

//import javafx.animation.FadeTransition;
//import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
//import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//import javafx.util.Duration;

public class About extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load the background image
        ImageView backgroundImage = new ImageView(new Image("assets//about1.png"));
        backgroundImage.setFitWidth(1800); // Set the desired width
        backgroundImage.setFitHeight(1000); // Set the desired height
        backgroundImage.setPreserveRatio(true);

        // Create the animated text
        // Label animatedText = new Label("Welcome to Habit Tracker!\n\n"
        // + "Track your daily habits and improve your productivity with ease.\n"
        // + "Stay on top of your goals and achieve more every day!");
        // animatedText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        // animatedText.setTextFill(Color.BLACK);
        // animatedText.setWrapText(true);
        // animatedText.setAlignment(Pos.TOP_CENTER);
        // animatedText.setMaxWidth(700);

        // // Create a VBox to hold the animated text
        // VBox textBox = new VBox(animatedText);
        // textBox.setAlignment(Pos.TOP_CENTER);

        // // Add fade-in animation to the text
        // FadeTransition fadeIn = new FadeTransition(Duration.seconds(2),
        // animatedText);
        // fadeIn.setFromValue(0);
        // fadeIn.setToValue(1);
        // fadeIn.setCycleCount(1);

        // // Add a slide-in animation to the text
        // TranslateTransition slideIn = new TranslateTransition(Duration.seconds(2),
        // animatedText);
        // slideIn.setFromX(-800);
        // slideIn.setToX(0);
        // slideIn.setCycleCount(1);

        // // Combine animations in a parallel transition
        // javafx.animation.ParallelTransition parallelTransition = new
        // javafx.animation.ParallelTransition(fadeIn, slideIn);
        // parallelTransition.play();

        // Create a "Next" button
        Button nextButton = new Button(">");
        nextButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nextButton.setTextFill(Color.WHITE);
        nextButton.setStyle("-fx-background-color: #2a5298; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        nextButton.setOnAction(event -> {
            // Open the landing page
            openLandingPage(primaryStage);
        });

        // Create a VBox to hold the button
        VBox buttonBox = new VBox(nextButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setSpacing(20);
        buttonBox.setTranslateY(-50); // Adjust vertical position

        // Create a StackPane to hold the background image, text box, and button
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImage, buttonBox);

        // Create and set up the scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("About");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openLandingPage(Stage primaryStage) {
        // Initialize and start the Landing_Page application
        Landing_Page landingPageApp = new Landing_Page();
        landingPageApp.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}