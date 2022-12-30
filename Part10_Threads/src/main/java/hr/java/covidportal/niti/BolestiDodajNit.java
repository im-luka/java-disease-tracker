package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.*;

public class BolestiDodajNit implements Runnable {

    private Bolest bolest;
    private boolean jeLiVirus;

    public BolestiDodajNit(Bolest bolest, boolean jeLiVirus) {
        this.bolest = bolest;
        this.jeLiVirus = jeLiVirus;
    }

    /**
     * Sprema unesenu bolest ili virus iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param bol bolest ili virus koja će se spremiti u bazu podataka
     * @param jeLiVirus varijabla koja govori radi li se o bolesti ili virusu
     * @throws SQLException
     * @throws IOException
     */
    public synchronized void spremiBolestUBazu(Bolest bol, boolean jeLiVirus) throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Spremanje bolesti, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom spremanja bolesti u bazu podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        PreparedStatement upit = veza.prepareStatement("INSERT INTO BOLEST(NAZIV,VIRUS) VALUES(?, ?)");
        upit.setString(1, bol.getNaziv());
        upit.setBoolean(2, jeLiVirus);
        upit.executeUpdate();

        long id = 99;
        Statement traziID = veza.createStatement();
        ResultSet operacija = traziID.executeQuery("SELECT * FROM BOLEST");
        while (operacija.next()) {
            id = operacija.getLong("ID");
        }

        PreparedStatement drugiUpit = veza.prepareStatement("INSERT INTO BOLEST_SIMPTOM(BOLEST_ID,SIMPTOM_ID) VALUES (?, ?)");
        for (Simptom simpt : bol.getSimptomi()) {
            drugiUpit.setLong(1, id);
            drugiUpit.setLong(2, simpt.getId());
            drugiUpit.executeUpdate();
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
    }

    @Override
    public void run() {
        try {
            spremiBolestUBazu(bolest, jeLiVirus);
        }
        catch (SQLException | IOException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom spremanja bolesti u bazu podataka! ", iznimka);
        }
    }
}
