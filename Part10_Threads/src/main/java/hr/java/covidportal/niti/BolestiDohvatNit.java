package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BolestiDohvatNit implements Callable {

    /**
     * Dohvaća bolesti i viruse iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @throws SQLException
     * @throws IOException
     */
    public synchronized List<Bolest> dohvatiBolestVirus() throws SQLException, IOException, ExecutionException, InterruptedException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Dohvaćanje bolesti, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom dohvaćanja bolesti iz baze podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        List<Bolest> listaBolesti = new ArrayList<>();

        Statement upit = veza.createStatement();
        ResultSet operacija  = upit.executeQuery("SELECT * FROM BOLEST");
        while (operacija.next()) {
            long id = operacija.getLong("ID");
            String naziv = operacija.getString("NAZIV");
            Boolean jeLiVirus = operacija.getBoolean("VIRUS");

            List<Simptom> pomocnaListaSimptoma = new ArrayList<>();
            Statement podUpit = veza.createStatement();
            ResultSet podOperacija = podUpit.executeQuery("SELECT * FROM BOLEST_SIMPTOM");
            while (podOperacija.next()) {
                long bolestID = podOperacija.getLong("BOLEST_ID");
                long simptomID = podOperacija.getLong("SIMPTOM_ID");
                if (id == bolestID) {
                    ExecutorService servis = Executors.newCachedThreadPool();
                    Future<Simptom> simptom = servis.submit(new SimptomiOdredeniNit(simptomID));
                    pomocnaListaSimptoma.add(simptom.get());
                }
            }

            Bolest bol = new Bolest(id, naziv, jeLiVirus, pomocnaListaSimptoma);
            listaBolesti.add(bol);
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
        return listaBolesti;
    }

    @Override
    public Object call() throws Exception {
        return dohvatiBolestVirus();
    }
}
