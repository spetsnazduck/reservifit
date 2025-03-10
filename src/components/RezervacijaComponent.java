package components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class RezervacijaComponent extends VBox {

    private int rezervacijaId;

    public RezervacijaComponent(Integer rezid, Time cas, Date datum, String ime, String kraj) {
        setPadding(new Insets(10));
        setSpacing(8);
        rezervacijaId = rezid;
        setPrefWidth(400);
        setStyle("-fx-background-color: #2A2A2A; -fx-background-radius: 10; -fx-padding: 15;");

        // Title
        Label titleLabel = new Label(ime);
        titleLabel.setStyle("-fx-text-fill: #AAAAAA; -fx-font-size: 14px;");

        // Description
        Label descLabel = new Label(cas.toString());
        descLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(datum);
        // Price and Location (aligned horizontally)
        Label priceLabel = new Label(dateString);
        priceLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label locationLabel = new Label(kraj);
        locationLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 14px;");

        Button showDetailsButton = new Button("Cancel");

        // Set the action on the button to open the popup
        showDetailsButton.setOnAction(e -> openOfferDetailsPopup(rezervacijaId));
        showDetailsButton.setStyle(
                "-fx-background-color: #2c3e50; " +  // Dark blue-gray
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-color: #1abc9c; " +  // Turquoise border
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-transition: 0.3s;"
        );

// Add hover effect
        showDetailsButton.setOnMouseEntered(e -> showDetailsButton.setStyle(
                "-fx-background-color: #1abc9c; " +  // Turquoise on hover
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-color: #1abc9c; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-transition: 0.3s;"
        ));

        showDetailsButton.setOnMouseExited(e -> showDetailsButton.setStyle(
                "-fx-background-color: #2c3e50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-color: #1abc9c; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-transition: 0.3s;"
        ));

        HBox detailsBox = new HBox(20, priceLabel, locationLabel);

        getChildren().addAll(descLabel, titleLabel, detailsBox, showDetailsButton);
    }

    public static void refreshRezervacije() {
        MainDashboard.showRezervacije();
    }

    private static void openOfferDetailsPopup(int rezid) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Potrditev brisanja");
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window

        // Create label
        Label confirmLabel = new Label("Ali ste prepričani, da želite izbrisati to rezervacijo?");
        confirmLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Create buttons
        Button confirmButton = new Button("Da");
        confirmButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-padding: 5px 15px;");
        confirmButton.setOnAction(e -> {
            database.Backend.deleteReservation(rezid); // Call the backend to delete
            refreshRezervacije();
            popupStage.close();
        });

        Button cancelButton = new Button("Prekliči");
        cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-padding: 5px 15px;");
        cancelButton.setOnAction(e -> popupStage.close());

        // Layout
        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox popupLayout = new VBox(15, confirmLabel, buttonBox);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(20));
        popupLayout.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 10px;");

        // Scene setup
        Scene popupScene = new Scene(popupLayout, 350, 150);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }


}
