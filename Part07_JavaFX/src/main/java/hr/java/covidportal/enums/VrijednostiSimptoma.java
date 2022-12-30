package main.java.hr.java.covidportal.enums;

/**
 * Zamjenjuje vrijednosti simptoma konstantama koje predstavljaju svaki simptom.
 */
public enum VrijednostiSimptoma {
    RIJETKO("RIJETKO"),
    SREDNJE("SREDNJE"),
    CESTO("CESTO");

    private String vrijednost;

    VrijednostiSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}
