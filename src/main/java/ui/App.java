package ui;

import database.Database;
import exception.BadCredentialsException;
import exception.FieldsNotFilledException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.User;

public class App extends Application {
    // db gets instantiated once in app and passed through every class to save its changes until application is closed
    // the App itself gets passed so that if you logout the details get saved
    // Note: it's in every page because it is possible to logout from each one of them
    private Stage window;
    private final Database db = new Database();

    Label userLabel;
    Label passwordLabel;
    Label errorLabel;
    TextField userInput;
    PasswordField passwordField;
    Button loginButton;


    @Override
    public void start(Stage window) throws Exception {
        loginContent();
    }

    public void loginContent() {
        window = new Stage();
        window.centerOnScreen();
        window.setResizable(false);
        window.setHeight(200);
        window.setWidth(350);
        window.setTitle("Cinema - Login");

        // create components
        userLabel = new Label("Username");
        passwordLabel = new Label("Password");
        errorLabel = new Label("        ");
        userInput = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Log in");

        //add attributes
        userInput.setPromptText("Enter your username...");
        passwordField.setPromptText("Enter your password...");

        //set grid
        GridPane gridPane = new GridPane();

        loginGrid(gridPane);
        loginOnAction();

        errorLabel.setStyle("-fx-text-fill: #ff0000");
        JMetro jMetro = new JMetro(gridPane, Style.LIGHT);

        Scene scene = new Scene(gridPane);
        window.setScene(scene);

        scene.getStylesheets().add("style.css");

        window.show();

        }

        private void loginGrid(GridPane gridPane) {
            gridPane.setPadding(new Insets(10));
            gridPane.setHgap(8);
            gridPane.setVgap(10);

            // add components to the grid
            gridPane.add(errorLabel, 1, 3);
            gridPane.add(userLabel, 0, 0);
            gridPane.add(userInput, 1, 0);
            gridPane.add(passwordLabel, 0, 1);
            gridPane.add(passwordField, 1, 1);
            gridPane.add(loginButton, 1, 2);
        }


    private void loginOnAction() {
        loginButton.setOnAction(actionEvent -> {

            if (!passwordField.getText().isEmpty() && !userInput.getText().isEmpty()) {
                User user = db.validationUser(userInput.getText(), passwordField.getText());
                if (user != null) {
                    new SellingTickets(this, db, user);
                    window.close();
                } else {
                    try {
                        throw new BadCredentialsException();
                    } catch (BadCredentialsException bce) {
                        errorLabel.setText(bce.getMessage());
                    }
                    userInput.clear();
                    passwordField.clear();
                }
            } else {
                try {
                    throw new FieldsNotFilledException();
                } catch (FieldsNotFilledException fnfe) {
                    errorLabel.setText(fnfe.getMessage());
                }
            }
        });
    }
}





