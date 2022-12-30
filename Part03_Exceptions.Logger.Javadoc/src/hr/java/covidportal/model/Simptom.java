package hr.java.covidportal.model;

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
}
