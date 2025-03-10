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

import java.util.List;
import java.util.Objects;

public class MainDashboard {
    private Scene scene;
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

        settingsIcon.setOnMouseClicked(e -> System.out.println("Open settings..."));
        exitIcon.setOnMouseClicked(e -> primaryStage.close());
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

    public Scene getScene() {
        return scene;
    }
}
