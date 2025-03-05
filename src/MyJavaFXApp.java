import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class MyJavaFXApp extends Application {
    private TableView<Kraj> krajiTable = new TableView<>();
    private TableView<Uporabnik> uporabnikiTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        Label label;
        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            label = new Label("Connected to Database!");
            loadKrajiData(conn);
            loadUporabnikiData(conn);
        } else {
            label = new Label("Failed to connect.");
        }

        // KRAJI Table
        TableColumn<Kraj, Integer> krajiIdColumn = new TableColumn<>("ID");
        krajiIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Kraj, String> krajiImeColumn = new TableColumn<>("Ime");
        krajiImeColumn.setCellValueFactory(cellData -> cellData.getValue().imeProperty());

        TableColumn<Kraj, String> krajiPostnaColumn = new TableColumn<>("Postna St.");
        krajiPostnaColumn.setCellValueFactory(cellData -> cellData.getValue().postnaProperty());

        krajiTable.getColumns().addAll(krajiIdColumn, krajiImeColumn, krajiPostnaColumn);

        // UPORABNIKI Table
        TableColumn<Uporabnik, Integer> uporabnikiIdColumn = new TableColumn<>("ID");
        uporabnikiIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Uporabnik, String> uporabnikiImeColumn = new TableColumn<>("Ime");
        uporabnikiImeColumn.setCellValueFactory(cellData -> cellData.getValue().imeProperty());

        TableColumn<Uporabnik, String> uporabnikiEmailColumn = new TableColumn<>("Email");
        uporabnikiEmailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        uporabnikiTable.getColumns().addAll(uporabnikiIdColumn, uporabnikiImeColumn, uporabnikiEmailColumn);

        // CRUD Buttons for KRAJI
        TextField krajNameInput = new TextField();
        krajNameInput.setPromptText("Ime kraja");

        TextField krajPostnaInput = new TextField();
        krajPostnaInput.setPromptText("Poštna številka");

        Button addKraj = new Button("Add Kraj");
        addKraj.setOnAction(e -> insertKraj(krajNameInput.getText(), krajPostnaInput.getText()));

        Button updateKraj = new Button("Update Kraj");
        updateKraj.setOnAction(e -> {
            Kraj selected = krajiTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                updateKraj(selected.getId(), krajNameInput.getText(), krajPostnaInput.getText());
            }
        });

        Button deleteKraj = new Button("Delete Kraj");
        deleteKraj.setOnAction(e -> {
            Kraj selected = krajiTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                deleteKraj(selected.getId());
            }
        });

        // CRUD Buttons for UPORABNIKI
        TextField uporabnikNameInput = new TextField();
        uporabnikNameInput.setPromptText("Ime uporabnika");

        TextField uporabnikEmailInput = new TextField();
        uporabnikEmailInput.setPromptText("Email");

        Button addUporabnik = new Button("Add Uporabnik");
        addUporabnik.setOnAction(e -> insertUporabnik(uporabnikNameInput.getText(), uporabnikEmailInput.getText()));

        Button updateUporabnik = new Button("Update Uporabnik");
        updateUporabnik.setOnAction(e -> {
            Uporabnik selected = uporabnikiTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                updateUporabnik(selected.getId(), uporabnikNameInput.getText(), uporabnikEmailInput.getText());
            }
        });

        Button deleteUporabnik = new Button("Delete Uporabnik");
        deleteUporabnik.setOnAction(e -> {
            Uporabnik selected = uporabnikiTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                deleteUporabnik(selected.getId());
            }
        });

        // Tabs
        TabPane tabPane = new TabPane();
        Tab krajiTab = new Tab("Kraji", new VBox(10, krajiTable, krajNameInput, krajPostnaInput, addKraj, updateKraj, deleteKraj));
        Tab uporabnikiTab = new Tab("Uporabniki", new VBox(10, uporabnikiTable, uporabnikNameInput, uporabnikEmailInput, addUporabnik, updateUporabnik, deleteUporabnik));

        krajiTab.setClosable(false);
        uporabnikiTab.setClosable(false);

        tabPane.getTabs().addAll(krajiTab, uporabnikiTab);

        VBox vbox = new VBox(10, label, tabPane);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox, 600, 500);

        primaryStage.setTitle("JavaFX - Database Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void insertKraj(String ime, String postna) {
        String sql = "INSERT INTO kraji (ime, postna_st) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ime);
            stmt.setString(2, postna);
            stmt.executeUpdate();
            loadKrajiData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateKraj(int id, String ime, String postna) {
        String sql = "UPDATE kraji SET ime = ?, postna_st = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ime);
            stmt.setString(2, postna);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            loadKrajiData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteKraj(int id) {
        String sql = "DELETE FROM kraji WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            loadKrajiData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUporabnik(String ime, String email) {
        String sql = "INSERT INTO uporabniki (ime, email) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ime);
            stmt.setString(2, email);
            stmt.executeUpdate();
            loadUporabnikiData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUporabnik(int id, String ime, String email) {
        String sql = "UPDATE uporabniki SET ime = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ime);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            loadUporabnikiData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUporabnik(int id) {
        String sql = "DELETE FROM uporabniki WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            loadUporabnikiData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadKrajiData(Connection conn) {
        String sql = "SELECT * FROM kraji";
        ObservableList<Kraj> krajiList = FXCollections.observableArrayList();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                krajiList.add(new Kraj(rs.getInt("id"), rs.getString("ime"), rs.getString("postna_st")));
            }
            krajiTable.setItems(krajiList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUporabnikiData(Connection conn) {
        String sql = "SELECT * FROM uporabniki";
        ObservableList<Uporabnik> uporabnikiList = FXCollections.observableArrayList();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                uporabnikiList.add(new Uporabnik(rs.getInt("id"), rs.getString("ime"), rs.getString("email")));
            }
            uporabnikiTable.setItems(uporabnikiList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
