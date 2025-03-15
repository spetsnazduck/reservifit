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
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("ReserviFit");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");
        signupButton.setOnMouseClicked(e -> openSignUp());
        loginButton.setOnAction(e -> {
            // Check if the username and password fields are not empty
            if (usernameField.getText() != "" && passwordField.getText() != "") {

                // Output to the console that login attempt has started
                System.out.println("Login attempt for user: " + usernameField.getText());

                // Call the validateUser method to check authentication
                boolean isAuthenticated = database.Backend.validateUser(usernameField.getText(), passwordField.getText());
                boolean isBiz = database.Backend.isBiz();
                String bizName = "none";
                if (isBiz == true) {
                    bizName = database.Backend.fetchBizName();
                }
                // Output the result of authentication check
                if (isAuthenticated) {
                    System.out.println("Login successful for user: " + usernameField.getText());
                    if (isBiz == true) {
                        BizDashboard bizDashboard = new BizDashboard(primaryStage, bizName);
                        primaryStage.setScene(bizDashboard.getScene());
                    } else {
                        MainDashboard mainDashboard = new MainDashboard(primaryStage);
                        primaryStage.setScene(mainDashboard.getScene());
                    }
                } else {
                    System.out.println("Login failed for user: " + usernameField.getText());
                    showAlert("Login Failed", "Invalid credentials");
                }
            } else {
                System.out.println("Login failed, empty field detected.");
                showAlert("Login Failed", "You left a field empty!");
            }
        });
        HBox actionButtons = new HBox(10);
        actionButtons.setPadding(new Insets(20));
        actionButtons.getChildren().addAll(loginButton, signupButton);

        layout.getChildren().addAll(titleLabel, usernameField, passwordField, actionButtons);
        scene = new Scene(layout, 300, 200);
    }

    public Scene getScene() {
        return scene;
    }

    private void openSignUp() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registracija");
        dialog.setHeaderText(null);  // No default header
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Set custom style
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");
        Label titleLabel = new Label("ReserviFit");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        // Input Fields
        TextField imeField = new TextField();
        imeField.setPromptText("Ime");

        TextField priimekField = new TextField();
        priimekField.setPromptText("Priimek");

        TextField emailField = new TextField();
        emailField.setPromptText("E-pošta");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Geslo");

        TextField opisField = new TextField();
        opisField.setPromptText("Opis");

        DatePicker letoRojPicker = new DatePicker();
        letoRojPicker.setPromptText("Leto rojstva");

        ComboBox<String> krajComboBox = new ComboBox<>();
        krajComboBox.setPromptText("Izberite kraj");

        database.Backend.fetchKraji(krajComboBox); // Fetch data from DB

        // Layout Styling
        VBox layout = new VBox(10, titleLabel, imeField, priimekField, emailField, passField, opisField, letoRojPicker, krajComboBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("dialog-content");

        dialog.getDialogPane().setContent(layout);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Form Submission
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
                    showAlert("Vnesite vse podatke!", String.valueOf(Alert.AlertType.WARNING));
                    return null;
                }

                database.Backend.registerUser(ime, priimek, email, pass, opis, letoRojstva, kraj);
                    showAlert("Dobrodošel! Prijavi se.", String.valueOf(Alert.AlertType.INFORMATION));
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
