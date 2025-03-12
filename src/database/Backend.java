package database;

import javafx.scene.control.ComboBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Backend {
    private static final String URL = "jdbc:postgresql://ep-proud-cherry-a80jkr7w-pooler.eastus2.azure.neon.tech/reservifit?sslmode=require";
    private static final String USER = "reservifit_owner";
    private static final String PASSWORD = "npg_V8XkPJLYeWS2";

    public static int loggedUserID;

    // Connect to the database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    // Fetch offers from the database
    public static List<components.PonudbaComponent> fetchOffers() {
        List<components.PonudbaComponent> components = new ArrayList<>();
        String sql = "SELECT * FROM fetchAllOffers();";

        // Using the connect() method to establish a connection
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Process the result set and create PonudbaComponent objects
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("ponudnik_ime"); // Description
                String description = rs.getString("opis"); // Assuming it's the same as name for now
                double price = rs.getDouble("cena");
                String location = rs.getString("kraj_ime");

                // Create PonudbaComponent and add it to the list
                components.add(new components.PonudbaComponent(id, name, description, price, location));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching offers: " + e.getMessage());
            e.printStackTrace();
        }

        return components;
    }

    public static List<components.PonudbaComponentBiz> fetchOffersBiz() {
        List<components.PonudbaComponentBiz> components = new ArrayList<>();
        String sql = "SELECT * FROM fetchAllOffersBiz(?);";

        // Establish connection
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, loggedUserID); // Set parameter

            // Execute query and process results
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("ponudnik_ime");
                    String description = rs.getString("opis");
                    double price = rs.getDouble("cena");
                    String location = rs.getString("kraj_ime");

                    // Add to list
                    components.add(new components.PonudbaComponentBiz(id, name, description, price, location));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching offers biz: " + e.getMessage());
            e.printStackTrace();
        }

        return components;
    }




    public static void reserve(int ponudbaId, String cas, Date datum) {
        String sql = "SELECT * FROM podajRezervacijo(?, ?, ?, ?);"; // Function expects two parameters

        // Debugging: Print the values of the parameters
        System.out.println("Debugging - Parameters:");
        System.out.println("ponudbaId: " + ponudbaId);
        System.out.println("loggedUserID: " + loggedUserID);  // Assuming loggedUserID is stored here
        System.out.println("cas: " + cas);
        System.out.println("datum: " + datum);

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters: first is ponudbaId, second is logged-in user's ID
            stmt.setInt(1, ponudbaId);
            stmt.setInt(2, loggedUserID); // Assuming loggedUserID is stored here
            stmt.setString(3, cas);
            stmt.setDate(4, datum);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Reservation successful!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reserving termin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<components.RezervacijaComponent> getRezervacije() {
        List<components.RezervacijaComponent> components = new ArrayList<>();
        String sql = "SELECT * FROM getRezervacije(?);"; // Function expects two parameters

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters: first is ponudbaId, second is logged-in user's ID
            stmt.setInt(1, loggedUserID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("termin_id");
                    Date datum = rs.getDate("datum"); // Description
                    Time cas = rs.getTime("cas"); // Assuming it's the same as name for now
                    String ime = rs.getString("ime_lastnika");
                    String location = rs.getString("kraj_ime");


                    // Create PonudbaComponent and add it to the list
                    components.add(new components.RezervacijaComponent(id, cas, datum, ime, location));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching rezervacije: " + e.getMessage());
            e.printStackTrace();
        }

        return components;
    }

    public static List<components.RezervacijaComponentBiz> getRezervacijeBiz() {
        List<components.RezervacijaComponentBiz> components = new ArrayList<>();
        String sql = "SELECT * FROM getRezervacijeBiz(?);"; // Function expects two parameters

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters: first is ponudbaId, second is logged-in user's ID
            stmt.setInt(1, loggedUserID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Integer id = rs.getInt("termin_id");
                    Date datum = rs.getDate("datum"); // Description
                    Time cas = rs.getTime("cas"); // Assuming it's the same as name for now
                    String ime = rs.getString("ime_uporabnika");
                    String location = rs.getString("kraj_ime");


                    // Create PonudbaComponent and add it to the list
                    components.add(new components.RezervacijaComponentBiz(id, cas, datum, ime, location));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching rezervacije: " + e.getMessage());
            e.printStackTrace();
        }

        return components;
    }

    public static void deleteReservation(int id) {
        String sql = "SELECT izbrisiRezervacijo(?);"; // Function expects one parameter

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameter
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String resultMessage = rs.getString(1);
                    System.out.println(resultMessage);
                }
            }
            System.out.println("Rezervacija successfully deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting rezervacija: " + e.getMessage());
            e.printStackTrace();
        }
    }




    public static List<components.PonudnikComponent> fetchPonudniki() {
        List<components.PonudnikComponent> components = new ArrayList<>();
        String sql = "SELECT * FROM vsiPonudniki();";

        // Using the connect() method to establish a connection
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Process the result set and create PonudbaComponent objects
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("ime"); // Description
                String description = rs.getString("opis"); // Assuming it's the same as name for now
                String tema = rs.getString("tema");
                String ime = rs.getString("ime_lastnika");
                String priimek = rs.getString("priimek_lastnika");
                String location = rs.getString("kraj_ime");

                if (ime == "" && priimek == "") {
                    ime = "";
                    priimek = "";
                }

                // Create PonudbaComponent and add it to the list
                components.add(new components.PonudnikComponent(id, name, description, tema, ime, priimek, location));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching ponudniki: " + e.getMessage());
            e.printStackTrace();
        }

        return components;
    }

    public static boolean isBiz() {
        String sql = "SELECT isUserBiz(?)";
        try (Connection conn = connect()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                return false;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, loggedUserID);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {

                    boolean result = rs.getBoolean(1);

                    return result;
                } else {
                    System.out.println("No rows returned.");
                }

            }
        } catch (SQLException e) {
            System.out.println("Error checking if biz: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static String fetchBizName() {
        String sql = "SELECT fetchBizName(?)";
        try (Connection conn = connect()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                return "failed";
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, loggedUserID);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {

                    String result = rs.getString(1);

                    return result;
                } else {
                    System.out.println("No rows returned.");
                }

            }
        } catch (SQLException e) {
            System.out.println("Error checking if biz: " + e.getMessage());
            e.printStackTrace();
        }

        return "failed";
    }

    // Validate user credentials
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT validate_user_credentials(?, ?)";

        try (Connection conn = connect()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                return false;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password); // Don't forget this!

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Debugging column names
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.println("Column " + i + ": " + metaData.getColumnName(i));
                    }

                    // Fetching the values from the result set
                    String result = rs.getString(1);  // Assuming the tuple is returned as a String
                    System.out.println("Result: " + result);  // Check the content
                    boolean isValid = false;
                    int userId = 0;
                    if (result != null) {
                        String[] parts = result.replace("(", "").replace(")", "").split(",");
                        String isValidString = parts[0].trim();  // Extract the boolean part (e.g., "t" or "f")
                        userId = Integer.parseInt(parts[1].trim());  // Extract the user ID
                        System.out.println(userId);
                        loggedUserID = userId;

                        isValid = isValidString.equalsIgnoreCase("t");

                        System.out.println("DEBUG - isValid: " + isValid + ", userId: " + userId);
                    } else {
                        System.out.println("Null");
                    }
                    // Second column: int

                    System.out.println("DEBUG - isValid: " + isValid + ", userId: " + userId);  // Debug output
                    System.out.println("DEBUG - SQL Result: " + rs.getObject(1));


                    if (isValid) {
                        System.out.println("User valid, ID: " + userId);
                    } else {
                        System.out.println("Invalid credentials.");
                    }

                    return isValid;
                } else {
                    System.out.println("No rows returned.");
                }

            }
        } catch (SQLException e) {
            System.out.println("Error checking credentials: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static void fetchKraji(ComboBox<String> krajComboBox) {
        String sql = "SELECT * FROM getKraji();";  // Adjust SQL query as needed

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                krajComboBox.getItems().add(rs.getString("kime"));
            }

        } catch (SQLException e) {
            System.out.println("Napaka pri pridobivanju krajev: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String ime, String priimek, String email, String pass, String opis, String letoRojstva, String kraj) {
        String sql = "SELECT * FROM signupUser(?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, ime);
            stmt.setString(2, priimek);
            stmt.setString(3, email);
            stmt.setString(4, pass);
            stmt.setString(5, opis);
            stmt.setString(6, letoRojstva);
            stmt.setString(7, kraj);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Napaka pri registraciji: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUserDetails(String ime, String priimek, String email, String password, String description) {
        String sql = "SELECT updateUser(?, ?, ?, ?, ?, ?);"; // Call your SQL function

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, loggedUserID);
            stmt.setString(2, ime);
            stmt.setString(3, priimek);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, description);

            // Execute query
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1); // Assuming your SQL function returns BOOLEAN
            }

        } catch (SQLException e) {
            System.out.println("Error updating user details: " + e.getMessage());
            e.printStackTrace();
        }

        return false; // Return false in case of failure
    }

    public static boolean registerBiz(String ime, String letnica, String opis, String kraj) {
        String sql = "SELECT registerBiz(?, ?, ?, ?, ?);";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, loggedUserID);
            stmt.setString(2, ime);
            stmt.setString(3, letnica);
            stmt.setString(4, opis);
            stmt.setString(5, kraj);

            // Execute query
            stmt.executeQuery();

            return true;
        } catch (SQLException e) {
            System.out.println("Error adding business: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static void deleteUser() {

    }

    public static boolean newPonudba(String cena, String opis, String trajanje) {
        String sql = "SELECT newPonudba(?, ?, ?, ?);";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, loggedUserID);
            stmt.setString(2, cena);
            stmt.setString(3, opis);
            stmt.setString(4, trajanje);

            // Execute query
            stmt.executeQuery();

            return true;
        } catch (SQLException e) {
            System.out.println("Error adding ponudba: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteOffer(int ponudbaId) {
        String sql = "SELECT deleteOffer(?);";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, ponudbaId);

            // Execute query
            stmt.executeQuery();

            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting ponudba: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteBusiness() {
        String sql = "SELECT deleteBusiness(?);";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, loggedUserID);

            // Execute query
            stmt.executeQuery();

            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting ponudba: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateBusinessDetails(String name, String email, String password, String description, String kraj) {
        String sql = "SELECT updateBusiness(?, ?, ?, ?, ?, ?);"; // Call your SQL function

        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set parameters
            stmt.setInt(1, loggedUserID);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, description);
            stmt.setString(5, kraj);
            stmt.setString(6, name);

            // Execute query
            stmt.executeQuery();

            return true;

        } catch (SQLException e) {
            System.out.println("Error updating business details: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
}
