package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ZupanijeDohvatNit implements Callable {

    /**
     * Dohvaća županije iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @throws SQLException
     * @throws IOException
     */
    public synchronized List<Zupanija> dohvatiZupanije() throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Dohvaćanje županije, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom dohvaćanja županije iz baze podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        List<Zupanija> listaZupanija = new ArrayList<>();

        Statement upit = veza.createStatement();
        ResultSet operacija = upit.executeQuery("SELECT * FROM ZUPANIJA");
        while (operacija.next()){
            long id = operacija.getLong("ID");
            String naziv = operacija.getString("NAZIV");
            Integer brStanovnika = operacija.getInt("BROJ_STANOVNIKA");
            Integer brZarazenih = operacija.getInt("BROJ_ZARAZENIH_STANOVNIKA");

            Zupanija zup = new Zupanija(id, naziv, brStanovnika, brZarazenih);
            listaZupanija.add(zup);
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
        return listaZupanija;
    }

    @Override
    public Object call() throws Exception {
        return dohvatiZupanije();
    }
}
