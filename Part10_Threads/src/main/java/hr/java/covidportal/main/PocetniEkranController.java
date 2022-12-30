package main.java.hr.java.covidportal.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.java.hr.java.covidportal.enums.VrijednostiSimptoma;
import main.java.hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import main.java.hr.java.covidportal.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PocetniEkranController implements Initializable {

    public static final Logger logger = LoggerFactory.getLogger(PocetniEkranController.class);

    /**
     * Prikazuje ekran za pretragu županija.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Parent pretragaZupanijaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    /**
     * Prikazuje ekran za dodavanje županije.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaDodavanjeZupanije() throws IOException {
        Parent dodavanjeZupanijeFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeZupanije.fxml"));
        Scene dodavanjeZupanijeScene = new Scene(dodavanjeZupanijeFrame);
        Main.getMainStage().setScene(dodavanjeZupanijeScene);
    }

    /**
     * Prikazuje ekran za pretragu simptoma.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Parent pretragaSimptomaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    /**
     * Prikazuje ekran za dodavanje simptoma.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaDodavanjeSimptoma() throws IOException {
        Parent dodavanjeSimptomaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeSimptoma.fxml"));
        Scene dodavanjeSimptomaScene = new Scene(dodavanjeSimptomaFrame);
        Main.getMainStage().setScene(dodavanjeSimptomaScene);
    }

    /**
     * Prikazuje ekran za pretragu bolesti.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Parent pretragaBolestiFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    /**
     * Prikazuje ekran za dodavanje bolesti.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaDodavanjeBolesti() throws IOException {
        Parent dodavanjeBolestiFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeBolesti.fxml"));
        Scene dodavanjeBolestiScene = new Scene(dodavanjeBolestiFrame);
        Main.getMainStage().setScene(dodavanjeBolestiScene);
    }

    /**
     * Prikazuje ekran za pretragu virusa.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Parent pretragaVirusaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    /**
     * Prikazuje ekran za dodavanje virusa.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaDodavanjeVirusa() throws IOException {
        Parent dodavanjeVirusaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeVirusa.fxml"));
        Scene dodavanjeVirusaScene = new Scene(dodavanjeVirusaFrame);
        Main.getMainStage().setScene(dodavanjeVirusaScene);
    }

    /**
     * Prikazuje ekran za pretragu osoba.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Parent pretragaOsobaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    /**
     * Prikazuje ekran za dodavanje osoba.
     *
     * @throws IOException
     */
    @FXML
    public void prikaziEkranZaDodavanjeOsoba() throws IOException {
        Parent dodavanjeOsobaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeOsobe.fxml"));
        Scene dodavanjeOsobaScene = new Scene(dodavanjeOsobaFrame);
        Main.getMainStage().setScene(dodavanjeOsobaScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Služi za čitanje podataka o županijama iz datoteke.
     *
     * @param zupanijeLista lista županija u koji se spremaju podaci iz datoteke
     */
    public static void citanjeDatotekeZupanije(List<Zupanija> zupanijeLista) {
        File zupanijeFile = new File("dat/zupanije.txt");
        try(FileReader zupReader = new FileReader(zupanijeFile);
            BufferedReader reader = new BufferedReader(zupReader))
        {
            String procitanaLinija;
            while ((procitanaLinija = reader.readLine()) != null){
                Long id = Long.parseLong(procitanaLinija);
                String naziv = reader.readLine();
                Integer brStanovnika = Integer.parseInt(reader.readLine());
                Integer brZarazenih = Integer.parseInt(reader.readLine());
                Zupanija zup = new Zupanija(id, naziv, brStanovnika, brZarazenih);
                zupanijeLista.add(zup);
                logger.info("Uspješno pročitana " + zup.getId() + " županija i ubačena u set županija!");
            }
            System.out.println("Učitavanje podataka o županijama...");
            logger.info("Uspješno učitane županije iz datoteke!");
        }
        catch (IOException iznimka){
            System.out.println("Greška kod čitanja županija iz datoteke!");
            logger.error("Greška kod čitanja županija iz datoteke! " + iznimka);
        }
    }

    /**
     * Služi za čitanje podataka o simptomima iz datoteke.
     *
     * @param simptomiLista lista simptoma u koji se spremaju podaci iz datoteke
     */
    public static void citanjeDatotekeSimptomi(List<Simptom> simptomiLista) {
        File simptomiFile = new File("dat/simptomi.txt");
        try(FileReader simpReader = new FileReader(simptomiFile);
            BufferedReader reader = new BufferedReader(simpReader))
        {
            String procitanaLinija;
            while ((procitanaLinija = reader.readLine()) != null){
                Long id = Long.parseLong(procitanaLinija);
                String naziv = reader.readLine();
                String vrij = reader.readLine();
                VrijednostiSimptoma.valueOf(vrij);
                Simptom simpt = new Simptom(id, naziv, vrij);
                simptomiLista.add(simpt);
                logger.info("Uspješno pročitan " + simpt.getId() + " simptom i ubačen u set simptoma!");
            }
            System.out.println("Učitavanje podataka o simptomima...");
            logger.info("Uspješno učitani simptomi iz datoteke!");
        }
        catch (IOException iznimka){
            System.out.println("Greška kod čitanja simptoma iz datoteke!");
            logger.error("Greška kod čitanja simptoma iz datoteke! " + iznimka);
        }
    }

    /**
     * Služi za čitanje podataka o bolestima iz datoteke.
     *
     * @param simptomiLista lista simptoma potrebnih za odabir simptoma neke bolesti
     * @param bolestiLista lista bolesti u koji se spremaju podaci iz datoteke
     */
    public static void citanjeDatotekeBolesti(List<Simptom> simptomiLista, List<Bolest> bolestiLista) {
        File bolestiFile = new File("dat/bolesti.txt");
        try(FileReader bolReader = new FileReader(bolestiFile);
            BufferedReader reader = new BufferedReader(bolReader))
        {
            String procitanaLinija;
            while ((procitanaLinija = reader.readLine()) != null){
                Long id = Long.parseLong(procitanaLinija);
                String naziv = reader.readLine();

                String[] delimiter = reader.readLine().split(",");
                List<Simptom> listaPomoc = new ArrayList<>();
                for(int i = 0; i < delimiter.length; i++){
                    int xvar = Integer.parseInt(delimiter[i]);
                    listaPomoc.add(simptomiLista.get(xvar-1));
                }
                Bolest bol = new Bolest(id, naziv, false, listaPomoc);
                bolestiLista.add(bol);
            }
            System.out.println("Učitavanje podataka o bolestima...");
            logger.info("Uspješno učitane bolesti iz datoteke!");
        }
        catch (IOException iznimka){
            System.out.println("Greška kod čitanja bolesti iz datoteke!");
            logger.error("Greška kod čitanja bolesti iz datoteke! " + iznimka);
        }
    }

    /**
     * Služi za čitanje podataka o virusima iz datoteke.
     *
     * @param simptomiLista lista simptoma potrebnih za odabir simptoma nekog virusa
     * @param bolestiLista lista bolesti u koji se spremaju podaci iz datoteke
     */
    public static void citanjeDatotekeVirusi(List<Simptom> simptomiLista, List<Bolest> bolestiLista, List<Virus> virusiLista) {
        File virusiFile = new File("dat/virusi.txt");
        try(FileReader virReader = new FileReader(virusiFile);
            BufferedReader reader = new BufferedReader(virReader))
        {
            String procitanaLinija;
            while ((procitanaLinija = reader.readLine()) != null){
                int id = bolestiLista.size() + 1;
                Long idV = Long.parseLong(procitanaLinija);
                String naziv = reader.readLine();

                String[] delimiter = reader.readLine().split(",");
                List<Simptom> listaPomoc = new ArrayList<>();
                for(int i = 0; i < delimiter.length; i++){
                    int xvar = Integer.parseInt(delimiter[i]);
                    listaPomoc.add(simptomiLista.get(xvar-1));
                }
                Virus vir = new Virus((long)id, naziv, true, listaPomoc);
                Virus virV = new Virus(idV, naziv, true, listaPomoc);
                bolestiLista.add(vir);
                virusiLista.add(virV);
            }
            System.out.println("Učitavanje podataka o virusima...");
            logger.info("Uspješno učitani virusi iz datoteke!");
        }
        catch (IOException iznimka){
            System.out.println("Greška kod čitanja virusa iz datoteke!");
            logger.error("Greška kod čitanja virusa iz datoteke! " + iznimka);
        }
    }

    /**
     * Služi za čitanje podataka o osobama iz datoteke.
     *
     * @param zupanijeLista lista županija potrebnih za odabir županije pojedine osobe
     * @param bolestiLista lista bolesti potrebnih za odabir bolesti pojedine osobe
     * @param osobeLista lista osoba potrebnih za odabir kontaktiranih osoba pojedine osobe
     */
    public static void citanjeDatotekeOsobe(List<Zupanija> zupanijeLista, List<Bolest> bolestiLista, List<Osoba> osobeLista) {
        File osobeFile = new File("dat/osobe.txt");
        try (FileReader osobeReader = new FileReader(osobeFile);
             BufferedReader reader = new BufferedReader(osobeReader))
        {
            String procitanaLinija;
            while ((procitanaLinija = reader.readLine()) != null){
                Long id = Long.parseLong(procitanaLinija);
                String ime = reader.readLine();
                String prezime = reader.readLine();
                Integer god = Integer.parseInt(reader.readLine());
                int odabirZup = Integer.parseInt(reader.readLine());
                int odabirBiliV = Integer.parseInt(reader.readLine());

                String[] delimiter = reader.readLine().split(",");
                Osoba os = new Osoba();
                if (delimiter[0].equals("0")){
                    os = new Osoba.BuilderOsobe(id, ime, prezime)
                            .osStarost(LocalDate.of(god,1,1))
                            .osZupanija(zupanijeLista.get(odabirZup-1))
                            .osZarazenBolescu(bolestiLista.get(odabirBiliV-1))
                            .build();
                }
                else{
                    List<Osoba> kontOsobeLista = new ArrayList<>();
                    for(int i = 0; i < delimiter.length; i++){
                        int xvar = Integer.parseInt(delimiter[i]);
                        kontOsobeLista.add(osobeLista.get(xvar-1));
                    }
                    os = new Osoba.BuilderOsobe(id, ime, prezime)
                            .osStarost(LocalDate.of(god,1,1))
                            .osZupanija(zupanijeLista.get(odabirZup-1))
                            .osZarazenBolescu(bolestiLista.get(odabirBiliV-1))
                            .osKontaktiraneOsobe(kontOsobeLista)
                            .build();
                }
                osobeLista.add(os);
            }
            System.out.println("Učitavanje podataka o osobama...");
            logger.info("Uspješno učitane osobe iz datoteke!");
        }
        catch (IOException iznimka){
            System.out.println("Greška kod čitanja osoba iz datoteke!");
            logger.error("Greška kod čitanja osoba iz datoteke! " + iznimka);
        }
    }

    /**
     * Ispisuje sve osobe koje su unesene u program zajedno sa podacima svake pojedine osobe
     *
     * @param osobeLista lista osoba koje će se ispisati
     */
    private static void ispisOsoba(List<Osoba> osobeLista) {
        System.out.println("\nPopis osoba:");
        for(int i = 0; i < osobeLista.size(); i++){
            System.out.println("Ime i prezime: " + osobeLista.get(i).getIme() + " " + osobeLista.get(i).getPrezime());
            System.out.println("Starost: " + osobeLista.get(i).getStarost());
            System.out.println("Županija prebivališta: " + osobeLista.get(i).getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osobeLista.get(i).getZarazenBolescu().getNaziv());
            System.out.println("Kontaktirane osobe:");
            if(osobeLista.get(i).getKontaktiraneOsobe() == null || osobeLista.get(i).getKontaktiraneOsobe().size() == 0){
                System.out.println("Nema kontaktiranih osoba.");
            }
            else{
                for(int j = 0; j < osobeLista.get(i).getKontaktiraneOsobe().size(); j++){
                    System.out.println(osobeLista.get(i).getKontaktiraneOsobe().get(j).getIme() + " " + osobeLista.get(i).getKontaktiraneOsobe().get(j).getPrezime());
                }
            }
            System.out.println();
        }
    }

    /**
     * Služi za unos bolesti ili virusa u mapu kao ključa i pridruživanje osoba koje boluju od određene bolesti/virusa
     * kao vrijednosti.
     *
     * @param bolestiLista lista bolesti potreban za pridruživanje bolesti u mapu kao ključa
     * @param osobeLista lista osoba potrebna za evidenciju osoba koje boluju od određene bolesti
     * @param bolestOsobeMapa mapa u koju se sprema bolest kao ključ i lista osoba kao vrijednost
     */
    private static void unosBolestiOsobaUMapu(List<Bolest> bolestiLista, List<Osoba> osobeLista, Map<Bolest, List<Osoba>> bolestOsobeMapa) {
        for(Bolest bol : bolestiLista){
            List<Osoba> pomocnaLista = new ArrayList<>();
            boolean xvar = false;
            for(int i = 0; i < osobeLista.size(); i++){
                if(bol.getNaziv() == osobeLista.get(i).getZarazenBolescu().getNaziv()){
                    pomocnaLista.add(osobeLista.get(i));
                    xvar = true;
                }
            }
            if(xvar == true){
                bolestOsobeMapa.put(bol, pomocnaLista);
            }
        }
    }

    /**
     * Ispisuje bolesti ili viruse i listu osoba koje boluju od određene bolesti/virusa.
     *
     * @param bolestOsobeMapa mapa bolesti i osoba koje boluju od određene bolesti
     */
    private static void ispisBolestiOsobaMapa(Map<Bolest, List<Osoba>> bolestOsobeMapa) {
        for(Map.Entry<Bolest,List<Osoba>> mapa : bolestOsobeMapa.entrySet()){
            if(mapa.getKey() instanceof Virus){
                System.out.print("Od virusa " + mapa.getKey().getNaziv() + " boluju: ");
                logger.info("Od virusa " + mapa.getKey().getNaziv() + " boluju: ");
            }
            else{
                System.out.print("Od bolesti " + mapa.getKey().getNaziv() + " boluju: ");
                logger.info("Od bolesti " + mapa.getKey().getNaziv() + " boluju: ");
            }
            for(int i = 0; i < mapa.getValue().size(); i++){
                System.out.print(mapa.getValue().get(i).getIme() + " " + mapa.getValue().get(i).getPrezime() + ", ");
                logger.info(mapa.getValue().get(i).getIme() + " " + mapa.getValue().get(i).getPrezime() + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Služi za sortiranje unesenih županija po postotku oboljelih stanovnika.
     *
     * @param zupanijeLista lista nesortiranih zupanija
     * @param sortiraneZupSet set zupanija koje su sortirane
     */
    private static void unosSortiranihZupanija(List<Zupanija> zupanijeLista, SortedSet<Zupanija> sortiraneZupSet) {
        for(Zupanija zup : zupanijeLista){
            sortiraneZupSet.add(zup);
        }
    }

    /**
     * Stvara kliniku koja sadrži listu virusa koje smo unijeli u aplikaciju i listu osoba zaraženih tim virusima.
     *
     * @param bolestiLista lista bolesti potrebnih za pronalazak virusa
     * @param osobeLista lista osoba potrebnih za pronalazak osoba koje su zaražene nekim virusom
     * @return instancu klase <code>KlinikaZaInfektivneBolesti</code> sa listom virusa i osoba zaraženih virusom
     */
    //    @org.jetbrains.annotations.NotNull
    private static KlinikaZaInfektivneBolesti<Virus, Osoba> stvaranjeKlinikeZaInfektivneBolesti(List<Bolest> bolestiLista, List<Osoba> osobeLista) {
        List<Bolest> listaVirusa = bolestiLista.stream()
                .filter(bol -> bol instanceof Virus)
                .collect(Collectors.toList());
        KlinikaZaInfektivneBolesti<Virus,Osoba> klinika = new KlinikaZaInfektivneBolesti<>();
        for(Bolest vir : listaVirusa){
            klinika.dodajVirus((Virus) vir);
        }
        for(Osoba os : osobeLista){
            if(os.getZarazenBolescu() instanceof Virus){
                klinika.dodajOsobu(os);
            }
        }
        return klinika;
    }

    /**
     * Mjeri vrijeme potrebno za sortiranje virusa koristeći lambda izraz.
     *
     * @param klinika sadrži listu virusa koje sortiramo
     * @return vrijeme potrebno za sortiranje
     */
    private static Duration sortiranjeLambda(KlinikaZaInfektivneBolesti<Virus, Osoba> klinika) {
        Instant start = Instant.now();
        klinika.sortiranjeVirusa();
        Instant end = Instant.now();
        Duration time = Duration.between(start,end);
        return time;
    }

    /**
     * Mjeri vrijeme potrebno za sortiranje virusa koristeći običan <code>Collection</code> sort.
     *
     * @param klinikaPomoc sadrži listu virusa koje sortiramo
     * @return vrijeme potrebno za sortiranje
     */
    private static Duration sortiranjeBezLambde(KlinikaZaInfektivneBolesti<Virus, Osoba> klinikaPomoc) {
        Instant pocetak = Instant.now();
        klinikaPomoc.sortiranjeVirusaBezLambde();
        Instant kraj = Instant.now();
        Duration vrijeme = Duration.between(pocetak,kraj);
        return vrijeme;
    }

    /**
     * Pretražuje da li prezime neke od osoba u listi osoba sadrži dio stringa koji unosimo. U slučaju da odgovara, ispisuje
     * sve osobe koje u prezimenu imaju taj string. U slučaju da ne odgovara, ispisuje kako ne postoji osoba sa tim stringom u prezimenu.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param osobeLista lista osoba potrebinh za pretragu
     */
    private static void pretragaStringa(Scanner unos, List<Osoba> osobeLista) {
        System.out.print("\nUnesi string za pretragu po prezimenu: ");
        String filter = unos.nextLine();
        List<Osoba> osobeFilterLista = osobeLista.stream()
                .filter(os -> os.getPrezime().toUpperCase().contains(filter.toUpperCase()))
                .collect(Collectors.toList());
        Optional<Osoba> postojiLi = osobeFilterLista.stream().findFirst();
        if(postojiLi.isPresent()){
            System.out.println("Osobe čije prezime sadrži '" + filter + "' su sljedeće:");
            osobeFilterLista.stream().forEach(System.out::println);
            System.out.println();
        }
        else{
            System.out.println("Ne postoji osoba sa takvim string-om u prezimenu!!\n");
        }
    }

    /**
     * Ispisuje koliko svaka bolest/virus ima simptoma.
     *
     * @param bolestiLista lista svih unesenih bolesti/virusa
     */
    private static void mapiranjeBrojaSimptoma(List<Bolest> bolestiLista) {
        bolestiLista.stream()
                .map(bol -> bol.getNaziv() + " ima " + bol.getSimptomi().size() + " simptoma.")
                .forEach(System.out::println);
    }

    /**
     * Služi za zapisivanje županija koje imaju postotak zaraženosti veći od 2% u binarnu datoteku.
     *
     * @param zupanijeLista lista županija potrebnih za pronalazak županije koja će se zapisati u datoteku
     */
    private static void serijalizacija(List<Zupanija> zupanijeLista) {
        try (ObjectOutputStream serializator = new ObjectOutputStream(
                new FileOutputStream("dat/serializatorZupanija.dat")))
        {
            List<Zupanija> pomocLista = new ArrayList<>();
            for (Zupanija zup : zupanijeLista){
                if (((double)zup.getBrojZarazenih() / zup.getBrojStanovnika()) * 100 > 2.0){
                    pomocLista.add(zup);
                }
            }
            serializator.writeObject(pomocLista);
        }
        catch (IOException iznimka){
            System.out.println("Greška kod pisanja u binarnu datoteku!");
            logger.error("Greška kod pisanja u binarnu datoteku! " + iznimka);
        }
    }

    /**
     * Služi za čitanje županija iz binarne datoteke.
     *
     */
    private static void deserijalizacija() {
        try (ObjectInputStream serializator = new ObjectInputStream(
                new FileInputStream("dat/serializatorZupanija.dat")))
        {
            System.out.println();
            List<Zupanija> procitanaZupanija = (List<Zupanija>) serializator.readObject();
            Optional<Zupanija> postojiLi = procitanaZupanija.stream().findFirst();
            if(postojiLi.isPresent()){
                System.out.println("Županije sa postotkom zaraženosti većim od 2% su:");
                for (Zupanija zup : procitanaZupanija){
                    System.out.println(zup.getId() + " " + zup.getNaziv() + " " + zup.getBrojStanovnika()
                            + " " + zup.getBrojZarazenih());
                }
            }
            else{
                System.out.println("Nema županija sa postotkom zaraženosti većim od 2%!");
            }

        }
        catch (IOException | ClassNotFoundException iznimka){
            System.out.println("Greška kod čitanja iz binarne datoteke!");
            logger.error("Greška kod čitanja iz binarne datoteke! " + iznimka);
        }
    }
}
