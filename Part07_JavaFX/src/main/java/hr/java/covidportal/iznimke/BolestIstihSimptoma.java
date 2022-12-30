package main.java.hr.java.covidportal.iznimke;

/**
 * Nasljeđuje <code>RuntimeException</code> iznimke i služi kao vlastita iznimka koju ćemo bacati u glavni program
 * pomoću određene metode.
 */
public class BolestIstihSimptoma extends RuntimeException {
    /**
     * Prima poruku koja se prosljeđuje nadklasi.
     *
     * @param message poruka koja se prosljeđuje
     */
    public BolestIstihSimptoma(String message){
        super(message);
    }

    /**
     * Prima uzrok koji se prosljeđuje nadklasi
     *
     * @param cause uzrok koji se prosljeđuje
     */
    public BolestIstihSimptoma(Throwable cause){
        super(cause);
    }

    /**
     * Prima poruku i uzrok koji se prosljeđuju nadklasi
     *
     * @param message poruka koja se prosljeđuje
     * @param cause uzrok koji se prosljeđuje
     */
    public BolestIstihSimptoma(String message, Throwable cause){
        super(message, cause);
    }
}
