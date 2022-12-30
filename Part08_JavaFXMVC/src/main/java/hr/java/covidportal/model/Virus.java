package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Naslijeđuje klasu <code>Bolest</code> i služi za kreiranje instanci virusa koji su potrebni za ispravo funkcioniranje programa.
 */
public class Virus extends Bolest implements Zarazno, Serializable {

    /**
     * Prima naziv naslijeđen iz klase <code>Bolest</code> odnosno <code>ImenovaniEntitet</code> i set simptomi klase
     * <code>Simptom</code> te ih inicijalizira.
     *
     * @param naziv ime virusa
     * @param simptomi set simptoma
     */
    public Virus(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv, simptomi);
    }

    /**
     * Nadjačava metodu iz interface-a <code>Zarazno</code> koja dodjeljuje osobi zarazu ovim virusom.
     *
     * @param osoba određena osoba klase <code>Osoba</code>
     */
    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Virus virus = (Virus) o;
        return Objects.equals(getNaziv(), virus.getNaziv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNaziv());
    }
}
