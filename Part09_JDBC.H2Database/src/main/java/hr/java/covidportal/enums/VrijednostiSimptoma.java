package main.java.hr.java.covidportal.enums;

/**
 * Zamjenjuje vrijednosti simptoma konstantama koje predstavljaju svaki simptom.
 */
public enum VrijednostiSimptoma {
    RIJETKO("Rijetko"),
    SREDNJE("Srednje"),
    CESTO("Često"),
    PRODUKTIVNI("Produktivni"),
    INTENZIVNO("Intenzivno"),
    VISOKA("Visoka"),
    JAKA("Jaka");

    private String vrijednost;

    VrijednostiSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}
