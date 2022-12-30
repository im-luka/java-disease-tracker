package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ZupanijeDodajNit implements Runnable {

    private Zupanija zupanija;

    public ZupanijeDodajNit(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    /**
     * Sprema unesenu županiju iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param zup županija koja će se spremiti u bazu podataka
     * @throws SQLException
     * @throws IOException
     */
    public synchronized void spremiZupanijuUBazu(Zupanija zup) throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Spremanje županije, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom spremanja županije u bazu podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        PreparedStatement upit = veza.prepareStatement("INSERT INTO ZUPANIJA(NAZIV,BROJ_STANOVNIKA,BROJ_ZARAZENIH_STANOVNIKA) VALUES(?,?,?)");
        upit.setString(1, zup.getNaziv());
        upit.setInt(2, zup.getBrojStanovnika());
        upit.setInt(3, zup.getBrojZarazenih());

        upit.executeUpdate();
        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
    }

    @Override
    public void run() {
        try {
            spremiZupanijuUBazu(zupanija);
        }
        catch (SQLException | IOException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom spremanja županije u bazu podataka! ", iznimka);
        }
    }
}
