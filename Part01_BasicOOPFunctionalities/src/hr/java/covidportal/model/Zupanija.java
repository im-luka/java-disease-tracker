package hr.java.covidportal.model;

public class Zupanija {
    private String naziv;
    private Integer brojStanovnika;

    public Zupanija(String naziv, Integer brojStanovnika) {
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }
}
