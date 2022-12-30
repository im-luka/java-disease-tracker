package hr.java.covidportal.model;

import hr.java.covidportal.enums.VrijednostiSimptoma;

import java.util.Objects;

/**
 * Služi za kreiranje instanci simptoma koje su potrebne za ispravo funkcioniranje programa.
 */
public class Simptom extends ImenovaniEntitet{
    private String vrijednost;

    /**
     * Prima naziv koji je naslijeđen iz klase <code>ImenovaniEntitet</code> i vrijednost simptoma te ih inicijalizira.
     *
     * @param naziv ime simptoma
     * @param vrijednost vrijednost simptoma
     */
    public Simptom(String naziv, String vrijednost) {
        super(naziv);
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Simptom simptom = (Simptom) o;
        return Objects.equals(getNaziv(), simptom.getNaziv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNaziv());
    }
}
