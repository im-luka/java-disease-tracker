package hr.java.covidportal.model;

/**
 * Služi za kreiranje instanci bolesti koje su potrebne za ispravo funkcioniranje programa.
 */
public class Bolest extends ImenovaniEntitet{
    private Simptom[] simptomi;

    /**
     * Prima naziv koji je naslijeđen iz klase <code>ImenovaniEntitet</code> i polje simptomi klase <code>Simptom</code>
     * te ih inicijalizira.
     *
     * @param naziv ime bolesti
     * @param simptomi polje simptoma
     */
    public Bolest(String naziv, Simptom[] simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Simptom[] getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Simptom[] simptomi) {
        this.simptomi = simptomi;
    }
}
