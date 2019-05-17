package Client;

import Common.Account;
import Common.AccountPacket;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Group;

import java.io.IOException;
import java.net.Socket;


public class ClientGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Socket socket;
    private Connection connection;
    public static final int WIDTH = 1000, HEIGHT = 600;

    public static void primaryStageManage(Stage primaryStage) {
        ClientGame.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("XoXoXoX");
        primaryStage.getIcons().add(new Image("Client/icon.png"));//todo icon download
    }

    public void loginSceneManage(Group loginGroup, Scene loginScene) {
        loginGroup.getChildren().add(new ImageView(new Image("Client/119.jpg")));
        Button loginBtn = new Button("LOGIN");
        Button signInBtn = new Button("SIGN IN");
        TextField name = new TextField();
        PasswordField passwordField = new PasswordField();

        loginScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        buttonSetter(loginBtn, true);
        buttonSetter(signInBtn, false);

        signInBtn.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
        loginBtn.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
        name.setFont(Font.font(20));
        passwordField.setFont(Font.font(20));

        textFieldSetter(name, false);
        textFieldSetter(passwordField, true);

        loginGroup.getChildren().addAll(name, passwordField, loginBtn, signInBtn);

        primaryStage.setScene(loginScene);
        loginBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (name.getText().equals("") || passwordField.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User Name or Password is empty ...");
                    alert.show();
                } else {
                    Account account = new Account(name.getText(), passwordField.getText());
                    connection.sendPacket(new AccountPacket(account, true));
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
        name.setLayoutX(WIDTH / 2 - 100);
        name.getStyleClass().add("custom-text-field");
        name.setMinHeight(40);
        name.setMaxHeight(40);
        name.setMaxWidth(200);
        name.setMinWidth(200);
        name.setFocusTraversable(false);
    }

    public static void buttonSetter(Button button, boolean login) {
        if (login)
            button.relocate(WIDTH / 2 - 250, HEIGHT * 2 / 3);
        else
            button.relocate(WIDTH / 2 + 80, HEIGHT * 2 / 3);
        button.setMinSize(180, 50);
        button.setMaxSize(180, 50);
        button.getStyleClass().add("button");
    }

    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            socket = new Socket("localhost", 5555);
            connection = new Connection(socket);
            System.out.println("connection client ..");
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStageManage(primaryStage);
        Group login = new Group();
        Scene loginScene = new Scene(login, WIDTH, HEIGHT);
        this.loginSceneManage(login, loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
