package hr.java.covidportal.main;

import hr.java.covidportal.enums.VrijednostiSimptoma;
import hr.java.covidportal.iznimke.BolestIstihSimptoma;
import hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import hr.java.covidportal.iznimke.OgranicenjeOsoba;
import hr.java.covidportal.model.*;
import hr.java.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Služi za izvršavanje programa za koji su definirane sve klase i metode.
 */
public class Glavna {
    private static Integer BR_ZUPANIJA = 1;
    private static Integer BR_SIMPTOMA = 1;
    private static Integer BR_BOLESTI = 1;
    private static Integer BR_VIRUSA = 1;
    private static Integer BR_OSOBA = 1;
    private static final String[] BOLEST_VIRUS = new String[]{"BOLEST", "VIRUS"};
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    /**
     * Služi za pokretanje programa u kojem se traži unos županija, simptoma, bolesti i osoba kako bi lakše mogli voditi
     * evidenciju o broju zaraženih od virusa Covid-19 kao i od drugih bolesti i/ili virusa.
     *
     * @param args argumenti komandne linije (ne koriste se)
     */
    public static void main(String[] args){
        Scanner unos = new Scanner(System.in);
        Set<Zupanija> zupanijeSet = new LinkedHashSet<>();
        Set<Simptom> simptomiSet = new LinkedHashSet<>();
        Set<Bolest> bolestiSet = new LinkedHashSet<>();
        List<Osoba> osobeLista = new ArrayList<>();
        Map<Bolest,List<Osoba>> bolestOsobeMapa = new HashMap<>();
        SortedSet<Zupanija> sortiraneZupSet = new TreeSet<>(new CovidSorter());

        logger.info("Aplikacija je pokrenuta!");

        BR_ZUPANIJA = unosBrojaEntiteta(unos, "županija");
        System.out.println("Unesi podatke o " + BR_ZUPANIJA + " županije:");
        unosZupanije(unos, zupanijeSet);

        BR_SIMPTOMA = unosBrojaEntiteta(unos, "simptoma");
        System.out.println("Unesi podatke o " + BR_SIMPTOMA + " simptoma:");
        unosSimptomi(unos, simptomiSet);

        BR_BOLESTI = unosBrojaEntiteta(unos, "bolesti");
        BR_VIRUSA = unosBrojaEntiteta(unos, "virusa");
        System.out.println("Unesi podatke o " + (BR_BOLESTI+BR_VIRUSA) + " bolesti ili virusa:");
        unosBolesti(unos, simptomiSet, bolestiSet);

        BR_OSOBA = unosBrojaEntiteta(unos, "osoba");
        unosOsobe(unos, zupanijeSet, bolestiSet, osobeLista);

        System.out.println("\nPopis osoba:");
        ispisOsoba(osobeLista);

        unosBolestiOsobaUMapu(bolestiSet, osobeLista, bolestOsobeMapa);

        ispisBolestiOsobaMapa(bolestOsobeMapa);

        unosSortiranihZupanija(zupanijeSet, sortiraneZupSet);

        System.out.println("Najviše zaraženih osoba ima u županiji " + sortiraneZupSet.last().getNaziv() + ": " + ((double)sortiraneZupSet.last().getBrojZarazenih() / sortiraneZupSet.last().getBrojStanovnika() * 100) + "%");
        logger.info("Najviše zaraženih osoba ima u županiji " + sortiraneZupSet.last().getNaziv() + ": " + ((double)sortiraneZupSet.last().getBrojZarazenih() / sortiraneZupSet.last().getBrojStanovnika() * 100) + "%");

        logger.info("Završetak programa aplikacije!");
        logger.info("------------------------------------------------------------------------------------------------------");
    }

    /**
     * Pita korisnika koliko entiteta određenog objekta želi unijeti u program.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param entitet objekt za koji se definira količina koju unosimo
     * @return kolicina entiteta koji se unose u program
     */
    public static Integer unosBrojaEntiteta(Scanner unos, String entitet){
        boolean provjera = false;
        Integer broj = 1;
        do {
            try {
                System.out.print("Unesite broj " + entitet + " koje želite unijeti: ");
                broj = unos.nextInt();
                logger.info("Uspješno unesen broj " + entitet + "!");
                provjera = true;
            }
            catch(InputMismatchException iznimka1){
                System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost.");
                logger.error("Pogreška kod unosa entiteta, unesen je String umjesto brojčane vrijednosti!");
                unos.nextLine();
            }
        }while(provjera == false);
        unos.nextLine();
        return broj;
    }

