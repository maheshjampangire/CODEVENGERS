package com.codevengers.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.codevengers.firebase_config.DataService;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignupController {

    private LoginController loginController;

    public SignupController(LoginController loginController) {
        this.loginController = loginController;
    }

    public Scene createSignupScene(Stage primaryStage) {
        Label registerLabel = new Label("Register");
        registerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField firstNameTextField = new TextField();
        firstNameTextField.setPromptText("Enter your first name");
        firstNameTextField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");

        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField lastNameTextField = new TextField();
        lastNameTextField.setPromptText("Enter your last name");
        lastNameTextField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField emailTextField = new TextField();
        emailTextField.setPromptText("Enter your email address");
        emailTextField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");

        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");

        Label confirmPassLabel = new Label("Confirm Password:");
        confirmPassLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm your password");
        confirmPassField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");

        Label ageLabel = new Label("Age:");
        ageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField ageTextField = new TextField();
        ageTextField.setPromptText("Enter your age");
        ageTextField.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");

        Label genderLabel = new Label("Gender:");
        genderLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        RadioButton maleRadioButton = new RadioButton("Male");
        maleRadioButton.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white;");
        RadioButton femaleRadioButton = new RadioButton("Female");
        femaleRadioButton.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white;");

        ToggleGroup genderGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderGroup);
        femaleRadioButton.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(10, maleRadioButton, femaleRadioButton);
        genderBox.setAlignment(Pos.CENTER);

        CheckBox acceptCheckbox = new CheckBox("I accept the terms and conditions");
        acceptCheckbox.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Button regiButton = new Button("Register Now");
        regiButton.setStyle(
                "-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 30; -fx-background-color: #4CAF50;");

        // Create Back Button
        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 30; -fx-background-color: #F44336;");

        // Back button action
        backButton.setOnAction(event -> {
            loginController.showLoginScene();
        });

        // HBox for First Name and Last Name fields side by side
        HBox nameBox = new HBox(10, firstNameLabel, firstNameTextField, lastNameLabel, lastNameTextField);
        nameBox.setAlignment(Pos.CENTER);

        VBox emailBox = new VBox(10, emailLabel, emailTextField);
        emailBox.setMaxSize(300, 30);

        VBox passBox = new VBox(10, passLabel, passField);
        passBox.setMaxSize(300, 30);

        VBox confirmPassBox = new VBox(10, confirmPassLabel, confirmPassField);
        confirmPassBox.setMaxSize(300, 30);

        VBox ageBox = new VBox(10, ageLabel, ageTextField);
        ageBox.setMaxSize(300, 30);

        VBox genderBoxVBox = new VBox(10, genderLabel, genderBox);
        genderBoxVBox.setMaxSize(300, 30);

        HBox checkboxBox = new HBox(10, acceptCheckbox);
        checkboxBox.setAlignment(Pos.CENTER);

        // HBox for buttons
        HBox buttonBox = new HBox(10, backButton, regiButton);
        buttonBox.setMaxSize(350, 30);
        buttonBox.setAlignment(Pos.CENTER);

        regiButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (acceptCheckbox.isSelected()) {
                    handleSignup(primaryStage, firstNameTextField.getText(), lastNameTextField.getText(),
                            emailTextField.getText(), passField.getText(), confirmPassField.getText(),
                            maleRadioButton.isSelected() ? "Male" : femaleRadioButton.isSelected() ? "Female" : "",
                            ageTextField.getText());
                } else {
                    System.out.println("Please accept the terms and conditions");
                }
            }
        });

        VBox formBox = new VBox(20, registerLabel, nameBox, emailBox, passBox, confirmPassBox, ageBox, genderBoxVBox,
                checkboxBox, buttonBox);
        formBox.setAlignment(Pos.CENTER);

        // Load the image
        Image image = new Image("assets//signup.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(600);

        // Make image circular
        Circle clip = new Circle(250, 300, 250); // centerX, centerY, radius
        imageView.setClip(clip);

        // StackPane for holding the form and image side by side
        HBox contentBox = new HBox(20, imageView, formBox);
        contentBox.setAlignment(Pos.CENTER);

        // Create a StackPane for the animated background
        StackPane backgroundPane = new StackPane();
        backgroundPane.setStyle("-fx-background-color: LIGHTGREY;");

        Scene scene = new Scene(backgroundPane, 900, 600); // Adjusted width to fit image and form

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
        backgroundPane.getChildren().add(contentBox);

        return scene;
    }

    private void handleSignup(Stage primaryStage, String firstName, String lastName, String email, String password,
            String confirmPassword, String gender, String age) {
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match");
            return;
        }

        if (gender.isEmpty()) {
            System.out.println("Please select your gender");
            return;
        }

        try {
            int ageInt = Integer.parseInt(age);
            if (ageInt <= 0) {
                System.out.println("Please enter a valid age");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid age");
            return;
        }

        DataService dataService;
        try {
            dataService = new DataService();
            Map<String, Object> data = new HashMap<>();
            data.put("firstName", firstName);
            data.put("lastName", lastName);
            data.put("email", email);
            data.put("password", password);
            data.put("age", age);
            data.put("gender", gender);
            dataService.addData("users", email, data);
            System.out.println("User registered successfully");

            // Return to login scene after successful signup
            loginController.showLoginScene();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}