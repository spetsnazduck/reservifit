package components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

public class PonudbaComponentBiz extends VBox {

    private int ponudbaId;

    public PonudbaComponentBiz(Integer id,String title, String description, double price, String location) {
        setPadding(new Insets(10));
        ponudbaId = id;
        setSpacing(8);
        setPrefWidth(400);
        setStyle("-fx-background-color: #2A2A2A; -fx-background-radius: 10; -fx-padding: 15;");

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #AAAAAA; -fx-font-size: 14px;");

        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Price and Location (aligned horizontally)
        Label priceLabel = new Label("$" + price);
        priceLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label locationLabel = new Label(location);
        locationLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 14px;");

        Button showDetailsButton = new Button("Izbriši");
        Button updateDetailsButton = new Button("Uredi");

        // Set the action on the button to open the popup
        showDetailsButton.setOnAction(e -> openOfferDetailsPopup(ponudbaId));
        updateDetailsButton.setOnAction(e -> updateOfferPopup(ponudbaId));
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

        updateDetailsButton.setStyle(
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
        updateDetailsButton.setOnMouseEntered(e -> showDetailsButton.setStyle(
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

        updateDetailsButton.setOnMouseExited(e -> showDetailsButton.setStyle(
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
        HBox buttonsBox = new HBox(20, showDetailsButton, updateDetailsButton);
        getChildren().addAll(descLabel, titleLabel, detailsBox, buttonsBox);
    }

    private void openOfferDetailsPopup(int ponudbaId) {
        // Create the popup stage
        Stage popupStage = new Stage();
        popupStage.setTitle("Izbriši ponudbo");
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window

        Label titleLabel = new Label("Ali res želite izbrisati ponudbo?");
        titleLabel.setStyle("-fx-text-fill: #AAAAAA; -fx-font-size: 14px;");

        // Styled close button
        Button closeButton = new Button("Prekliči");
        closeButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-font-size: 14px; -fx-border-radius: 5px;");

        // Button action to send the reservation data to the backend
        Button reserveButton = new Button("Izbriši ponudbo");
        reserveButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        reserveButton.setOnAction(e -> {
                boolean success = database.Backend.deleteOffer(ponudbaId);
            if (success) {
                BizDashboard.showOffers();
                popupStage.close();
            } else {
                showAlert("Ponudba ni izbrisana.", "error");
            }
        });

        closeButton.setOnAction(e -> popupStage.close());

        // Layout setup
        VBox popupLayout = new VBox(15);
        popupLayout.getChildren().addAll(titleLabel, reserveButton, closeButton);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setStyle("-fx-padding: 20px; -fx-background-color: #f8f9fa; -fx-border-radius: 10px;");

        // Create scene for the popup
        Scene popupScene = new Scene(popupLayout, 350, 300);
        popupStage.setScene(popupScene);

        // Show the popup window
        popupStage.show();
    }

    private void updateOfferPopup(int ponudbaId) {
        // Fetch the current offer data from the backend
        Offer offer = database.Backend.getOfferById(ponudbaId);
        if (offer == null) {
            showAlert("Ponudba ni najdena.", "error");
            return;
        }

        // Create the popup stage
        Stage popupStage = new Stage();
        popupStage.setTitle("Posodobitev ponudbe");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        Label titleLabel = new Label("Posodobi ponudbo.");
        titleLabel.setStyle("-fx-text-fill: #AAAAAA; -fx-font-size: 14px;");

        // Input fields with existing data
        TextField cenaField = new TextField(String.valueOf(offer.getCena()));
        cenaField.setPromptText("Cena");

        TextArea opisField = new TextArea(offer.getOpis());
        opisField.setPromptText("Opis");

        TextField trajanjeField = new TextField((offer.getTrajanjePonudbe()).toString());
        trajanjeField.setPromptText("Trajanje ponudbe");

        // Styled close button
        Button closeButton = new Button("Prekliči");
        closeButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        closeButton.setOnAction(e -> popupStage.close());

        // Button action to send updated data to backend
        Button updateButton = new Button("Posodobi ponudbo");
        updateButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        updateButton.setOnAction(e -> {
            // Get updated values
            double updatedCena;
            int updatedTrajanjePon;
            try {
                updatedCena = Double.parseDouble(cenaField.getText().trim());
                updatedTrajanjePon = Integer.parseInt(trajanjeField.getText().trim());
            } catch (NumberFormatException ex) {
                showAlert("Cena in trajanje ponudbe mora biti številka.", "error");
                return;
            }

            String updatedOpis = opisField.getText().trim();
            String updatedTrajanje = trajanjeField.getText().trim();

            // Send updated values to the backend
            boolean success = database.Backend.updateOffer(ponudbaId, updatedCena, updatedOpis, updatedTrajanjePon);
            if (success) {
                BizDashboard.showOffers();
                popupStage.close();
            } else {
                showAlert("Posodobitev ni uspela.", "error");
            }
        });

        // Layout setup
        VBox popupLayout = new VBox(15);
        popupLayout.getChildren().addAll(titleLabel, cenaField, opisField, trajanjeField, updateButton, closeButton);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setStyle("-fx-padding: 20px; -fx-background-color: #f8f9fa; -fx-border-radius: 10px;");

        // Create scene for the popup
        Scene popupScene = new Scene(popupLayout, 400, 400);
        popupStage.setScene(popupScene);

        // Show the popup window
        popupStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(title);
        alert.show();
    }

}
