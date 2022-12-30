package hr.java.covidportal.model;

public class Bolest {
    private String naziv;
    private Simptom[] simptomi;

    public Bolest(String naziv, Simptom[] simptomi) {
        this.naziv = naziv;
        this.simptomi = simptomi;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Simptom[] getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Simptom[] simptomi) {
        this.simptomi = simptomi;
    }
}
