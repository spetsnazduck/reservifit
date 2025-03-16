package components;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;

public class LoginScreen {
    private Scene scene;


    public LoginScreen(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #1e1e1e; -fx-alignment: center;");

        Label titleLabel = new Label("ReserviFit");
        titleLabel.setStyle(
                "-fx-font-size: 32px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-effect: dropshadow(gaussian, rgba(78, 54, 186, 0.8), 15, 0.5, 0, 0);"
        );

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleTextField(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleTextField(passwordField);

        Button loginButton = new Button("Login");
        styleButton(loginButton);
        loginButton.setOnAction(e -> {
            if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                System.out.println("Login attempt for user: " + usernameField.getText());
                boolean isAuthenticated = database.Backend.validateUser(usernameField.getText(), passwordField.getText());
                boolean isBiz = database.Backend.isBiz();
                String bizName = isBiz ? database.Backend.fetchBizName() : "none";

                if (isAuthenticated) {
                    System.out.println("Login successful for user: " + usernameField.getText());
                    if (isBiz) {
                        primaryStage.setScene(new BizDashboard(primaryStage, bizName).getScene());
                    } else {
                        primaryStage.setScene(new MainDashboard(primaryStage).getScene());
                    }
                } else {
                    System.out.println("Login failed for user: " + usernameField.getText());
                    showAlert("Login Failed", "Napačno geslo ali email. Če še nimaš računa se prijavi!");
                }
            } else {
                System.out.println("Izpolni oba polja!");
                showAlert("Login Failed", "Pustil si prazno polje!");
            }
        });

        Button signupButton = new Button("Sign Up");
        styleButton(signupButton);
        signupButton.setOnMouseClicked(e -> openSignUp());

        HBox actionButtons = new HBox(15, loginButton, signupButton);
        actionButtons.setPadding(new Insets(20));
        actionButtons.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(titleLabel, usernameField, passwordField, actionButtons);
        scene = new Scene(layout, 400, 500);
    }

    private void styleTextField(TextField textField) {
        textField.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-background-color: #2c2c2c; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-color: transparent;"
        );
        textField.setOnMouseEntered(e -> textField.setStyle(
                "-fx-font-size: 18px; -fx-text-fill: white; -fx-background-color: #383838; -fx-border-color: #00ffff; -fx-border-width: 1px;"
        ));
        textField.setOnMouseExited(e -> textField.setStyle(
                "-fx-font-size: 18px; -fx-text-fill: white; -fx-background-color: #2c2c2c; -fx-border-color: transparent;"
        ));
        textField.setOnMouseClicked(e -> textField.setStyle(
                "-fx-font-size: 18px; -fx-text-fill: white; -fx-background-color: #383838; -fx-border-color: #00ffff; -fx-border-width: 1px;"
        ));
    }

    private void styleButton(Button button) {
        button.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-background-color: linear-gradient(to right, #00c6ff, #4e36ba); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: transparent;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-font-size: 16px; -fx-background-color: linear-gradient(to right, #0072ff, #4e36ba); " +
                        "-fx-border-color: #00ffff; -fx-border-width: 1px;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-font-size: 16px; -fx-background-color: linear-gradient(to right, #00c6ff, #4e36ba); " +
                        "-fx-border-color: transparent;"
        ));
    }

    public Scene getScene() {
        return scene;
    }

    private void openSignUp() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registracija");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        Label titleLabel = new Label("ReserviFit");
        titleLabel.getStyleClass().add("title-label");

        TextField imeField = new TextField();
        imeField.setPromptText("Ime");
        imeField.getStyleClass().add("custom-field");

        TextField priimekField = new TextField();
        priimekField.setPromptText("Priimek");
        priimekField.getStyleClass().add("custom-field");

        TextField emailField = new TextField();
        emailField.setPromptText("E-pošta");
        emailField.getStyleClass().add("custom-field");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Geslo");
        passField.getStyleClass().add("custom-field");

        TextField opisField = new TextField();
        opisField.setPromptText("Opis");
        opisField.getStyleClass().add("custom-field");

        DatePicker letoRojPicker = new DatePicker();
        letoRojPicker.setPromptText("Leto rojstva");
        letoRojPicker.getStyleClass().add("custom-field");

        ComboBox<String> krajComboBox = new ComboBox<>();
        krajComboBox.setPromptText("Izberite kraj");
        krajComboBox.getStyleClass().add("custom-field");

        database.Backend.fetchKraji(krajComboBox);

        VBox layout = new VBox(10, titleLabel, imeField, priimekField, emailField, passField, opisField, letoRojPicker, krajComboBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("dialog-content");

        dialog.getDialogPane().setContent(layout);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String ime = imeField.getText();
                String priimek = priimekField.getText();
                String email = emailField.getText();
                String pass = passField.getText();
                String opis = opisField.getText();
                String letoRojstva = (letoRojPicker.getValue() != null) ? letoRojPicker.getValue().toString() : "";
                String kraj = krajComboBox.getValue();

                if (ime.isEmpty() || priimek.isEmpty() || email.isEmpty() || pass.isEmpty() || letoRojstva.isEmpty() || kraj == null) {
                    showAlert("Vnesite vse podatke!", "warning");
                    return null;
                }

                database.Backend.registerUser(ime, priimek, email, pass, opis, letoRojstva, kraj);
                showAlert("Dobrodošel! Prijavi se.", "info");
                dialog.close();
            }
            return null;
        });

        dialog.showAndWait();
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(title);
        alert.show();
    }

    public void start(Stage loginStage) {
        loginStage.setTitle("Login");
        loginStage.setScene(getScene());
        loginStage.show();
    }

}
