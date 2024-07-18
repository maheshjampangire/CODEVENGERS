package com.codevengers.Controller;

import java.util.Random;
//import java.util.concurrent.ExecutionException;

import com.codevengers.dashboards.UserPage;
import com.codevengers.firebase_config.DataService;
import com.google.firebase.auth.FirebaseAuthException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
    private Stage primaryStage;
    private Scene loginScene;
    private Scene userScene;
    private DataService dataService;

    public static String key;

    public LoginController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        dataService = new DataService();
        initScenes();
    }

    private void initScenes() {
        initLoginScene();
    }

    private void initLoginScene() {
        // Create a StackPane for layering
        StackPane stackPane = new StackPane();

        // Create a Pane for the animated circles
        Pane circlesPane = new Pane();

        // Add animated circles to the pane
        addAnimatedCircles(circlesPane, 30); // Add 30 animated circles

        // Create the login form
        Label welcomeLabel = new Label("Welcome back!");
        welcomeLabel.setStyle("-fx-font-size: 24pt; -fx-text-fill: white; -fx-font-weight: bold;");

        Label userLabel = new Label("Username");
        TextField userTextField = new TextField();
        Label passLabel = new Label("Password");
        PasswordField passField = new PasswordField();

        Text forgotPasswordLink = new Text("Forgot Password?");
        forgotPasswordLink.setStyle("-fx-font-size: 12pt; -fx-underline: true; -fx-text-fill: white; cursor: hand;");
        forgotPasswordLink.setOnMouseClicked(event -> {
            // Handle forgot password action here
            System.out.println("Forgot Password clicked!");
        });

        Button loginButton = new Button("Login");

        Text signupLink = new Text("Don't have an account? Sign up");
        signupLink.setStyle("-fx-font-size: 12pt; -fx-underline: true; -fx-text-fill: white; cursor: hand;");
        signupLink.setOnMouseClicked(event -> {
            showSignupScene();
            userTextField.clear();
            passField.clear();
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleLogin(userTextField.getText(), passField.getText());
                userTextField.clear();
                passField.clear();
            }
        });

        userLabel.setStyle("-fx-text-fill: white;");
        passLabel.setStyle("-fx-text-fill: white;");

        VBox fieldBox1 = new VBox(10, userLabel, userTextField);
        fieldBox1.setMaxSize(300, 30);
        VBox fieldBox2 = new VBox(10, passLabel, passField);
        fieldBox2.setMaxSize(300, 30);
        HBox forgotPasswordBox = new HBox();
        forgotPasswordBox.getChildren().addAll(forgotPasswordLink);
        forgotPasswordBox.setAlignment(Pos.CENTER);

        userTextField.setStyle("-fx-pref-width: 350px;");
        passField.setStyle("-fx-pref-width: 350px;");

        VBox vbox = new VBox(20, welcomeLabel, fieldBox1, fieldBox2, forgotPasswordBox, loginButton, signupLink);
        vbox.setAlignment(Pos.CENTER);

        // Load the image
        Image image = new Image("/assets/login.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);

        // Make the image circular
        Circle clip = new Circle(250, 250, 250);
        imageView.setClip(clip);

        // HBox for the image and form side by side
        HBox contentBox = new HBox(60, imageView, vbox);
        contentBox.setAlignment(Pos.CENTER);

        // Add the HBox to the StackPane
        stackPane.getChildren().addAll(circlesPane, contentBox);
        stackPane.setAlignment(Pos.CENTER); // Center the VBox in the StackPane

        // Create a gradient background
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#1e3c72")), // Start color
                new Stop(1, Color.web("#2a5298")) // End color
        };
        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        stackPane.setStyle("-fx-background-color: linear-gradient(#1e3c72, #2a5298);");

        // Create a Scene with the StackPane as the root
        loginScene = new Scene(stackPane, 900, 600); // Adjusted width to fit image and form
    }

    private void addAnimatedCircles(Pane pane, int numCircles) {
        Random random = new Random();
        HBox circles = new HBox();
        for (int i = 0; i < 20; i++) {
            Circle circle = new Circle(150, Color.web("white", 0.05));
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }

        // Animate circles
        Timeline timeline = new Timeline();
        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, // set start position at 0
                            new KeyValue(circle.translateXProperty(), circle.getTranslateX()),
                            new KeyValue(circle.translateYProperty(), circle.getTranslateY())),
                    new KeyFrame(new Duration(20000), // set end position at 20s
                            new KeyValue(circle.translateXProperty(), random.nextDouble() * 800),
                            new KeyValue(circle.translateYProperty(), random.nextDouble() * 600)));
        }
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Add the content box to the root StackPane
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
                new Group(new Rectangle(pane.getWidth(), pane.getHeight(), Color.BLACK), circles), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        pane.getChildren().add(blendModeGroup);
    }

    private void initUserScene() {
        UserPage userPage = new UserPage(dataService);
        userScene = new Scene(userPage.createUserScene(this::handleLogout, key), 600, 600);
    }

    public Scene getLoginScene() {
        return loginScene;
    }

    public void showLoginScene() {
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login To Routiner");
        primaryStage.show();
    }

    private void handleLogin(String username, String password) {
        try {
            if (dataService.authenticateUser(username, password)) {
                key = username;
                initUserScene();
                primaryStage.setScene(userScene);
                primaryStage.setTitle("User Dashboard");
            } else {
                System.out.println("Invalid credentials");
                key = null;
            }
        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
        }
    }

    private void showSignupScene() {
        SignupController signupController = new SignupController(this);
        Scene signupScene = signupController.createSignupScene(primaryStage);

        primaryStage.setScene(signupScene);
        primaryStage.setTitle("Signup");
        primaryStage.show();
    }

    private void handleLogout() {
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
    }
}