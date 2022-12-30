package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.sort.CovidSorter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class NajviseZarazenihNit implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Zupanija zup = BazaPodataka.dohvatiZupanije().stream().max(new CovidSorter()).get();
                double zarazenost = (double) zup.getBrojZarazenih() / (double)zup.getBrojStanovnika() * 100;
                System.out.println("Najviše zaraženih ima u županiji " + zup.getNaziv() + " od " + zarazenost + "%");
            } catch (SQLException | IOException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom ispisa županije sa najvećim postotkom zaraženih u konzolu. ", iznimka);
            }

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
            }
            catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Pogreška kod ispisivanja županije sa najvećim postotkom zaraženih u konzolu.", iznimka);
            }
        }
    }
}
