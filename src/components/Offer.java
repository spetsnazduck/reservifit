package components;

public class Offer {
    private int id;
    private double cena;
    private String opis;
    private int trajanjePonudbe;

    public Offer(int id, double cena, String opis, Integer trajanjePonudbe) {
        this.id = id;
        this.cena = cena;
        this.opis = opis;
        this.trajanjePonudbe = trajanjePonudbe;
    }

    // Getters
    public int getId() {
        return id;
    }

    public double getCena() {
        return cena;
    }

    public String getOpis() {
        return opis;
    }

    public Integer getTrajanjePonudbe() {
        return trajanjePonudbe;
    }

    // Setters
    public void setCena(double cena) {
        this.cena = cena;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setTrajanjePonudbe(Integer trajanjePonudbe) {
        this.trajanjePonudbe = trajanjePonudbe;
    }
}
