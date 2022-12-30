package hr.java.covidportal.model;

/**
 * Sadrži jedno svojstvo naziv koje će u klasama koje naslijeđuju ovu klasu biti inicijalizirano.
 */
public abstract class ImenovaniEntitet {
    private String naziv;

    /**
     * Prima naziv koji je potreban za inicijalizaciju budućeg entiteta.
     *
     * @param naziv ime budućeg entiteta
     */
    public ImenovaniEntitet(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
