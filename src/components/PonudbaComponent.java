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
import java.time.LocalDate;

public class PonudbaComponent extends VBox {

    private int ponudbaId;

    public PonudbaComponent(Integer id,String title, String description, double price, String location) {
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

        Button showDetailsButton = new Button("Rezerviraj");

        // Set the action on the button to open the popup
        showDetailsButton.setOnAction(e -> openOfferDetailsPopup(ponudbaId));
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

    private void openOfferDetailsPopup(int ponudbaId) {
        // Create the popup stage
        Stage popupStage = new Stage();
        popupStage.setTitle("Rezervacija");
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window

        // Fetch the details for the specific offer (replace with actual database call if needed)
        String offerDetails = "Rezervirano! Ponudba ID: " + ponudbaId;

        // Create labels
        Label offerLabel = new Label(offerDetails);
        offerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Date picker for the user to choose a date
        Label selectDateLabel = new Label("Izberi datum:");
        DatePicker datePicker = new DatePicker(LocalDate.now());  // Default date is today's date
        datePicker.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");

        // Time picker (using TextField for simplicity)
        Label selectTimeLabel = new Label("Izberi čas (HH:mm):");
        TextField timeTextField = new TextField();
        timeTextField.setPromptText("HH:mm");
        timeTextField.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");

        // Styled close button
        Button closeButton = new Button("Zapri");
        closeButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-font-size: 14px; -fx-border-radius: 5px;");

        // Button action to send the reservation data to the backend
        Button reserveButton = new Button("Rezerviraj");
        reserveButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-font-size: 14px; -fx-border-radius: 5px;");
        reserveButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            String selectedTime = timeTextField.getText(); // User-input time (HH:mm)

            // Validate if the time input is in the correct format (HH:mm)
            if (selectedDate != null && !selectedTime.isEmpty()) {
                // Regular expression to check if the time is in HH:mm format

                    database.Backend.reserve(ponudbaId, selectedTime, Date.valueOf(selectedDate));

            } else {
                // Error handling: inform the user that the date and time are required
                System.out.println("Prosimo, izberite veljaven datum in čas.");
            }
        });

        closeButton.setOnAction(e -> popupStage.close());

        // Layout setup
        VBox popupLayout = new VBox(15);
        popupLayout.getChildren().addAll(offerLabel, selectDateLabel, datePicker, selectTimeLabel, timeTextField, reserveButton, closeButton);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setStyle("-fx-padding: 20px; -fx-background-color: #f8f9fa; -fx-border-radius: 10px;");

        // Create scene for the popup
        Scene popupScene = new Scene(popupLayout, 350, 300);
        popupStage.setScene(popupScene);

        // Show the popup window
        popupStage.show();
    }

}
