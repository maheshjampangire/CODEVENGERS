package com.codevengers.initialpage;

import java.util.Random;

import com.codevengers.Controller.Landing_Page;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InitialApp extends Application {

        @Override
        public void start(Stage primaryStage) {
                primaryStage.setTitle("Routiner");
                primaryStage.getIcons().add(new Image("assets/about.png"));
                primaryStage.setResizable(true);
                primaryStage.setHeight(1000);
                primaryStage.setWidth(1800);

                // Load the image
                Image logoImage = new Image("assets/about.png");
                Ellipse imageEllipse = new Ellipse(500, 250); // Set width radius and height radius
                imageEllipse.setFill(new ImagePattern(logoImage));

                // Create a label with the text "Routiner"
                Label titleLabel = new Label("Routiner");
                titleLabel.setTextFill(Color.WHITE);
                titleLabel.setFont(new Font("Poppins", 100)); // Adjust font and size as needed

                // Create a slogan label
                Label sloganLabel = new Label("Your Path to Better Habits, Every Day");
                sloganLabel.setTextFill(Color.WHITE);
                sloganLabel.setFont(new Font("Poppins", 40)); // Adjust font and size as needed

                // Create a VBox and add the image and labels to it
                VBox vbox = new VBox(20, imageEllipse, titleLabel, sloganLabel); // 20 is the spacing between the
                                                                                 // elements
                vbox.setAlignment(Pos.CENTER); // Center the elements in the VBox

                // Create a StackPane and add the VBox to it
                StackPane stackPane = new StackPane(vbox);
                stackPane.setAlignment(Pos.CENTER); // Center the VBox in the StackPane

                // Create a gradient background
                Stop[] stops = new Stop[] {
                                new Stop(0, Color.web("#1e3c72")), // Start color
                                new Stop(1, Color.web("#2a5298")) // End color
                };
                LinearGradient linearGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
                stackPane.setStyle("-fx-background-color: linear-gradient(#1e3c72, #2a5298);");

                // Create a pane for animated circles
                Pane circlesPane = new Pane();
                stackPane.getChildren().add(circlesPane);

                // Add animated circles to the pane
                addAnimatedCircles(circlesPane, 30); // Add 30 animated circles

                // Create a Scene with the StackPane as the root
                Scene scene = new Scene(stackPane);

                // Set up the stage
                primaryStage.setTitle("Routiner");
                primaryStage.setScene(scene);
                // primaryStage.setFullScreen(true);
                primaryStage.show();

                // Create a Timeline for delaying the transition to about page
                Timeline timeline = new Timeline(
                                new KeyFrame(Duration.seconds(5), event -> {
                                        openAboutPage(primaryStage);
                                }));
                timeline.play();
        }

        private void openAboutPage(Stage primaryStage) {
                // Initialize and start the About page
                About aboutPage = new About();
                aboutPage.start(primaryStage);
        }

        // private void openLandingPage(Stage primaryStage) {
        //         // Initialize and start the Landing_Page application
        //         Landing_Page landingPageApp = new Landing_Page();
        //         landingPageApp.start(primaryStage);
        // }

        private void addAnimatedCircles(Pane pane, int numCircles) {
                Random random = new Random();
                Timeline timeline = new Timeline();
                Group circles = new Group();

                for (int i = 0; i < numCircles; i++) {
                        Circle circle = new Circle(10, Color.web("white", 0.2));
                        circle.setTranslateX(random.nextDouble() * 1200);
                        circle.setTranslateY(random.nextDouble() * 1000);
                        circles.getChildren().add(circle);

                        timeline.getKeyFrames().addAll(
                                        new KeyFrame(Duration.ZERO,
                                                        new KeyValue(circle.translateXProperty(),
                                                                        circle.getTranslateX()),
                                                        new KeyValue(circle.translateYProperty(),
                                                                        circle.getTranslateY())),
                                        new KeyFrame(new Duration(40000),
                                                        new KeyValue(circle.translateXProperty(),
                                                                        random.nextDouble() * 800),
                                                        new KeyValue(circle.translateYProperty(),
                                                                        random.nextDouble() * 600)));
                }

                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

                pane.getChildren().add(circles);

                // Create a gradient rectangle
                Rectangle colors = new Rectangle(pane.getWidth(), pane.getHeight(),
                                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[] {
                                                new Stop(0, Color.web("#f8bd55")),
                                                new Stop(0.14, Color.web("#c0fe56")),
                                                new Stop(0.28, Color.web("#5dfbc1")),
                                                new Stop(0.43, Color.web("#64c2f8")),
                                                new Stop(0.57, Color.web("#be4af7")),
                                                new Stop(0.71, Color.web("#ed5fc2")),
                                                new Stop(0.85, Color.web("#ef504c")),
                                                new Stop(1, Color.web("#f2660f"))
                                }));
                colors.widthProperty().bind(pane.widthProperty());
                colors.heightProperty().bind(pane.heightProperty());

                Group blendModeGroup = new Group(
                                new Group(new Rectangle(pane.getWidth(), pane.getHeight(), Color.BLACK), circles),
                                colors);
                colors.setBlendMode(BlendMode.OVERLAY);
                pane.getChildren().add(blendModeGroup);
        }

        // public static void main(String[] args) {
        //         launch(args);
        // }
}