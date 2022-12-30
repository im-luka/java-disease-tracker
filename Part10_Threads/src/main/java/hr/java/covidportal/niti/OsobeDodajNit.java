package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Osoba;

import java.io.IOException;
import java.sql.*;

public class OsobeDodajNit implements Runnable {

    private Osoba osoba;

    public OsobeDodajNit(Osoba osoba) {
        this.osoba = osoba;
    }

    /**
     * Sprema unesenu osobu iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param osoba osoba koja će se spremiti u bazu podataka
     * @throws SQLException
     * @throws IOException
     */
    public synchronized void spremiOsobuUBazu(Osoba osoba) throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Spremanje osobe, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom spremanja osobe u bazu podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        PreparedStatement upit = veza.prepareStatement("INSERT INTO OSOBA(IME,PREZIME,DATUM_RODJENJA,ZUPANIJA_ID,BOLEST_ID) VALUES(?,?,?,?,?)");
        upit.setString(1, osoba.getIme());
        upit.setString(2, osoba.getPrezime());
        upit.setObject(3, osoba.getStarostDatum());
        upit.setLong(4, osoba.getZupanija().getId());
        upit.setLong(5, osoba.getZarazenBolescu().getId());
        upit.executeUpdate();

        long id = -1;
        Statement nadiID = veza.createStatement();
        ResultSet operacija = nadiID.executeQuery("SELECT * FROM OSOBA");
        while (operacija.next()) {
            id = operacija.getLong("ID");
        }

        PreparedStatement drugiUpit = veza.prepareStatement("INSERT INTO KONTAKTIRANE_OSOBE(OSOBA_ID,KONTAKTIRANA_OSOBA_ID) VALUES(?,?)");
        for (Osoba os : osoba.getKontaktiraneOsobe()) {
            drugiUpit.setLong(1, id);
            drugiUpit.setLong(2, os.getId());
            drugiUpit.executeUpdate();
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
    }

    @Override
    public void run() {
        try {
            spremiOsobuUBazu(osoba);
        }
        catch (SQLException | IOException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom spremanja osobe u bazu podataka! ", iznimka);
        }
    }
}
