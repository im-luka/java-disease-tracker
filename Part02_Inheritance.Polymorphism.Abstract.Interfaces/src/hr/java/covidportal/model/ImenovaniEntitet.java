package hr.java.covidportal.model;

public abstract class ImenovaniEntitet {
    private String naziv;

    public ImenovaniEntitet(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
