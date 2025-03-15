package components;

public class Biznis {
    private String imebiz;
    private String opis;
    private String email;
    private String pass;

    public Biznis(String imebiz, String email, String pass, String opis) {
        this.imebiz = imebiz;
        this.opis = opis;
        this.email = email;
        this.pass = pass;
    }

    // Getters

    public String getImebiz() {
        return imebiz;
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

    public void setName(String bizname) {
        this.imebiz = bizname;
    }
}