    /**
     * Služi za unos bolesti ili virusa u mapu kao ključa i pridruživanje osoba koje boluju od određene bolesti/virusa
     * kao vrijednosti.
     *
     * @param bolestiSet set bolesti potreban za pridruživanje bolesti u mapu kao ključa
     * @param osobeLista lista osoba potrebna za evidenciju osoba koje boluju od određene bolesti
     * @param bolestOsobeMapa mapa u koju se sprema bolest kao ključ i lista osoba kao vrijednost
     */
    private static void unosBolestiOsobaUMapu(Set<Bolest> bolestiSet, List<Osoba> osobeLista, Map<Bolest, List<Osoba>> bolestOsobeMapa) {
        for(Bolest bol : bolestiSet){
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
     * @param zupanijeSet set nesortiranih zupanija
     * @param sortiraneZupSet set zupanija koje su sortirane
     */
    private static void unosSortiranihZupanija(Set<Zupanija> zupanijeSet, SortedSet<Zupanija> sortiraneZupSet) {
        for(Zupanija zup : zupanijeSet){
            sortiraneZupSet.add(zup);
        }
    }

    /**
     * Ispisuje sve osobe koje su unesene u program zajedno sa podacima svake pojedine osobe
     *
     * @param osobeLista lista osoba koje će se ispisati
     */
    private static void ispisOsoba(List<Osoba> osobeLista) {
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
     * Unose se podaci osobe (ime, prezime i starost) zajedno sa podacima prebivališta i bolesti osobe. Nakon toga se unosi broj
     * osoba s kojima je osoba bila u kontaktu. U slučaju da je osoba zaražena virusom i u kontaktu je bila s drugom osobom,
     * tada će i ta druga osoba biti zaražena istim virusom. Nakon odabira kontaktiranih osoba, osoba se unosi u polje osoba.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param zupanijeSet set županija potreban za pridruživanje županije osobi koju unosimo
     * @param bolestiSet set bolesti potreban za pridruživanje bolesti osobi koju unosimo
     * @param osobeLista lista osoba u koju se sprema svaka unesena osoba
     */
    private static void unosOsobe(Scanner unos, Set<Zupanija> zupanijeSet, Set<Bolest> bolestiSet, List<Osoba> osobeLista) {
        for(int i = 0; i < BR_OSOBA; i++){
            System.out.println("Unesi podatke " + (i+1) + ". osobe:");
            System.out.print("Unesi ime osobe: ");
            String ime = unos.nextLine();
            logger.info("Uneseno ime " + (i+1) + ". osobe: " + ime);
            System.out.print("Unesi prezime osobe: ");
            String prezime = unos.nextLine();
            logger.info("Uneseno prezime " + (i+1) + ". osobe: " + prezime);

            boolean provjera = false;
            int god = 1;
            do {
                try {
                    System.out.print("Unesi starost osobe: ");
                    god = unos.nextInt();
                    logger.info("Uspješno unesena starost " + (i+1) +  ". osobe: " + god);
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod unosa starosti " + (i+1) + ". osobe, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost.");
                    unos.nextLine();
                }
            }while(provjera == false);

            provjera = false;
            int odabirZup = 1;
            do {
                try {
                    System.out.println("Unesi županiju prebivališta osobe:");
                    int brojacOdabirZupanije = 1;
                    for(Zupanija zup : zupanijeSet){
                        System.out.println(brojacOdabirZupanije + ". " + zup.getNaziv());
                        brojacOdabirZupanije++;
                    }
                    System.out.print("Odabir: ");
                    odabirZup = unos.nextInt();
                    while(odabirZup < 1 || odabirZup > zupanijeSet.size()){
                        logger.error("Pogreška kod unosa županije " + (i+1) + ". osobe, unesen je veći ili manji broj od ponuđenog: " + odabirZup);
                        System.out.print("Neispravan unos! Odaberi ponovno: ");
                        odabirZup = unos.nextInt();
                    }
                    logger.info("Uspješno unesena županija " + (i+1) + ". osobe.");
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod unosa županije " + (i+1) + ". osobe, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost.");
                    unos.nextLine();
                }
            }while(provjera == false);

            provjera = false;
            int odabirBol = 1;
            do {
                try {
                    System.out.println("Unesi bolest osobe:");
                    int brojacOdabirBolesti = 1;
                    for(Bolest bol : bolestiSet){
                        System.out.println(brojacOdabirBolesti + ". " + bol.getNaziv());
                        brojacOdabirBolesti++;
                    }
                    System.out.print("Odabir: ");
                    odabirBol = unos.nextInt();
                    while(odabirBol < 1 || odabirBol > bolestiSet.size()){
                        logger.error("Pogreška kod unosa bolesti " + (i+1) + ". osobe, unesen je veći ili manji broj od ponuđenog: " + odabirBol);
                        System.out.print("Neispravan unos! Odaberi ponovno: ");
                        odabirBol = unos.nextInt();
                    }
                    logger.info("Uspješno unesena bolest " + (i+1) + ". osobe.");
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod unosa bolesti " + (i+1) + ". osobe, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost.");
                    unos.nextLine();
                }
            }while(provjera == false);
            unos.nextLine();

            if(i == 0){
                Osoba os = new Osoba.BuilderOsobe(ime, prezime)
                        .osStarost(god)
                        .osZupanija(odabirZupanijeBuilderPattern(zupanijeSet, odabirZup))
                        .osZarazenBolescu(odabirBolestiBuilderPattern(bolestiSet, odabirBol))
                        .build();
                osobeLista.add(os);
                logger.info("Uspješno je unesena " + (i+1) + ". osoba!");
            }
            else{
                provjera = false;
                int brOsoba = 1;
                do {
                    try {
                        System.out.print("Unesi broj osoba koje su bile u kontaktu s ovom osobom: ");
                        brOsoba = unos.nextInt();
                        provjeraOgranicenja(brOsoba, i);
                        logger.info("Uspješno je unesen broj osoba koje su bile u kontaktu s " + (i+1) + ". osobom: " + brOsoba);
                        provjera = true;
                    }
                    catch(InputMismatchException iznimka1){
                        logger.error("Pogreška kod odabira kontaktiranih osoba " + (i+1) + ". osobe, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                        System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost.");
                        unos.nextLine();
                    }
                    catch(OgranicenjeOsoba iznimka2){
                        logger.error("Pogreška kod odabira kontaktiranih osoba " + (i+1) + ". osobe, unesen je preveliki ili premali broj osoba.", iznimka2);
                        System.out.println(iznimka2.getMessage());
                        unos.nextLine();
                    }
                }while(provjera == false);
                unos.nextLine();

                List<Osoba> popisPomoc = new ArrayList<>();
                provjera = false;
                int brojac = 0;
                do {
                    try {
                        for(int j = brojac; j < brOsoba; j++){
                            int odabirOs = 0;
                            while(odabirOs < 1 || odabirOs > i){
                                System.out.println("Odaberi " + (j+1) + ". osobu:");
                                int brojacOdabirOsoba = 1;
                                for(Osoba os : osobeLista){
                                    System.out.println(brojacOdabirOsoba + ". " + os.getIme() + " " + os.getPrezime());
                                    brojacOdabirOsoba++;
                                }
                                System.out.print("Odabir: ");
                                odabirOs = unos.nextInt();
                                if(odabirOs > 0 && odabirOs <= i){
                                    provjeraKontaktiraneOsobe(odabirOs, osobeLista, popisPomoc);
                                    logger.info("Uspješno je odabrana " + (j+1) + ". osoba: " + osobeLista.get(odabirOs-1).getIme() + " " + osobeLista.get(odabirOs-1).getPrezime());
                                    popisPomoc.add(osobeLista.get(odabirOs-1));
                                    brojac++;
                                    unos.nextLine();
                                    break;
                                }
                                logger.error("Pogreška kod odabira " + (j+1) + ". kontaktirane osobe, odabran je veći ili manji broj od ponuđenog: " + odabirOs);
                                System.out.println("Neispravan unos, možeš odabrati samo " + brOsoba + " osobu!");
                            }
                        }
                        logger.info("Uspješan unos kontaktiranih osoba " + (i+1) + ". osobe!");
                        provjera = true;
                    }
                    catch(InputMismatchException iznimka1){
                        logger.error("Pogreška kod unosa kontaktiranih osoba " + (i+1) + ". osobe, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                        System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost.");
                        unos.nextLine();
                    }
                    catch(DuplikatKontaktiraneOsobe iznimka2) {
                        logger.error("Pogreška kod unosa kontaktiranih osoba " + (i+1) + ". osobe, već je unesena odabrana osoba!", iznimka2);
                        System.out.println(iznimka2.getMessage());
                        unos.nextLine();
                    }
                }while(provjera == false);

                Osoba os = new Osoba.BuilderOsobe(ime, prezime)
                        .osStarost(god)
                        .osZupanija(odabirZupanijeBuilderPattern(zupanijeSet, odabirZup))
                        .osZarazenBolescu(odabirBolestiBuilderPattern(bolestiSet, odabirBol))
                        .osKontaktiraneOsobe(popisPomoc)
                        .build();
                osobeLista.add(os);
                logger.info("Uspješan unos " + (i+1) + ". osobe!");
            }
        }
    }

    /**
     * Služi za pronalazak određene županije u setu županija i pridruživanje iste osobi koja se unosi.
     *
     * @param zupanijeSet set županija kroz koje se iterira kako bi se pronašla odgovarajuća županija
     * @param odabirZup indeks određene županije koja se traži u setu županija
     * @return određena županija koja se treba pridružiti osobi koja se unosi
     */
    public static Zupanija odabirZupanijeBuilderPattern(Set<Zupanija> zupanijeSet, int odabirZup){
        Zupanija zupRadiReturna = new Zupanija("bla", 1, 1);
        int brojac = 0;
        for(Zupanija zup : zupanijeSet){
            if(brojac == odabirZup-1){
                return zup;
            }
            brojac++;
        }
        return zupRadiReturna;
    }

    /**
     * Služi za pronalazak određene bolesti u setu bolesti i pridruživanje iste osobi koja se unosi.
     *
     * @param bolestiSet set bolesti kroz koje se iterira kako bi se pronašla odgovarajuća bolest
     * @param odabirBol indeks određene bolesti koja se traži u setu bolesti
     * @return određena bolest koja se treba pridružiti osobi koja se unosi
     */
    public static Bolest odabirBolestiBuilderPattern(Set<Bolest> bolestiSet, int odabirBol){
        Set<Simptom> pomocni = new LinkedHashSet<>();
        Bolest bolRadiReturna = new Bolest("bla", pomocni);
        int brojac = 0;
        for(Bolest bol : bolestiSet){
            if(brojac == odabirBol-1){
                return bol;
            }
            brojac++;
        }
        return bolRadiReturna;
    }

    /**
     * Provjerava radi li se o bolesti ili virusu, te se nakon toga unosi naziv i broj simptoma određene bolesti ili virusa.
     * Nakon odabira simptoma, bolest ili virus se pohranjuje u polje bolesti.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param simptomiSet set simptoma koji je potreban da bi bolesti pridružili odgovarajuće simptome
     * @param bolestiSet set bolesti u koji se sprema svaka unesena bolest
     */
    private static void unosBolesti(Scanner unos, Set<Simptom> simptomiSet, Set<Bolest> bolestiSet) {
        for(int i = 0; i < (BR_BOLESTI+BR_VIRUSA); i++){
            System.out.println("Unosiš li bolest ili virus?");
            boolean provjera = false;
            int odabir = 1;
            do {
                try {
                    do{
                        for(int j = 0; j < BOLEST_VIRUS.length; j++){
                            System.out.println((j+1) + ") " + BOLEST_VIRUS[j]);
                        }
                        System.out.print("ODABIR >> ");
                        odabir = unos.nextInt();
                        if(odabir > 0 && odabir <= BOLEST_VIRUS.length){
                            break;
                        }
                        else{
                            logger.error("Pogreška kod odabira " + (i+1) + ". bolesti ili virusa, odabran je veći ili manji broj od ponuđenog: " + odabir);
                            System.out.println("Neispravan unos! Odaberi ponovno: ");
                        }
                    }while(odabir < 1 || odabir > BOLEST_VIRUS.length);
                    logger.info("Uspješno odabrana " + (i+1) + ". bolest ili virus: " + BOLEST_VIRUS[odabir-1]);
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod odabira " + (i+1) + ". bolesti ili virusa, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost: ");
                    unos.nextLine();
                }
            }while(provjera == false);
            unos.nextLine();

            provjera = false;
            do {
                System.out.print("Unesi naziv bolesti ili virusa: ");
                String ime = unos.nextLine();
                logger.info("Unesen je naziv " + (i+1) + ". bolesti ili virusa: " + ime);

                int brSimpt = 1;
                provjera = false;
                do {
                    try {
                        System.out.print("Unesi broj simptoma: ");
                        brSimpt = unos.nextInt();
                        logger.info("Unesen je broj simptoma " + (i+1) + ". bolesti ili virusa: " + brSimpt);
                        provjera = true;
                    }
                    catch(InputMismatchException iznimka1){
                        logger.error("Pogreška kod odabira broja simptoma " + (i+1) + ". bolesti ili virusa, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                        System.out.println("Neispravan unos! Molim odaberi brojčanu vrijednost.");
                        unos.nextLine();
                    }
                }while(provjera == false);
                unos.nextLine();

                Set<Simptom> setPomocSimptom = new LinkedHashSet<>();
                provjera = false;
                int brojac = 0;
                try {
                    for(int j = brojac; j < brSimpt; j++){
                        int pomoc = 0;
                        while(pomoc < 1 || pomoc > simptomiSet.size()){
                            int brojacOdabirSimptoma = 1;
                            System.out.println("Odaberi " + (j+1) + ". simptom:");
                            for(Simptom simpt : simptomiSet){
                                System.out.println(brojacOdabirSimptoma + ". " + simpt.getNaziv() + " " + simpt.getVrijednost());
                                brojacOdabirSimptoma++;
                            }
                            System.out.print("Odaberi: ");
                            pomoc = unos.nextInt();
                            if(pomoc > 0 && pomoc <= simptomiSet.size()){
                                boolean xvar = true;
                                if(i != 0){
                                    provjeraSimptoma(bolestiSet, simptomiSet, brSimpt, pomoc, xvar);
                                }
                                logger.info("Uspješno je odabran " + (j+1) + ". simptom.");
                                int brojacZaPetlju = 0;
                                for(Simptom simpt : simptomiSet){
                                    if(brojacZaPetlju == pomoc-1){
                                        setPomocSimptom.add(simpt);
                                    }
                                    brojacZaPetlju++;
                                }
                                brojac++;
                                unos.nextLine();
                                break;
                            }
                            logger.error("Pogreška kod odabira " + (j+1) + ". simptoma, unesen je veći ili manji broj od ponuđenog: " + pomoc);
                            System.out.println("Neispravan unos, pokušaj ponovno!");
                            unos.nextLine();
                        }
                    }
                    if(odabir == 1){
                        Bolest bolest = new Bolest(ime, setPomocSimptom);
                        bolestiSet.add(bolest);
                        logger.info("Uspješno je unesena " + (i+1) + ". bolest!");
                    }
                    else{
                        Bolest virus = new Virus(ime, setPomocSimptom);
                        bolestiSet.add(virus);
                        logger.info("Uspješno je unesen " + (i+1) + ". virus!");
                    }
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod odabira simptoma " + (i+1) + ". bolesti ili virusa, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim ponovi unos.");
                    unos.nextLine();
                }
                catch(BolestIstihSimptoma iznimka2){
                    logger.error("Pogreška kod odabira simptoma " + (i+1) + ". bolesti ili virusa, već postoji bolest ili virus sa istim simptomom/simptomima!", iznimka2);
                    System.out.println(iznimka2.getMessage());
                    unos.nextLine();
                }
            }while(provjera == false);

        }
    }

    /**
     * Unos naziva i vrijednosti simptoma i pohranjivanje istog u polje simptoma.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param simptomiSet set simptoma u koji se sprema svaki uneseni simptom
     */
    private static void unosSimptomi(Scanner unos, Set<Simptom> simptomiSet) {
        for(int i = 0; i < BR_SIMPTOMA; i++){
            System.out.print("Unesi naziv simptoma: ");
            String ime = unos.nextLine();
            logger.info("Unesen je naziv " + (i+1) + ". simptoma: " + ime);
            String vrij = "abc";
            boolean xvar = false;
            do{
                try{
                    System.out.print("Unesi vrijednost simptoma (RIJETKO, SREDNJE ILI CESTO): ");
                    vrij = unos.nextLine();
                    vrij = vrij.toUpperCase();
                    VrijednostiSimptoma.valueOf(vrij);
                    if(vrij == VrijednostiSimptoma.CESTO.getVrijednost()){
                        vrij = VrijednostiSimptoma.CESTO.getVrijednost();
                    }
                    else if(vrij == VrijednostiSimptoma.SREDNJE.getVrijednost()){
                        vrij = VrijednostiSimptoma.SREDNJE.getVrijednost();
                    }
                    else if(vrij == VrijednostiSimptoma.RIJETKO.getVrijednost()){
                        vrij = VrijednostiSimptoma.RIJETKO.getVrijednost();
                    }
                    xvar = true;
                }
                catch(IllegalArgumentException iznimka1){
                    System.out.println("Pogreška kod unosa vrijednosti simptoma! Pokušaj ponovno.");
                    logger.error("Pogreška kod unosa vrijednosti simptoma!", iznimka1);
                }
            } while(xvar == false);
            logger.info("Unesena je vrijednost " + (i+1) + ". simptoma: " + vrij);
            Simptom simpt = new Simptom(ime, vrij);
            simptomiSet.add(simpt);
            logger.info("Uspješno unesen " + (i+1) + ". simptom!");
        }
    }

    /**
     * Unos naziva i broja stanovnika županije i pohranjivanje iste u polje županija.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param zupanijeSet set županija u koji se sprema svaka unesena županija
     */
    private static void unosZupanije(Scanner unos, Set<Zupanija> zupanijeSet) {
        for(int i = 0; i < BR_ZUPANIJA; i++){
            System.out.print("Unesi naziv županije: ");
            String ime = unos.nextLine();
            logger.info("Unesen je naziv " + (i+1) + ". županije: " + ime);
            boolean provjera = false;
            Integer brStan = 1;
            do {
                try{
                    System.out.print("Unesi broj stanovnika: ");
                    brStan = unos.nextInt();
                    logger.info("Unesen je broj stanovnika " + (i+1) +". županije: " + brStan);
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod unosa broja stanovnika " + (i+1) + ". županije, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim unesi brojčanu vrijednost.");
                    unos.nextLine();
                }
            }while(provjera == false);
            provjera = false;
            Integer brZar = 1;
            do {
                try{
                    System.out.print("Unesi broj zaraženih stanovnika: ");
                    brZar = unos.nextInt();
                    logger.info("Unesen je broj zaraženih stanovnika " + (i+1) + ". županije: " + brZar);
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod unosa broja zaraženih stanovnika " + (i+1) + ". županije, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim unesi brojčanu vrijednost.");
                    unos.nextLine();
                }
            }while(provjera == false);
            Zupanija zup = new Zupanija(ime, brStan, brZar);
            zupanijeSet.add(zup);
            unos.nextLine();
            logger.info("Uspješno unesena " + (i+1) + ". županija!");
        }
    }

    /**
     * Uspoređuje da li se kontaktirane osobe koje trenutno unosimo već nalaze u polju kontaktiranih osoba polja osoba. U slučaju
     * da se nalaze, baca se iznimka <code>DuplikatKontaktiraneOsobe</code>.
     *
     * @param odabirOs varijabla koja nam služi za dohvat potrebne osobe
     * @param osobeLista lista osoba u koje je spremljena svaka dosad unesena osoba
     * @param popisPomoc pomoćna lista osoba kojom se provjerava da li se kontaktirane osobe koje trenutno unosimo
     *                   već nalaze u polju kontaktiranih osoba polja osoba
     * @throws DuplikatKontaktiraneOsobe iznimka koja se baca u slučaju da se trenutne kontaktirane osobe koje unosimo već
     *                                   nalaze u polju kontaktiranih osoba polja osoba
     */
    public static void provjeraKontaktiraneOsobe(int odabirOs, List<Osoba> osobeLista, List<Osoba> popisPomoc) throws DuplikatKontaktiraneOsobe{
        for(int i = 0; i < popisPomoc.size(); i++){
            if(popisPomoc.get(i) == osobeLista.get(odabirOs-1)){
                throw new DuplikatKontaktiraneOsobe("Pogrešan unos! Već si unio ovu osobu.");
            }
        }
    }

    /**
     * Uspoređuje da li bolest koja se trenutno unosi ima isti broj simptoma kao i prethodno unesena bolest. Ako je broj simptoma jednak,
     * provjerava se da li se radi o istom nazivu simptoma. U slučaju da je naziv također jednak, baca se iznimka
     * <code>BolestIstihSimptoma</code> te se bolest ponovno unosi.
     *
     * @param bolestiSet set bolesti u koji je spremljena svaka dosad unesena bolest
     * @param simptomiSet set simptoma koji služi kao pomoć za usporedbu jednakosti prethodno unesenih simptoma pojedine bolesti
     * @param brSimpt broj simptoma unesenih od strane korisnika
     * @param pomoc odabir jednog od simptoma za određenu bolest
     * @param xvar pomoćna varijabla koja služi kao zastavica za provjeru ispravnosti uvjeta
     * @throws BolestIstihSimptoma iznimka koja se baca u slučaju da prethodno unesena bolest ima isti broj i naziv simptoma kao i
     *                             bolest koju pokušavamo unijeti
     */
    public static void provjeraSimptoma(Set<Bolest> bolestiSet, Set<Simptom> simptomiSet, int brSimpt, int pomoc, boolean xvar) throws BolestIstihSimptoma{
        Simptom pomocniSimptom = new Simptom("a", "a");
        Set<Simptom> pomocniSet = new LinkedHashSet<>();
        for(Bolest bol : bolestiSet){
            if(bol.getSimptomi().size() == brSimpt){
                if(brSimpt <= 1){
                    int brojac = 0;
                    for(Simptom simpt : simptomiSet){
                        if(brojac == pomoc-1){
                            pomocniSimptom.setNaziv(simpt.getNaziv());
                            pomocniSimptom.setVrijednost(simpt.getVrijednost());
                        }
                        brojac++;
                    }
                    for(Simptom simpt : bol.getSimptomi()){
                        if(simpt.getNaziv() == pomocniSimptom.getNaziv()){
                            throw new BolestIstihSimptoma("Pogrešan unos! Već si unio bolest ili virus s istim simptomima.");
                        }
                    }
                }
                else{
                    int brojac = 0;
                    for(Simptom simpt : simptomiSet){
                        if(brojac == pomoc-1){
                            pomocniSet.add(simpt);
                        }
                        brojac++;
                    }
                    for(Simptom simpt : bol.getSimptomi()){
                        for(Simptom simpt2 : pomocniSet){
                            if(simpt.getNaziv() == simpt2.getNaziv()){
                                xvar = false;
                            }
                            else{
                                xvar = true;
                            }
                        }
                    }
                }
            }
        }
        if(xvar == false){
            throw new BolestIstihSimptoma("Pogrešan unos! Već si unio bolest ili virus s istim simptomima.");
        }
    }

    /**
     * Provjerava da li je korisnik unio veći broj od broja osoba koje su unesene u program ili manji broj od 0. Ako se radi
     * o jednom od tih slučaja, baca se iznimka <code>OgranicenjeOsoba</code> te se traži ponovni unos broja osoba.
     *
     * @param brOsoba varijabla u koju se pohranjuje korisnikov unos preko konzole o broju kontaktiranih osoba
     * @param i osoba koja se trenutno unosi u program, služi kao uvjet da korisnik ne odabere veći broj osoba od onih koje
     *          su trenutno unesene
     * @throws OgranicenjeOsoba iznimka koja se baca u slučaju da je broj osoba koje korisnik unosi u konzolu manji od 0 ili
     *                          veći od broja osoba koje su trenutno unesene
     */
    public static void provjeraOgranicenja(int brOsoba, int i) throws OgranicenjeOsoba{
        if(brOsoba < 0 || brOsoba > i){
            throw new OgranicenjeOsoba("Unesen je preveliki ili premali broj osoba! Ponovi unos.");
        }
    }
}
