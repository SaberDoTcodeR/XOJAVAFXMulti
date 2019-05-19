package Client;

import com.google.gson.Gson;
import javafx.animation.Animation;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
import java.util.concurrent.ConcurrentLinkedDeque;

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
    static Group gameGroup = new Group();
    static Scene gameScene = new Scene(gameGroup, WIDTH, HEIGHT);
    private static GraphicsContext mapGraphic;
    public static Socket socket;
    public static Connection connection;
    public static TextArea messages = new TextArea();

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

    public static void whoWinError(Game game) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (game.getFinished() == 3) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Drawwwwwww ...");
                    alert.show();
                } else if (game.getFinished() != game.getWhoseTurn() && !game.isMyTurn()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You Win the Battle ...");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You loose the Battle  ...");
                    alert.show();
                }
            }
        });
    }

    public static void printOppMessage(String message) {
        messages.appendText(message + "\n");
    }

    public static String printMessage(String message) {
        String messagePrint = Account.getCurrentAccount().getUserName() + " : ";
        messagePrint += message;

        messages.appendText(messagePrint + "\n");
        return messagePrint;
    }

    public static void makeGame(Game game) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameGroup = new Group();
                gameScene = new Scene(gameGroup, WIDTH, HEIGHT);
                gameGroup.getChildren().add(new ImageView(new Image("Client/119.jpg")));

                messages.setFont(Font.loadFont(ClassLoader.getSystemResource("Client/Crushed-Regular.ttf").toExternalForm(), 25));

                Button quitBattle = new Button("Quit Battle");
                buttonSetter(quitBattle, WIDTH - 300, HEIGHT - 100);
                TextField input = new TextField();
                input.setPromptText("Enter a message here ...");
                input.setFont(Font.loadFont(ClassLoader.getSystemResource("Client/Crushed-Regular.ttf").toExternalForm(), 25));
                input.setPrefHeight(40);
                input.setOnAction(event -> {
                    try {
                        connection.sendPacket("MSG" + printMessage(input.getText()));
                        input.clear();
                    } catch (Exception e) {
                        messages.appendText("Failed to send\n");
                    }
                });

                VBox root = new VBox(10, messages, input);
                root.setLayoutX(650);
                root.setLayoutY(40);
                root.setPrefSize(300, 400);
                gameGroup.getChildren().addAll(quitBattle, root);
                quitBattle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        connection.sendPacket("quit battle");
                        Account.getCurrentAccount().setGame(null);
                        ClientGame.makeMainMenu(Account.getCurrentAccount());
                    }
                });
                Canvas board;
                board = new Canvas(62 * game.getSize(), 62 * game.getSize());

                board.setOnMousePressed(e -> game.doMouseClick(e.getX(), e.getY()));
                Label message;
                if (game.isMyTurn()) {
                    message = new Label("Your turn ...");
                } else {
                    message = new Label("opponent turn ...");
                }
                if (game.getWhoseTurn() == 1) {
                    message.setTextFill(Color.RED);
                } else
                    message.setTextFill(Color.BLUE);
                message.setFont(Font.loadFont(ClassLoader.getSystemResource("Client/Crushed-Regular.ttf").toExternalForm(), 25));

                BorderPane.setAlignment(message, Pos.CENTER);
                BorderPane content = new BorderPane(board);
                content.setTop(message);
                content.setLayoutX(30);


                GraphicsContext g = board.getGraphicsContext2D();
                g.setFill(Color.BURLYWOOD);
                g.fillRect(0, 0, board.getWidth(), board.getHeight());
                g.setStroke(Color.BLACK);
                g.setLineWidth(6);
                g.strokeRect(0, 0, board.getWidth(), board.getHeight());
                g.setFill(Color.BLACK);
                g.setLineWidth(10);
                g.setStroke(Color.BLACK);
                for (int i = 0; i < game.getSize() - 1; i++) {
                    g.strokeLine(62 + 62 * i, 0, 62 + 62 * i, 62 * game.getSize());
                    g.strokeLine(0, 62 + 62 * i, 62 * game.getSize(), 62 + 62 * i);
                }
                mapGraphic = g;
                updateMap(game);
                gameGroup.getChildren().add(content);
                primaryStage.setScene(gameScene);
            }
        });
    }

    public static void updateMap(Game game) {
        for (int row = 0; row < game.getSize(); row++) {
            for (int col = 0; col < game.getSize(); col++) {
                if (game.getMap()[row][col] == 1) {
                    mapGraphic.setStroke(Color.RED);
                    mapGraphic.strokeLine(13 + col * 62, 13 + row * 62, 50 + col * 62, 50 + row * 62);
                    mapGraphic.strokeLine(13 + col * 62, 50 + row * 62, 50 + col * 62, 13 + row * 62);
                } else if (game.getMap()[row][col] == 2) {
                    mapGraphic.setStroke(Color.BLUE);
                    mapGraphic.strokeOval(10.5 + col * 62, 10.5 + row * 62, 40, 40);
                }
            }
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
                mainMenuGroup.getChildren().addAll(logOutBtn, selectUser, name, size);
                primaryStage.setScene(mainMenuScene);

                logOutBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        ClientGame.loginSceneManage(false);
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
                                mapSize = Integer.parseInt(size.getText());
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

    public static void loginSceneManage(boolean out) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
                if (out)
                    primaryStage.show();
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
                socket = new Socket("194.225.47.71", 5555);
                connection = new Connection(socket);
                System.out.println("connection client ..");
                break;
            } catch (ConnectException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        primaryStageManage(primaryStage);

        loginSceneManage(false);
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
