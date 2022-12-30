package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class BazaPodataka {

    /**
     * Sadrži putanju do baze podataka.
     */
    private static final String PRISTUPNI_PODACI_BAZE_PODATAKA = "src\\main\\resources\\pristupniPodaci.properties";

    /**
     * Služi za povezivanje sa bazom podataka.
     *
     * @return vraća vezu sa bazom podataka
     * @throws SQLException
     * @throws IOException
     */
    public static Connection povezivanjeSaBazomPodataka() throws SQLException, IOException {
        Properties property = new Properties();
        property.load(new FileReader(PRISTUPNI_PODACI_BAZE_PODATAKA));

        String bazaPodatakaURL = property.getProperty("bazaPodatakaURL");
        String korisnickoIme = property.getProperty("korisnickoIme");
        String lozinka = property.getProperty("lozinka");

        Connection veza = DriverManager.getConnection(bazaPodatakaURL, korisnickoIme, lozinka);

        return veza;
    }

    /**
     * Služi za zatvaranje veze sa bazom podataka.
     *
     * @param veza veza za bazom podataka
     * @throws SQLException
     */
    public static void zatvaranjeBazePodataka(Connection veza) throws SQLException {
        veza.close();
    }

    /**
     * Dohvaća simptome iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @param listaSimptoma lista simptoma u koju se spremaju simptomi iz baze podataka
     * @throws SQLException
     * @throws IOException
     */
    public static void dohvatiSimptome(List<Simptom> listaSimptoma) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

        Statement upit = veza.createStatement();
        ResultSet operacija = upit.executeQuery("SELECT * FROM SIMPTOM");
        while (operacija.next()){
            long id = operacija.getLong("ID");
            String naziv = operacija.getString("NAZIV");
            String vrijednost = operacija.getString("VRIJEDNOST");
            Simptom simpt = new Simptom(id, naziv, vrijednost);
            listaSimptoma.add(simpt);
        }

        zatvaranjeBazePodataka(veza);
    }

    /**
     * Dohvaća određeni simptom pomoću id-a iz baze podataka.
     *
     * @param id id simptoma koji će se dohvatiti
     * @return željeni simptom
     * @throws SQLException
     * @throws IOException
     */
    public static Simptom dohvatiOdredeniSimptom(long id) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

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

        zatvaranjeBazePodataka(veza);
        return simpt;
    }

    /**
     * Sprema uneseni simptom iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param simpt simptom koji će se spremiti u bazu podataka
     * @throws SQLException
     * @throws IOException
     */
    public static void spremiSimptomUBazu(Simptom simpt) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

        PreparedStatement upit = veza.prepareStatement("INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES(?, ?)");
        upit.setString(1, simpt.getNaziv());
        upit.setString(2, simpt.getVrijednost());

        upit.executeUpdate();
        zatvaranjeBazePodataka(veza);
    }

    /**
     * Dohvaća bolesti i viruse iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @param listaBolesti lista bolesti u koju se spremaju sve bolesti i virusi iz baze podataka
     * @param listaVirusa lista virusa u koju se spremaju virusi iz baze podataka
     * @throws SQLException
     * @throws IOException
     */
    public static void dohvatiBolestVirus(List<Bolest> listaBolesti, List<Virus> listaVirusa) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

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
                    pomocnaListaSimptoma.add(dohvatiOdredeniSimptom(simptomID));
                }
            }

            if (jeLiVirus == true) {
                Virus vir = new Virus(id, naziv, true, pomocnaListaSimptoma);
                listaVirusa.add(vir);
            }
            Bolest bol = new Bolest(id, naziv, jeLiVirus, pomocnaListaSimptoma);
            listaBolesti.add(bol);
        }

        zatvaranjeBazePodataka(veza);
    }

    /**
     * Dohvaća određenu bolesti ili virus pomoću id-a iz baze podataka.
     *
     * @param id id bolesti ili virusa koja će se dohvatiti
     * @return željena bolest ili virus
     * @throws SQLException
     * @throws IOException
     */
    public static Bolest dohvatiOdredenuBolestVirus(long id) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

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
                    pomocnaListaSimptoma.add(dohvatiOdredeniSimptom(simptomID));
                }
            }

            bol = new Bolest(idB, naziv, jeLiVirus, pomocnaListaSimptoma);
        }

        zatvaranjeBazePodataka(veza);
        return bol;
    }

    /**
     * Sprema unesenu bolest ili virus iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param bol bolest ili virus koja će se spremiti u bazu podataka
     * @param jeLiVirus varijabla koja govori radi li se o bolesti ili virusu
     * @throws SQLException
     * @throws IOException
     */
    public static void spremiBolestUBazu(Bolest bol, Boolean jeLiVirus) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

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

        zatvaranjeBazePodataka(veza);
    }

    /**
     * Dohvaća županije iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @param listaZupanija lista županija u koju se spremaju županije iz baze podataka
     * @throws SQLException
     * @throws IOException
     */
    public static void dohvatiZupanije(List<Zupanija> listaZupanija) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

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

        zatvaranjeBazePodataka(veza);
    }

    /**
     * Dohvaća određenu županiju pomoću id-a iz baze podataka.
     *
     * @param id id županije koja će se dohvatiti
     * @return željena županija
     * @throws SQLException
     * @throws IOException
     */
    public static Zupanija dohvatiOdredenuZupaniju(long id) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

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

        zatvaranjeBazePodataka(veza);
        return zup;
    }

    /**
     * Sprema unesenu županiju iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param zup županija koja će se spremiti u bazu podataka
     * @throws SQLException
     * @throws IOException
     */
    public static void spremiZupanijuUBazu(Zupanija zup) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

        PreparedStatement upit = veza.prepareStatement("INSERT INTO ZUPANIJA(NAZIV,BROJ_STANOVNIKA,BROJ_ZARAZENIH_STANOVNIKA) VALUES(?,?,?)");
        upit.setString(1, zup.getNaziv());
        upit.setInt(2, zup.getBrojStanovnika());
        upit.setInt(3, zup.getBrojZarazenih());

        upit.executeUpdate();
        zatvaranjeBazePodataka(veza);
    }

    /**
     * Dohvaća osobe iz baze podataka te ih sprema u listu koja će se prikazati u korisničkom sučelju aplikacije.
     *
     * @param listaOsoba lista osoba u koju se spremaju osobe iz baze podataka
     * @throws SQLException
     * @throws IOException
     */
    public static void dohvatiOsobe(List<Osoba> listaOsoba) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

        Statement upit = veza.createStatement();
        ResultSet operacija = upit.executeQuery("SELECT * FROM OSOBA");
        while (operacija.next()) {
            long id = operacija.getLong("ID");
            String ime = operacija.getString("IME");
            String prezime = operacija.getString("PREZIME");
            LocalDate datumRod = operacija.getObject("DATUM_RODJENJA", LocalDate.class);
            long zupanijaID = operacija.getLong("ZUPANIJA_ID");
            long bolestID = operacija.getLong("BOLEST_ID");

            Zupanija zupanija = dohvatiOdredenuZupaniju(zupanijaID);
            Bolest bolest = dohvatiOdredenuBolestVirus(bolestID);

            List<Osoba> kontakti = new ArrayList<>();
            Osoba os = new Osoba.BuilderOsobe(id, ime, prezime)
                    .osStarost(datumRod)
                    .osZupanija(zupanija)
                    .osZarazenBolescu(bolest)
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

        zatvaranjeBazePodataka(veza);
    }

    /**
     * Dohvaća određenu osobu pomoću id-a iz baze podataka.
     *
     * @param id id osobe koja će se dohvatiti
     * @return željena osoba
     * @throws SQLException
     * @throws IOException
     */
    public static Osoba dohvatiOdredenuOsobu(long id) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM OSOBA WHERE ID=?");
        upit.setLong(1, id);
        ResultSet operacija = upit.executeQuery();

        Osoba osoba = null;
        while (operacija.next()) {
            long idOs = operacija.getLong("ID");
            String ime = operacija.getString("IME");
            String prezime = operacija.getString("PREZIME");
            LocalDate datumRod = operacija.getObject("DATUM_RODJENJA", LocalDate.class);
            long zupanijaID = operacija.getLong("ZUPANIJA_ID");
            long bolestID = operacija.getLong("BOLEST_ID");

            Zupanija zupanija = dohvatiOdredenuZupaniju(zupanijaID);
            Bolest bolest = dohvatiOdredenuBolestVirus(bolestID);

            List<Osoba> kontaktiraneOsobe = new ArrayList<>();
            Statement podUpit = veza.createStatement();
            ResultSet podOperacija = podUpit.executeQuery("SELECT * FROM KONTAKTIRANE_OSOBE");
            while (podOperacija.next()) {
                long osobaID = podOperacija.getLong("OSOBA_ID");
                long kontaktID = podOperacija.getLong("KONTAKTIRANA_OSOBA_ID");
                if (idOs == osobaID) {
                    for(Osoba os : PretragaOsobaController.getObservableListaOsoba().stream().collect(Collectors.toList())) {
                        if (os.getId() == kontaktID) {
                            kontaktiraneOsobe.add(os);
                        }
                    }
                }
            }

            osoba = new Osoba.BuilderOsobe(idOs, ime, prezime)
                    .osStarost(datumRod)
                    .osZupanija(zupanija)
                    .osZarazenBolescu(bolest)
                    .osKontaktiraneOsobe(kontaktiraneOsobe)
                    .build();
        }

        zatvaranjeBazePodataka(veza);
        return osoba;
    }

    /**
     * Sprema unesenu osobu iz korisničkog sučelja aplikacije u bazu podataka.
     *
     * @param osoba osoba koja će se spremiti u bazu podataka
     * @throws SQLException
     * @throws IOException
     */
    public static void spremiOsobuUBazu(Osoba osoba) throws SQLException, IOException {
        Connection veza = povezivanjeSaBazomPodataka();

        PreparedStatement upit = veza.prepareStatement("INSERT INTO OSOBA(IME,PREZIME,DATUM_RODJENJA,ZUPANIJA_ID,BOLEST_ID) VALUES(?,?,?,?,?)");
        upit.setString(1, osoba.getIme());
        upit.setString(2, osoba.getPrezime());
        upit.setObject(3, osoba.getStarostDatum());
        upit.setLong(4, osoba.getZupanija().getId());
        upit.setLong(5, osoba.getZarazenBolescu().getId());
        upit.executeUpdate();

        long id = -1;
        Statement nadiID = veza.createStatement();
        ResultSet operacija = nadiID.executeQuery("SELECT * FROM OSOBA");
        while (operacija.next()) {
            id = operacija.getLong("ID");
        }

        PreparedStatement drugiUpit = veza.prepareStatement("INSERT INTO KONTAKTIRANE_OSOBE(OSOBA_ID,KONTAKTIRANA_OSOBA_ID) VALUES(?,?)");
        for (Osoba os : osoba.getKontaktiraneOsobe()) {
            drugiUpit.setLong(1, id);
            drugiUpit.setLong(2, os.getId());
            drugiUpit.executeUpdate();
        }

        zatvaranjeBazePodataka(veza);
    }
}
