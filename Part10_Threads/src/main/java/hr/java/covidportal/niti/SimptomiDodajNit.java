package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimptomiDodajNit implements Runnable{

    private Simptom simptom;

    public SimptomiDodajNit(Simptom simptom) {
        this.simptom = simptom;
    }

    /**
     * Sprema uneseni simptom iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param simpt simptom koji će se spremiti u bazu podataka
     * @throws SQLException
     * @throws IOException
     */
    public synchronized void spremiSimptomUBazu(Simptom simpt) throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Spremanje simptoma, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom spremanja simptoma u bazu podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        PreparedStatement upit = veza.prepareStatement("INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES(?, ?)");
        upit.setString(1, simpt.getNaziv());
        upit.setString(2, simpt.getVrijednost());

        upit.executeUpdate();
        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
    }

    @Override
    public void run() {
        try {
            spremiSimptomUBazu(simptom);
        }
        catch (SQLException | IOException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom spremanja simptoma u bazu podataka! ", iznimka);
        }
    }
}
