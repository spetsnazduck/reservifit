package components;

public class UserDetails {
    private String ime;
    private String priimek;
    private String opis;
    private String email;
    private String pass;

    public UserDetails(String ime, String priimek, String opis, String email, String pass) {
        this.ime = ime;
        this.priimek = priimek;
        this.opis = opis;
        this.email = email;
        this.pass = pass;
    }

    // Getters

    public String getUserIme() {
        return ime;
    }

    public String getUserPriimek() {
        return priimek;
    }

    public String getOpis() {
        return opis;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String ime) {
        this.ime = ime;
    }

    public void setSurname(String priimek) {
        this.priimek = priimek;
    }
}
