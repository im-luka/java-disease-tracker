package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.main.BazaPodataka;
import main.java.hr.java.covidportal.main.PocetniEkranController;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class OsobeDohvatNit implements Callable {

    /**
     * Dohvaća osobe iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @throws SQLException
     * @throws IOException
     */
    public synchronized List<Osoba> dohvatiOsobe() throws SQLException, IOException, ExecutionException, InterruptedException {
        Connection veza = BazaPodataka.povezivanjeSaBazomPodataka();
        while (BazaPodataka.isAktivnaVezaSaBazomPodataka() == true) {
            try {
                System.out.println("Dohvaćanje osobe, čekam...");
                wait();
            } catch (InterruptedException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom dohvaćanja osobe iz baze podataka! ", iznimka);
            }
        }

        BazaPodataka.setAktivnaVezaSaBazomPodataka(true);

        List<Osoba> listaOsoba = new ArrayList<>();

        Statement upit = veza.createStatement();
        ResultSet operacija = upit.executeQuery("SELECT * FROM OSOBA");
        while (operacija.next()) {
            long id = operacija.getLong("ID");
            String ime = operacija.getString("IME");
            String prezime = operacija.getString("PREZIME");
            LocalDate datumRod = operacija.getObject("DATUM_RODJENJA", LocalDate.class);
            long zupanijaID = operacija.getLong("ZUPANIJA_ID");
            long bolestID = operacija.getLong("BOLEST_ID");

            ExecutorService servis = Executors.newCachedThreadPool();
            Future<Zupanija> zupanija = servis.submit(new ZupanijeOdredeneNit(zupanijaID));
            Future<Bolest> bolest = servis.submit(new BolestiOdredeneNit(bolestID));

            List<Osoba> kontakti = new ArrayList<>();
            Osoba os = new Osoba.BuilderOsobe(id, ime, prezime)
                    .osStarost(datumRod)
                    .osZupanija(zupanija.get())
                    .osZarazenBolescu(bolest.get())
                    .osKontaktiraneOsobe(kontakti)
                    .build();
            listaOsoba.add(os);
        }

        Statement upitKontakti = veza.createStatement();
        ResultSet operacijaKontakti = upitKontakti.executeQuery("SELECT * FROM KONTAKTIRANE_OSOBE");
        while (operacijaKontakti.next()) {
            long osobaID = operacijaKontakti.getLong("OSOBA_ID");
            long kontaktID = operacijaKontakti.getLong("KONTAKTIRANA_OSOBA_ID");
            for (Osoba osoba : listaOsoba) {
                if (osoba.getId() == osobaID) {
                    for (Osoba kontakti : listaOsoba) {
                        if (kontakti.getId() == kontaktID) {
                            osoba.getKontaktiraneOsobe().add(kontakti);
                        }
                    }
                }
            }
        }

        BazaPodataka.zatvaranjeBazePodataka(veza);

        BazaPodataka.setAktivnaVezaSaBazomPodataka(false);
        notifyAll();
        return listaOsoba;
    }

    @Override
    public Object call() throws Exception {
        return dohvatiOsobe();
    }
}
