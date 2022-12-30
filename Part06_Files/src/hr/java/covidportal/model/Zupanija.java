package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Služi za kreiranje instanci županije koje su potrebne za ispravo funkcioniranje programa.
 */
public class Zupanija extends ImenovaniEntitet implements Serializable {
    private Integer brojStanovnika;
    private Integer brojZarazenih;

    /**
     * Prima naziv koji je naslijeđen iz klase <code>ImenovaniEntitet</code> i broj stanovnika te ih inicijalizira.
     *
     * @param naziv ime županije
     * @param brojStanovnika broj stanovnika u županiji
     * @param brojZarazenih broj zaraženih stanovnika županije
     */
    public Zupanija(Long id, String naziv, Integer brojStanovnika, Integer brojZarazenih) {
        super(id, naziv);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenih = brojZarazenih;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojZarazenih() {
        return brojZarazenih;
    }

    public void setBrojZarazenih(Integer brojZarazenih) {
        this.brojZarazenih = brojZarazenih;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zupanija zupanija = (Zupanija) o;
        return Objects.equals(getNaziv(), zupanija.getNaziv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNaziv());
    }
}
