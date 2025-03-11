package components;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;
import java.util.Objects;

public class MainDashboard {
    private static Scene scene;
    private static VBox contentContainer;
    private ScrollPane scrollPane;

    public MainDashboard(Stage primaryStage) {
        VBox root = new VBox();
        root.setPadding(new Insets(0));

        // NAVBAR - Modern Top Bar
        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(15, 20, 15, 20));
        navbar.setStyle("-fx-background-color: #222; -fx-border-width: 0 0 2px 0; -fx-border-color: #4CAF50;");
        navbar.setAlignment(Pos.CENTER_LEFT);

        // Brand Name
        Label brand = new Label("ReserviFit");
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Navigation Buttons
        Button btnRezervacije = new Button("Rezervacije");
        Button btnHome = new Button("🏠");
        Button btnPonudbe = new Button("Ponudbe");
        Button btnPonudniki = new Button("Ponudniki");

        styleNavButton(btnRezervacije);
        styleNavButton(btnHome);
        styleNavButton(btnPonudbe);
        styleNavButton(btnPonudniki);

        HBox navButtons = new HBox(15, btnRezervacije, btnHome, btnPonudbe, btnPonudniki);
        navButtons.setAlignment(Pos.CENTER);

        // Right Icons (Settings & Exit)
        ImageView settingsIcon = createIcon("settings.png", 24);
        ImageView exitIcon = createIcon("exit.png", 24);

        settingsIcon.setOnMouseClicked(e -> showSettings());
        exitIcon.setOnMouseClicked(e -> {
            openLoginWindow();
            primaryStage.close();
        });
        btnPonudbe.setOnMouseClicked(e -> showOffers());
        btnPonudniki.setOnMouseClicked(e -> showProviders());
        btnRezervacije.setOnMouseClicked(e -> showRezervacije());

        HBox rightIcons = new HBox(15, settingsIcon, exitIcon);
        rightIcons.setAlignment(Pos.CENTER_RIGHT);

        // Removing redundant spacer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        // Ensure children are added only once to the HBox
        navbar.getChildren().clear(); // Clear any previous children if necessary
        navbar.getChildren().addAll(brand, spacer, navButtons, rightIcons);

        contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(20));
        contentContainer.setStyle("-fx-background-color: #f0f0f0;");

        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Set a fixed height so it doesn't get cut off
        scrollPane.setPrefHeight(600);

        root.getChildren().addAll(navbar, scrollPane);

        scene = new Scene(root, 1200, 600);
        primaryStage.setTitle("Main Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public void showSettings() {
        Stage settingsStage = new Stage();
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setTitle("Settings");

        // Main layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Settings");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // === Update User Details Section ===
        Label userDetailsLabel = new Label("Update User Details");
        userDetailsLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        TextField nameField = new TextField();
        nameField.setPromptText("Ime");

        TextField surnameField = new TextField();
        nameField.setPromptText("Priimek");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Novo geslo");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Kratek Bio / Opis");
        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(2);

        Button updateBtn = new Button("Update Details");
        updateBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");

        updateBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String description = descriptionField.getText().trim();

            // Validation (optional but recommended)
            if (name.isEmpty() || email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Name and Email are required!", ButtonType.OK);
                alert.show();
                return;
            }

            // Send data to backend
            boolean success = database.Backend.updateUserDetails(name, surname, email, password, description);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User details updated successfully!", ButtonType.OK);
                alert.show();
                settingsStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update user details. Please try again.", ButtonType.OK);
                alert.show();
            }
        });

        // === Register as a Business Section ===
        Label bizLabel = new Label("Register as a Business");
        bizLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");

        Button registerBizBtn = new Button("Register as Business");
        registerBizBtn.setStyle("-fx-background-color: #008000; -fx-text-fill: white;");

        registerBizBtn.setOnAction(e -> {
            showRegisterBiz();
            settingsStage.close();
        });

        // === Danger Zone (Delete Account) ===
        Label dangerLabel = new Label("Danger Zone");
        dangerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red; -fx-padding: 10 0 0 0;");

        Button deleteBtn = new Button("Delete Account");
        deleteBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");

        deleteBtn.setOnAction(e -> {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Potrdi brisanje");
            confirmDialog.setHeaderText("Ali res hočeš izbrisati račun?");
            confirmDialog.setContentText("Ta akcija je končna.");

            if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // TODO: Add account deletion logic
                database.Backend.deleteUser();
                settingsStage.close();
            }
        });

        // Layout and adding elements
        layout.getChildren().addAll(
                titleLabel,
                userDetailsLabel, nameField, surnameField, emailField, passwordField, descriptionField, updateBtn,
                bizLabel, registerBizBtn
        );

        Scene scene = new Scene(layout, 350, 450);
        settingsStage.setScene(scene);
        settingsStage.showAndWait();
    }

    private void showRegisterBiz() {
        // Create a new stage for the modal
        Stage registerStage = new Stage();
        registerStage.initModality(Modality.APPLICATION_MODAL);
        registerStage.setTitle("Register Business");

        // Form fields
        TextField imeField = new TextField();
        imeField.setPromptText("Business Name");

        DatePicker letnicaField = new DatePicker();
        letnicaField.setPromptText("Year of Establishment");

        TextArea opisField = new TextArea();
        opisField.setPromptText("Business Description");

        ComboBox<String> krajComboBox = new ComboBox<>();
        krajComboBox.setPromptText("Izberite kraj");


        // Fetch locations from database
        database.Backend.fetchKraji(krajComboBox); // Assuming this function returns a List<String>

        // Submit button
        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            String ime = imeField.getText();
            String letnica = (letnicaField.getValue() != null) ? letnicaField.getValue().toString() : "";
            String opis = opisField.getText();
            String kraj = krajComboBox.getValue();

            if (ime.isEmpty() || letnica.isEmpty() || opis.isEmpty() || kraj == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled!", ButtonType.OK);
                alert.show();
                return;
            }

            // Call Backend function
            boolean success = database.Backend.registerBiz(ime, letnica, opis, kraj);
            String msg = success ? "Your business is registered successfully!" : "An error occurred!";

            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            alert.showAndWait();

            if (success) {
                // Close the current stage
                Stage currentStage = (Stage) registerStage.getScene().getWindow();
                currentStage.close();

                // Close the main dashboard
                Stage mainStage = (Stage) MainDashboard.getScene().getWindow();
                mainStage.close();

                // Open the login window
                openLoginWindow();
            }

        });


        // Layout
        VBox layout = new VBox(10, imeField, letnicaField, opisField, krajComboBox, registerBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        registerStage.setScene(scene);
        registerStage.showAndWait();
    }

    public static void openLoginWindow() {
        try {
            Stage loginStage = new Stage();
            LoginScreen loginScreen = new LoginScreen(loginStage);
            loginScreen.start(loginStage);
 // Start the login UI
        } catch (Exception e) {
            System.out.println("Error opening login window: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void showOffers() {
        contentContainer.getChildren().clear();
        List<PonudbaComponent> offers = database.Backend.fetchOffers();

        if (offers.isEmpty()) {
            contentContainer.getChildren().add(new Label("No offers available."));
        } else {
            for (PonudbaComponent offer : offers) {
                contentContainer.getChildren().add(offer);
            }
        }
    }

    public static void showRezervacije() {
        contentContainer.getChildren().clear();
        List<RezervacijaComponent> rezervacije = database.Backend.getRezervacije();

        if (rezervacije.isEmpty()) {
            contentContainer.getChildren().add(new Label("No reservations available."));
        } else {
            for (RezervacijaComponent rezervacija : rezervacije) {
                contentContainer.getChildren().add(rezervacija);
            }
        }
    }

    /**
     * Fetches and displays Ponudniki (Providers)
     */
    private void showProviders() {
        contentContainer.getChildren().clear();
        List<PonudnikComponent> providers = database.Backend.fetchPonudniki(); // Fetch from DB

        if (providers.isEmpty()) {
            contentContainer.getChildren().add(new Label("No providers available."));
        } else {
            for (PonudnikComponent provider : providers) {
                contentContainer.getChildren().add(provider);
            }
        }
    }



    private void styleNavButton(Button btn) {
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;"));
    }

    private ImageView createIcon(String iconName, int size) {
        ImageView icon = new ImageView(new Image(getClass().getResource("/" + iconName).toExternalForm()));
        icon.setFitWidth(size);
        icon.setFitHeight(size);
        icon.setPreserveRatio(true);
        return icon;
    }

    public static Scene getScene() {
        return scene;
    }
}
