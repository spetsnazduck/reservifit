package components;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.util.List;
import java.util.Objects;

public class BizDashboard {
    private Scene scene;
    private static VBox contentContainer;
    private ScrollPane scrollPane;
    private static String currentadd = "none";

    public BizDashboard(Stage primaryStage, String bizname) {
        VBox root = new VBox();
        root.setPadding(new Insets(0));

        // NAVBAR - Modern Top Bar
        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(15, 20, 15, 20));
        navbar.setStyle("-fx-background-color: #222; -fx-border-width: 0 0 2px 0; -fx-border-color: #4CAF50;");
        navbar.setAlignment(Pos.CENTER_LEFT);

        // Brand Name
        Label brand = new Label("ReserviFit" + " | " + bizname);
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Navigation Buttons
        Button btnRezervacije = new Button("Termini");
        Button btnPonudbe = new Button("Ponudbe");
        Button btnDodaj = new Button("Dodaj");

        styleNavButton(btnRezervacije);
        styleNavButton(btnPonudbe);
        styleNavButton(btnDodaj);

        HBox navButtons = new HBox(15, btnRezervacije, btnPonudbe, btnDodaj);
        navButtons.setAlignment(Pos.CENTER);

        // Right Icons (Settings & Exit)
        ImageView settingsIcon = createIcon("settings.png", 24);
        ImageView exitIcon = createIcon("exit.png", 24);

        settingsIcon.setOnMouseClicked(e -> showSettings(primaryStage));
        exitIcon.setOnMouseClicked(e -> {
            MainDashboard.openLoginWindow();
            primaryStage.close();

        });
        btnPonudbe.setOnMouseClicked(e -> showOffers());
        btnRezervacije.setOnMouseClicked(e -> showRezervacije());
        btnDodaj.setOnMouseClicked(e -> {
            if (currentadd == "ponudbe") {
                System.out.println("ponudbe add");
                showAddOfferModal();
            }
        });

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
        primaryStage.setTitle("Business Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showOffers() {
        contentContainer.getChildren().clear();
        List<PonudbaComponentBiz> offers = database.Backend.fetchOffersBiz();
        currentadd = "ponudbe";
        if (offers.isEmpty()) {
            contentContainer.getChildren().add(new Label("Dodaj ponudbe!"));
        } else {
            for (PonudbaComponentBiz offer : offers) {
                contentContainer.getChildren().add(offer);
            }
        }
    }

    public static void showRezervacije() {
        contentContainer.getChildren().clear();
        List<RezervacijaComponentBiz> rezervacije = database.Backend.getRezervacijeBiz();
        currentadd = "termini";
        if (rezervacije.isEmpty()) {
            contentContainer.getChildren().add(new Label("Ni rezerviranih terminov."));
        } else {
            for (RezervacijaComponentBiz rezervacija : rezervacije) {
                contentContainer.getChildren().add(rezervacija);
            }
        }
    }


    private void showAddOfferModal() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Ponudbo");
        dialog.setHeaderText("Vnesite podrobnosti ponudbe");
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Create input fields
        TextField cenaField = new TextField();
        cenaField.setPromptText("Cena (€)");

        TextField opisField = new TextField();
        opisField.setPromptText("Opis ponudbe");

        TextField trajanjeField = new TextField();
        trajanjeField.setPromptText("Trajanje v minutah");

        // Restrict trajanjeField to accept only numbers
        trajanjeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                trajanjeField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        cenaField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cenaField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Layout styling
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Cena:"), 0, 0);
        grid.add(cenaField, 1, 0);
        grid.add(new Label("Opis:"), 0, 1);
        grid.add(opisField, 1, 1);
        grid.add(new Label("Trajanje (min):"), 0, 2);
        grid.add(trajanjeField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle response
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String cena = cenaField.getText();
                String opis = opisField.getText();
                String trajanje = trajanjeField.getText();

                if (cena.isEmpty() || opis.isEmpty() || trajanje.isEmpty()) {
                    showAlert("Vnesite vse podatke!", Alert.AlertType.WARNING);
                    return null;
                } else {
                    boolean res = database.Backend.newPonudba(cena, opis, trajanje);
                    if (res) {
                        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmDialog.setTitle("Potrditev");
                        confirmDialog.setContentText("Ponudba uspešno dodana!");
                        confirmDialog.show();
                        showOffers();
                    } else {
                        Alert confirmDialog = new Alert(Alert.AlertType.WARNING);
                        confirmDialog.setTitle("Potrditev");
                        confirmDialog.setContentText("Ponudba ni dodana! Error.");
                        confirmDialog.show();
                    }
                }

                System.out.println("Nova ponudba: Cena: " + cena + ", Opis: " + opis + ", Trajanje: " + trajanje + " minut");
            }
            return null;
        });

        dialog.showAndWait();
    }


    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Opozorilo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public void showSettings(Stage primaryStage) {
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
        nameField.setPromptText("Ime Biznisa");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Novo geslo");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Kratek Bio / Opis");

        ComboBox<String> krajComboBox = new ComboBox<>();
        krajComboBox.setPromptText("Izberite kraj");

        database.Backend.fetchKraji(krajComboBox);

        descriptionField.setWrapText(true);
        descriptionField.setPrefRowCount(2);

        Button updateBtn = new Button("Update Details");
        updateBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");

        updateBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String description = descriptionField.getText().trim();
            String kraj = krajComboBox.getValue().trim();

            // Validation (optional but recommended)
            if (name.isEmpty() || email.isEmpty() || kraj == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Fill out all of the fields!", ButtonType.OK);
                alert.show();
                return;
            }

            // Send data to backend
            boolean success = database.Backend.updateBusinessDetails(name, email, password, description, kraj);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Business details updated successfully!", ButtonType.OK);
                alert.show();
                settingsStage.close();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update business details. Please try again.", ButtonType.OK);
                alert.show();
            }
        });

        Label dangerLabel = new Label("Danger Zone");
        dangerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red; -fx-padding: 10 0 0 0;");

        Button deleteBtn = new Button("Delete Account");
        deleteBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");

        deleteBtn.setOnAction(e -> {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Potrdi brisanje");
            confirmDialog.setHeaderText("Ali res hočeš izbrisati biznis?");
            confirmDialog.setContentText("Ta akcija je končna.");

            if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // TODO: Add account deletion logic
                boolean success = database.Backend.deleteBusiness();
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Business deleted succesfully!", ButtonType.OK);
                    alert.show();
                    settingsStage.close();
                    MainDashboard.openLoginWindow();
                    primaryStage.close();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to delete business. Please try again.", ButtonType.OK);
                    alert.show();
                }
            }
        });

        // Layout and adding elements
        layout.getChildren().addAll(
                titleLabel,
                userDetailsLabel, nameField, emailField, passwordField, descriptionField, krajComboBox, updateBtn,
                dangerLabel, deleteBtn
        );

        Scene scene = new Scene(layout, 350, 450);
        settingsStage.setScene(scene);
        settingsStage.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }


}
