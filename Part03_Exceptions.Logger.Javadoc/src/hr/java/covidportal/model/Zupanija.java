package hr.java.covidportal.model;

/**
 * Služi za kreiranje instanci županije koje su potrebne za ispravo funkcioniranje programa.
 */
public class Zupanija extends ImenovaniEntitet {
    private Integer brojStanovnika;

    /**
     * Prima naziv koji je naslijeđen iz klase <code>ImenovaniEntitet</code> i broj stanovnika te ih inicijalizira.
     *
     * @param naziv ime županije
     * @param brojStanovnika broj stanovnika u županiji
     */
    public Zupanija(String naziv, Integer brojStanovnika) {
        super(naziv);
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }
}
