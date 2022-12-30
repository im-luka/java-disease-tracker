package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class SimptomiOdredeniNit implements Callable {

    private long id;

    public SimptomiOdredeniNit(long id) {
        this.id = id;
    }

    /**
     * Dohvaća određeni simptom pomoću id-a iz baze podataka.
     *
     * @return željeni simptom
     * @throws SQLException
     * @throws IOException
     */
    public synchronized Simptom dohvatiOdredeniSimptom() throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaZaDohvatInstanci() == true) {
            try {
                System.out.println("Dohvaćanje određenog simptoma, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom dohvaćanja određenog simptoma iz baze podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaZaDohvatInstanci(true);

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE ID=?");
        upit.setLong(1, id);
        ResultSet operacija = upit.executeQuery();

        Simptom simpt = null;
        while (operacija.next()) {
            long idS = operacija.getLong("ID");
            String naziv = operacija.getString("NAZIV");
            String vrijednost = operacija.getString("VRIJEDNOST");
            simpt = new Simptom(idS, naziv, vrijednost);
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaZaDohvatInstanci(false);
        notifyAll();
        return simpt;
    }

    @Override
    public Object call() throws Exception {
        return dohvatiOdredeniSimptom();
    }
}
