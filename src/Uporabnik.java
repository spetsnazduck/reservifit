import javafx.beans.property.*;

public class Uporabnik {
    private final IntegerProperty id;
    private final StringProperty ime;
    private final StringProperty email;

    public Uporabnik(int id, String ime, String email) {
        this.id = new SimpleIntegerProperty(id);
        this.ime = new SimpleStringProperty(ime);
        this.email = new SimpleStringProperty(email);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getIme() { return ime.get(); }
    public StringProperty imeProperty() { return ime; }

    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
}
