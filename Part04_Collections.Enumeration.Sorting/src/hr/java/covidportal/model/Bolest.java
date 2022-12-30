package hr.java.covidportal.model;

import java.util.Objects;
import java.util.Set;

/**
 * Služi za kreiranje instanci bolesti koje su potrebne za ispravo funkcioniranje programa.
 */
public class Bolest extends ImenovaniEntitet{
    private Set<Simptom> simptomi;

    /**
     * Prima naziv koji je naslijeđen iz klase <code>ImenovaniEntitet</code> i polje simptomi klase <code>Simptom</code>
     * te ih inicijalizira.
     *
     * @param naziv ime bolesti
     * @param simptomi polje simptoma
     */
    public Bolest(String naziv, Set<Simptom> simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Set<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Set<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(getNaziv(), bolest.getNaziv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNaziv());
    }
}
