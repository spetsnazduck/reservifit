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

import java.time.LocalDate;

public class PonudnikComponent extends VBox {
    private int ponudnikId;
    public PonudnikComponent(Integer id, String title, String description, String tema, String ime, String priimek, String location) {
        setPadding(new Insets(10));
        ponudnikId = id;
        setSpacing(8);
        setPrefWidth(400);
        setStyle("-fx-background-color: #2A2A2A; -fx-background-radius: 10; -fx-padding: 15;");

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label temaLabel = new Label(tema);
        temaLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #AAAAAA; -fx-font-size: 14px;");

        // Price and Location (aligned horizontally)

        Label locationLabel = new Label(location);
        locationLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 14px;");

        Button showDetailsButton = new Button("Več o ponudniku");

        // Set the action on the button to open the popup
        showDetailsButton.setOnAction(e -> openOfferDetailsPopup(ponudnikId));
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

        HBox detailsBox = new HBox(20, locationLabel);

        getChildren().addAll(titleLabel, temaLabel, descLabel, detailsBox, showDetailsButton);
    }

    private void openOfferDetailsPopup(int ponudnikId) {
        // Create the stage and scene for the popup
        Stage popupStage = new Stage();
        popupStage.setTitle("Offer Details");

        // Create layout for the popup
        VBox popupLayout = new VBox(10);
        popupLayout.setPadding(new Insets(10));

        // Label displaying offer details
        Label detailsLabel = new Label("Details for Offer ID: " + ponudnikId);
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Additional information
        String offerDetails = "Offer Details for ID: " + ponudnikId + "\nMore information about the offer goes here.";
        Label additionalInfoLabel = new Label(offerDetails);
        additionalInfoLabel.setStyle("-fx-font-size: 14px; -fx-wrap-text: true;");

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #ff5733; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton.setOnAction(e -> popupStage.close());

        // Add components to the layout
        popupLayout.getChildren().addAll(detailsLabel, additionalInfoLabel, closeButton);

        // Create a scene for the popup with specific size
        Scene popupScene = new Scene(popupLayout, 300, 200);
        popupStage.setScene(popupScene);

        // Show the popup window
        popupStage.show();
    }

}
