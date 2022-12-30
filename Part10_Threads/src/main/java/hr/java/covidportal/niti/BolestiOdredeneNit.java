package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BolestiOdredeneNit implements Callable {

    private long id;

    public BolestiOdredeneNit(long id) {
        this.id = id;
    }

    /**
     * Dohvaća određenu bolesti ili virus pomoću id-a iz baze podataka.
     *
     * @return željena bolest ili virus
     * @throws SQLException
     * @throws IOException
     */
    public synchronized Bolest dohvatiOdredenuBolestVirus() throws SQLException, IOException, ExecutionException, InterruptedException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        BazaPodataka.dohvatiBolestVirus();
        while (BazaPodataka.isAktivnaVezaZaDohvatInstanci() == true) {
            try {
                System.out.println("Dohvaćanje određene bolesti, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom dohvaćanja određene bolesti iz baze podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaZaDohvatInstanci(true);

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM BOLEST WHERE ID=?");
        upit.setLong(1, id);
        ResultSet operacija = upit.executeQuery();

        Bolest bol = null;
        while (operacija.next()) {
            long idB = operacija.getLong("ID");
            String naziv = operacija.getString("NAZIV");
            Boolean jeLiVirus = operacija.getBoolean("VIRUS");

            List<Simptom> pomocnaListaSimptoma = new ArrayList<>();
            Statement podUpit = veza.createStatement();
            ResultSet podOperacija = podUpit.executeQuery("SELECT * FROM BOLEST_SIMPTOM");
            while (podOperacija.next()) {
                long bolestID = podOperacija.getLong("BOLEST_ID");
                long simptomID = podOperacija.getLong("SIMPTOM_ID");
                if (idB == bolestID) {
                    BazaPodataka.setAktivnaVezaZaDohvatInstanci(false);
                    ExecutorService servis = Executors.newCachedThreadPool();
                    Future<Simptom> simptom = servis.submit(new SimptomiOdredeniNit(simptomID));
                    pomocnaListaSimptoma.add(simptom.get());
                    BazaPodataka.setAktivnaVezaZaDohvatInstanci(true);
                }
            }

            bol = new Bolest(idB, naziv, jeLiVirus, pomocnaListaSimptoma);
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaZaDohvatInstanci(false);
        notifyAll();
        return bol;
    }

    @Override
    public Object call() throws Exception {
        return dohvatiOdredenuBolestVirus();
    }
}
