package Client;

import com.google.gson.Gson;
import javafx.animation.Animation;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Random;

public class ClientGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static final int WIDTH = 1000, HEIGHT = 600;

    static Stage primaryStage;
    static Group loginGroup = new Group();
    static Scene loginScene = new Scene(loginGroup, WIDTH, HEIGHT);
    static Group mainMenuGroup = new Group();
    static Scene mainMenuScene = new Scene(mainMenuGroup, WIDTH - 300, HEIGHT - 100);

    public static Socket socket;
    public static Connection connection;

    public static void raining(Circle circle) {
        Random random = new Random();
        circle.setCenterX(random.nextInt(WIDTH));
        int time = 10 + random.nextInt(50);
        Animation fall = TranslateTransitionBuilder.create()
                .node(circle)
                .fromY(-200)
                .toY(HEIGHT + 200)
                .toX(random.nextDouble() * circle.getCenterX())
                .duration(Duration.seconds(time))
                .onFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        raining(circle);
                    }
                }).build();
        fall.play();
    }

    public static void snowMaker(Group group) {
        Random random = new Random();
        Circle c[] = new Circle[2000];
        for (int i = 0; i < 2000; i++) {
            c[i] = new Circle(1, 1, 2);
            c[i].setRadius(random.nextDouble() * 3);
            Color color = Color.rgb(255, 255, 255, random.nextDouble());
            c[i].setFill(color);
            group.getChildren().add(c[i]);
            ClientGame.raining(c[i]);
        }
    }

    public static void wrongOpponent() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR, "User not available to play with ...");
                alert.show();
            }
        });
    }

    public static void connectionError() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Connection to the server closed ...");
                alert.show();
            }
        });
    }

    public static void makeMainMenu(Account account) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ImageView armImage = new ImageView(new Image("Client/126-1261070_toe-game-in-c-tic-tac-toe-game.png.png"));
                armImage.setY(20);
                armImage.setFitWidth(200);
                armImage.setFitHeight(200);
                armImage.setX(250);
                mainMenuGroup.getChildren().add(new ImageView(new Image("Client/119.jpg")));
                ClientGame.snowMaker(mainMenuGroup);
                mainMenuGroup.getChildren().add(armImage);

                mainMenuScene.getStylesheets().add("Client/style.css");
                Button logOutBtn = new Button("LOG OUT");
                TextField name = new TextField();
                TextField size = new TextField();
                name.setPromptText("Enter a UserName...");
                size.setPromptText("Enter Size ...");
                Button selectUser = new Button("START XoXoXoX");
                buttonSetter(logOutBtn, (WIDTH - 300) / 2 - 100, (HEIGHT - 100) * 4 / 5);
                buttonSetter(selectUser, (WIDTH - 300) / 2 - 100, (HEIGHT - 100) * 2 / 3);
                name.setLayoutX((WIDTH - 300) / 2 - 100);
                name.setLayoutY((HEIGHT - 100) * 2 / 3 - 100);
                name.setFont(Font.loadFont(ClassLoader.getSystemResource("Client/Crushed-Regular.ttf").toExternalForm(), 25));
                name.getStyleClass().add("custom-text-field");
                size.setLayoutX((WIDTH - 300) / 2 - 100);
                size.setLayoutY((HEIGHT - 100) * 2 / 3 - 50);
                size.setFont(Font.loadFont(ClassLoader.getSystemResource("Client/Crushed-Regular.ttf").toExternalForm(), 25));
                size.getStyleClass().add("custom-text-field");
                name.setMinHeight(41);
                name.setMaxHeight(40);
                name.setMaxWidth(200);
                name.setMinWidth(200);
                size.setMinHeight(41);
                size.setMaxHeight(40);
                size.setMaxWidth(200);
                size.setMinWidth(200);
                size.setFocusTraversable(false);
                name.setFocusTraversable(false);
                mainMenuGroup.getChildren().addAll(logOutBtn, selectUser, name);
                primaryStage.setScene(mainMenuScene);

                logOutBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ClientGame.loginSceneManage();
                    }
                });
                selectUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (name.getText().equals("") || name.getText().equals(account.getUserName())) {
                            wrongOpponent();
                        } else {
                            Gson gson = new Gson();
                            int mapSize = 3;
                            if (!size.getText().equals("") && size.getText().matches("[3-9]")) {
                                mapSize = Integer.getInteger(size.getText());
                                System.out.println(mapSize);
                            }
                            String string = gson.toJson(new GamePacket(new Game(mapSize, account, new Account(name.getText(), "123")), true));
                            connection.sendPacket(string);
                        }
                    }
                });
            }
        });
    }

    public static void wrongSingUp() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR, "User Name already used...");
                alert.show();
            }
        });
    }

    public static void wrongLogIn() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR, "User Name or Password is Wrong ...");
                alert.show();
            }
        });
    }

    public static void primaryStageManage(Stage primaryStage) {
        ClientGame.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("XoXoXoX");
        primaryStage.getIcons().add(new Image("Client/iconfinder_Tic-Tac-Toe-Game-red_190320.png"));//todo icon download
    }

    public static void loginSceneManage() {

        loginGroup.getChildren().add(new ImageView(new Image("Client/119.jpg")));
        Button loginBtn = new Button("LOGIN");
        Button signUpBtn = new Button("SIGN UP");
        TextField name = new TextField();
        PasswordField passwordField = new PasswordField();
        ClientGame.snowMaker(loginGroup);
        loginScene.getStylesheets().add("Client/style.css");

        buttonSetter(loginBtn, WIDTH / 2 - 250, HEIGHT * 2 / 3);
        buttonSetter(signUpBtn, WIDTH / 2 + 80, HEIGHT * 2 / 3);

        textFieldSetter(name, false);
        textFieldSetter(passwordField, true);

        loginGroup.getChildren().addAll(name, passwordField, loginBtn, signUpBtn);

        primaryStage.setScene(loginScene);
        loginBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (name.getText().equals("") || passwordField.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User Name or Password is empty ...");
                    alert.show();
                } else {
                    Account account = new Account(name.getText(), passwordField.getText());
                    Gson gson = new Gson();
                    String string = gson.toJson(new AccountPacket(account, true));
                    connection.sendPacket(string);
                }
            }
        });
        signUpBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (name.getText().equals("") || passwordField.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User Name or Password is empty ...");
                    alert.show();
                } else {
                    Account account = new Account(name.getText(), passwordField.getText());
                    Gson gson = new Gson();
                    String string = gson.toJson(new AccountPacket(account, false));
                    connection.sendPacket(string);
                }
            }
        });
    }

    public static void textFieldSetter(TextInputControl name, boolean pass) {
        if (pass) {
            name.setLayoutY(HEIGHT * 2 / 3 - 100);
            name.setPromptText("Pass Word ...");
        } else {
            name.setPromptText("User Name ...");
            name.setLayoutY(HEIGHT * 2 / 3 - 200);
        }
        name.setFont(Font.font(20));
        name.setLayoutX(WIDTH / 2 - 100);

        name.getStyleClass().add("custom-text-field");
        name.setMinHeight(40);
        name.setMaxHeight(40);
        name.setMaxWidth(200);
        name.setMinWidth(200);
        name.setFocusTraversable(false);
    }

    public static void buttonSetter(Button button, int x, int y) {
        button.relocate(x, y);
        button.setMinSize(180, 50);
        button.setMaxSize(180, 50);
        button.getStyleClass().add("button");
        button.setFont(Font.loadFont(ClassLoader.getSystemResource("Client/Crushed-Regular.ttf").toExternalForm(), 25));

    }


    @Override
    public void start(Stage primaryStage) {
        while (true) {
            try {
                socket = new Socket("localhost", 5555);
                connection = new Connection(socket);
                System.out.println("connection client ..");
                break;
            } catch (ConnectException e) {
                // ClientGame.connectionError();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        primaryStageManage(primaryStage);
        loginSceneManage();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    connection.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.show();
    }
}
