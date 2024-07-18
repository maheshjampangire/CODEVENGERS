package com.codevengers.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

import java.util.Random;

public class Landing_Page extends Application {

        @Override
        public void start(Stage primaryStage) {
                // Create a StackPane to hold the background image and other elements
                StackPane stackPane = new StackPane();

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

                // Create a VBox to hold the text, image, and button, centered
                VBox vbox = new VBox(20);
                vbox.setAlignment(Pos.CENTER);
                vbox.setPadding(new Insets(50, 50, 20, 50)); // Adjusted padding around the VBox

                // Create and style the text
                Text welcomeText = new Text("Welcome to Routiner!");
                welcomeText.setFont(new Font("Arial", 40));
                welcomeText.setFill(Color.WHITE);
                welcomeText.setTranslateY(-40); // Move the text up by 20 units

                // Add the slogan text with updated font
                Text sloganText1 = new Text("Explore the app, ");
                sloganText1.setFont(Font.font("Italic", FontWeight.BOLD, 40)); // Change the font
                sloganText1.setFill(Color.LAVENDER);
                sloganText1.setTranslateY(-10);

                // Add the slogan text with updated font
                Text sloganText = new Text("find some peace of mind to achieve good habits");
                sloganText.setFont(Font.font("Verdana", 24)); // Change the font
                sloganText.setFill(Color.WHITE);
                sloganText.setTranslateY(-10);

                // Load the image with adjusted opacity
                Image image = new Image("/assets/img.jpeg");
                ImageView imageView = new ImageView(image);
                double imageSize = 300; // Adjust the size of the circular image
                imageView.setFitWidth(imageSize); // Set the width of the ImageView
                imageView.setFitHeight(imageSize); // Set the height of the ImageView
                imageView.setPreserveRatio(false); // Disable preserve ratio to allow circular clipping

                // Create a circular clip for the ImageView
                Circle clip = new Circle(imageSize / 2); // Radius is half of the image size
                clip.setCenterX(imageSize / 2); // Center X coordinate of the circle clip
                clip.setCenterY(imageSize / 2); // Center Y coordinate of the circle clip
                imageView.setClip(clip);

                // Create the "Get Started" button
                Button getStartedButton = new Button("Get Started");
                getStartedButton.setStyle(
                                "-fx-font-size: 20px; -fx-padding: 10 35; -fx-background-radius: 30; -fx-background-color: #FFFFFF; -fx-text-fill: #8a2be2;");

                getStartedButton.setTranslateY(40);

                // Add drop shadow effect to the button
                DropShadow shadow = new DropShadow();
                shadow.setColor(Color.GRAY);
                shadow.setOffsetX(2.0);
                shadow.setOffsetY(2.0);
                getStartedButton.setEffect(shadow);

                // Set action on button click
                getStartedButton.setOnAction(event -> {
                        LoginController loginController = new LoginController(primaryStage);
                        loginController.showLoginScene();
                });

                // Add text, slogan, image, and button to the VBox
                vbox.getChildren().addAll(welcomeText, sloganText1, sloganText, imageView, getStartedButton);

                // Add the VBox to the StackPane
                stackPane.getChildren().add(vbox);

                // Create the Scene
                Scene scene = new Scene(stackPane, 800, 600);

                // Set up the Stage
                primaryStage.setTitle("Welcome to Routiner");
                primaryStage.setScene(scene);
                primaryStage.show();
        }

        private void addAnimatedCircles(Pane pane, int numCircles) {
                Random random = new Random();
                Timeline timeline = new Timeline();
                Group circles = new Group();

                for (int i = 0; i < 20; i++) {
                        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(80, Color.web("white", 0.2));
                        circle.setTranslateX(random.nextDouble() * 1200);
                        circle.setTranslateY(random.nextDouble() * 1000);
                        circle.setStrokeWidth(4);
                        circles.getChildren().add(circle);

                        timeline.getKeyFrames().addAll(
                                        new KeyFrame(Duration.ZERO,
                                                        new KeyValue(circle.translateXProperty(),
                                                                        circle.getTranslateX()),
                                                        new KeyValue(circle.translateYProperty(),
                                                                        circle.getTranslateY())),
                                        new KeyFrame(new Duration(10000),
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
                colors.setBlendMode(javafx.scene.effect.BlendMode.OVERLAY);
                pane.getChildren().add(blendModeGroup);
        }

        // public static void main(String[] args) {
        // launch(args);
        // }
}