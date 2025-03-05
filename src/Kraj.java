import javafx.beans.property.*;

public class Kraj {
    private final IntegerProperty id;
    private final StringProperty ime;
    private final StringProperty postna;

    public Kraj(int id, String ime, String postna) {
        this.id = new SimpleIntegerProperty(id);
        this.ime = new SimpleStringProperty(ime);
        this.postna = new SimpleStringProperty(postna);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getIme() { return ime.get(); }
    public StringProperty imeProperty() { return ime; }

    public String getPostna() { return postna.get(); }
    public StringProperty postnaProperty() { return postna; }
}
