package main.java.hr.java.covidportal.model;

import java.io.Serializable;

/**
 * Sadrži dva svojstva, naziv i id, koji će u klasama koje naslijeđuju ovu klasu biti inicijalizirani.
 */
public abstract class ImenovaniEntitet implements Serializable {
    private Long id;
    private String naziv;

    /**
     * Prima id i naziv koji je potreban za inicijalizaciju budućeg entiteta.
     *
     * @param id id budućeg entiteta
     * @param naziv ime budućeg entiteta
     */
    public ImenovaniEntitet(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
