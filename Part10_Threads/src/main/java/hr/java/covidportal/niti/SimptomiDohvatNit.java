package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SimptomiDohvatNit implements Callable {

    /**
     * Dohvaća simptome iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @throws SQLException
     * @throws IOException
     */
    public synchronized List<Simptom> dohvatiSimptome() throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Dohvaćanje simptoma, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom dohvaćanja simptoma iz baze podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        List<Simptom> listaSimptoma = new ArrayList<>();

        Statement upit = veza.createStatement();
        ResultSet operacija = upit.executeQuery("SELECT * FROM SIMPTOM");
        while (operacija.next()){
            long id = operacija.getLong("ID");
            String naziv = operacija.getString("NAZIV");
            String vrijednost = operacija.getString("VRIJEDNOST");
            Simptom simpt = new Simptom(id, naziv, vrijednost);
            listaSimptoma.add(simpt);
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
        return listaSimptoma;
    }

    @Override
    public Object call() throws Exception {
        return dohvatiSimptome();
    }
}
