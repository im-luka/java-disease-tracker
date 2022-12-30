package main.java.hr.java.covidportal.iznimke;

/**
 * Naslijeđuje <code>Exception</code> iznimke i služi kao vlastita iznimka koju ćemo bacati u glavni program
 * pomoću određene metode.
 */
public class OgranicenjeOsoba extends Exception {
    /**
     * Prima poruku koja se prosljeđuje nadklasi.
     *
     * @param message poruka koja se prosljeđuje
     */
    public OgranicenjeOsoba(String message){
        super(message);
    }

    /**
     * Prima uzrok koji se prosljeđuje nadklasi
     *
     * @param cause uzrok koji se prosljeđuje
     */
    public OgranicenjeOsoba(Throwable cause){
        super(cause);
    }

    /**
     * Prima poruku i uzrok koji se prosljeđuju nadklasi
     *
     * @param message poruka koja se prosljeđuje
     * @param cause uzrok koji se prosljeđuje
     */
    public OgranicenjeOsoba(String message, Throwable cause){
        super(message, cause);
    }
}
