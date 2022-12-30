package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class ZupanijeOdredeneNit implements Callable {

    private long id;

    public ZupanijeOdredeneNit(long id) {
        this.id = id;
    }

    /**
     * Dohvaća određenu županiju pomoću id-a iz baze podataka.
     *
     * @return željena županija
     * @throws SQLException
     * @throws IOException
     */
    public synchronized Zupanija dohvatiOdredenuZupaniju() throws SQLException, IOException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaZaDohvatInstanci() == true) {
            try {
                System.out.println("Dohvaćanje određene županije, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom dohvaćanja određene županije iz baze podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaZaDohvatInstanci(true);

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM ZUPANIJA WHERE ID=?");
        upit.setLong(1, id);
        ResultSet operacija = upit.executeQuery();

        Zupanija zup = null;
        while (operacija.next()) {
            long idZ = operacija.getLong("ID");
            String naziv = operacija.getString("NAZIV");
            Integer brStanovnika = operacija.getInt("BROJ_STANOVNIKA");
            Integer brZarazenih = operacija.getInt("BROJ_ZARAZENIH_STANOVNIKA");
            zup = new Zupanija(idZ, naziv, brStanovnika, brZarazenih);
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaZaDohvatInstanci(false);
        notifyAll();
        return zup;
    }

    @Override
    public Object call() throws Exception {
        return dohvatiOdredenuZupaniju();
    }
}
