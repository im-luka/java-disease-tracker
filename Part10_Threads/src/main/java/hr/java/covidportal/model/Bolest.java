package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Služi za kreiranje instanci bolesti koje su potrebne za ispravo funkcioniranje programa.
 */
public class Bolest extends ImenovaniEntitet implements Serializable {
    private boolean jeLiVirus;
    private List<Simptom> simptomi;

    /**
     * Prima naziv koji je naslijeđen iz klase <code>ImenovaniEntitet</code> i polje simptomi klase <code>Simptom</code>
     * te ih inicijalizira.
     *
     * @param naziv ime bolesti
     * @param simptomi polje simptoma
     */
    public Bolest(Long id, String naziv, boolean jeLiVirus, List<Simptom> simptomi) {
        super(id, naziv);
        this.jeLiVirus = jeLiVirus;
        this.simptomi = simptomi;
    }

    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    public boolean isJeLiVirus() {
        return jeLiVirus;
    }

    public void setJeLiVirus(boolean jeLiVirus) {
        this.jeLiVirus = jeLiVirus;
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
