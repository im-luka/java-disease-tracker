package hr.java.covidportal.model;

/**
 * Naslijeđuje klasu <code>Bolest</code> i služi za kreiranje instanci virusa koji su potrebni za ispravo funkcioniranje programa.
 */
public class Virus extends Bolest implements Zarazno{

    /**
     * Prima naziv naslijeđen iz klase <code>Bolest</code> odnosno <code>ImenovaniEntitet</code> i polje simptomi klase
     * <code>Simptom</code> te ih inicijalizira.
     *
     * @param naziv ime virusa
     * @param simptomi polje simptoma
     */
    public Virus(String naziv, Simptom[] simptomi) {
        super(naziv, simptomi);
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
}
