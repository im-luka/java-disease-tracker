package hr.java.covidportal.main;

import hr.java.covidportal.iznimke.BolestIstihSimptoma;
import hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import hr.java.covidportal.iznimke.OgranicenjeOsoba;
import hr.java.covidportal.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Služi za izvršavanje programa za koji su definirane sve klase i metode.
 */
public class Glavna {
    private static final Integer KONSTANTA = 3;
    private static final Integer BR_BOLESTI = 4;
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
        Zupanija[] zupanije = new Zupanija[KONSTANTA];
        Simptom[] simptomi = new Simptom[KONSTANTA];
        Bolest[] bolesti = new Bolest[BR_BOLESTI];
        Osoba[] osobe = new Osoba[KONSTANTA];

        logger.info("Aplikacija je pokrenuta!");

        System.out.println("Unesi podatke o " + KONSTANTA + " županije:");
        unosZupanije(unos, zupanije);

        System.out.println("Unesi podatke o " + KONSTANTA + " simptoma:");
        unosSimptomi(unos, simptomi);

        System.out.println("Unesi podatke o " + BR_BOLESTI + " bolesti ili virusa:");
        unosBolesti(unos, simptomi, bolesti);

        unosOsobe(unos, zupanije, bolesti, osobe);

        System.out.println("\nPopis osoba:");
        ispisOsoba(osobe);

        logger.info("Završetak programa aplikacije!");
        logger.info("------------------------------------------------------------------------------------------------------");
    }

    /**
     * Ispisuje sve osobe koje su unesene u program zajedno sa podacima svake pojedine osobe
     *
     * @param osobe polje osoba koje će se ispisati
     */
    private static void ispisOsoba(Osoba[] osobe) {
        for(int i = 0; i < osobe.length; i++){
            System.out.println("Ime i prezime: " + osobe[i].getIme() + " " + osobe[i].getPrezime());
            System.out.println("Starost: " + osobe[i].getStarost());
            System.out.println("Županija prebivališta: " + osobe[i].getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osobe[i].getZarazenBolescu().getNaziv());
            System.out.println("Kontaktirane osobe:");
            if(osobe[i].getKontaktiraneOsobe() == null || osobe[i].getKontaktiraneOsobe().length == 0){
                System.out.println("Nema kontaktiranih osoba.");
            }
            else{
                for(int j = 0; j < osobe[i].getKontaktiraneOsobe().length; j++){
                    System.out.println(osobe[i].getKontaktiraneOsobe()[j].getIme() + " " + osobe[i].getKontaktiraneOsobe()[j].getPrezime());
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
     * @param zupanije polje županija potrebno za pridruživanje županije osobi koju unosimo
     * @param bolesti polje bolesti potrebno za pridruživanje bolesti osobi koju unosimo
     * @param osobe polje osoba u koje se sprema svaka unesena osoba
     */
    private static void unosOsobe(Scanner unos, Zupanija[] zupanije, Bolest[] bolesti, Osoba[] osobe) {
        for(int i = 0; i < KONSTANTA; i++){
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
                    for(int j = 0; j < zupanije.length; j++){
                        System.out.println((j+1) + ". " + zupanije[j].getNaziv());
                    }
                    System.out.print("Odabir: ");
                    odabirZup = unos.nextInt();
                    while(odabirZup < 1 || odabirZup > zupanije.length){
                        logger.error("Pogreška kod unosa županije " + (i+1) + ". osobe, unesen je veći ili manji broj od ponuđenog: " + odabirZup);
                        System.out.print("Neispravan unos! Odaberi ponovno: ");
                        odabirZup = unos.nextInt();
                    }
                    logger.info("Uspješno unesena županija " + (i+1) + ". osobe: " + zupanije[odabirZup-1].getNaziv());
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
                    for(int j = 0; j < bolesti.length; j++){
                        System.out.println((j+1) + ". " + bolesti[j].getNaziv());
                    }
                    System.out.print("Odabir: ");
                    odabirBol = unos.nextInt();
                    while(odabirBol < 1 || odabirBol > bolesti.length){
                        logger.error("Pogreška kod unosa bolesti " + (i+1) + ". osobe, unesen je veći ili manji broj od ponuđenog: " + odabirBol);
                        System.out.print("Neispravan unos! Odaberi ponovno: ");
                        odabirBol = unos.nextInt();
                    }
                    logger.info("Uspješno unesena bolest " + (i+1) + ". osobe: " + bolesti[odabirBol-1].getNaziv());
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
                osobe[i] = new Osoba.BuilderOsobe(ime, prezime)
                        .osStarost(god)
                        .osZupanija(zupanije[odabirZup-1])
                        .osZarazenBolescu(bolesti[odabirBol-1])
                        .build();
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

                Osoba[] popisPomoc = new Osoba[brOsoba];
                provjera = false;
                int brojac = 0;
                do {
                    try {
                        for(int j = brojac; j < brOsoba; j++){
                            int odabirOs = 0;
                            while(odabirOs < 1 || odabirOs > i){
                                System.out.println("Odaberi " + (j+1) + ". osobu:");
                                for(int k = 0; k < i; k++){
                                    System.out.println((k+1) + ". " + osobe[k].getIme() + " " + osobe[k].getPrezime());
                                }
                                System.out.print("Odabir: ");
                                odabirOs = unos.nextInt();
                                if(odabirOs > 0 && odabirOs <= i){
                                    provjeraKontaktiraneOsobe(odabirOs, osobe, popisPomoc);
                                    logger.info("Uspješno je odabrana " + (j+1) + ". osoba: " + osobe[odabirOs-1].getIme() + " " + osobe[odabirOs-1].getPrezime());
                                    popisPomoc[j] = osobe[odabirOs-1];
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

                osobe[i] = new Osoba.BuilderOsobe(ime, prezime)
                        .osStarost(god)
                        .osZupanija(zupanije[odabirZup-1])
                        .osZarazenBolescu(bolesti[odabirBol-1])
                        .osKontaktiraneOsobe(popisPomoc)
                        .build();
                logger.info("Uspješan unos " + (i+1) + ". osobe!");
            }
        }
    }

    /**
     * Provjerava radi li se o bolesti ili virusu, te se nakon toga unosi naziv i broj simptoma određene bolesti ili virusa.
     * Nakon odabira simptoma, bolest ili virus se pohranjuje u polje bolesti.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param simptomi polje simptoma koje je potrebno da bi bolesti pridružili odgovarajuće simptome
     * @param bolesti polje bolesti u koje se sprema svaka unesena bolest
     */
    private static void unosBolesti(Scanner unos, Simptom[] simptomi, Bolest[] bolesti) {
        for(int i = 0; i < BR_BOLESTI; i++){
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

                Simptom[] listaSimpt = new Simptom[brSimpt];
                provjera = false;
                int brojac = 0;
                try {
                    for(int j = brojac; j < brSimpt; j++){
                        int pomoc = 0;
                        while(pomoc < 1 || pomoc > simptomi.length){
                            System.out.println("Odaberi " + (j+1) + ". simptom:");
                            for(int k = 0; k < simptomi.length; k++){
                                System.out.println((k+1) + ". " + simptomi[k].getNaziv() + " " + simptomi[k].getVrijednost());
                            }
                            System.out.print("Odaberi: ");
                            pomoc = unos.nextInt();
                            if(pomoc > 0 && pomoc <= simptomi.length){
                                boolean xvar = true;
                                if(i != 0){
                                    provjeraSimptoma(bolesti, simptomi, brSimpt, pomoc, xvar, i);
                                }
                                logger.info("Uspješno je odabran " + (j+1) + ". simptom: " + simptomi[pomoc-1].getNaziv());
                                listaSimpt[j] = simptomi[pomoc-1];
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
                        bolesti[i] = new Bolest(ime, listaSimpt);
                        logger.info("Uspješno je unesena " + (i+1) + ". bolest!");
                    }
                    else{
                        bolesti[i] = new Virus(ime, listaSimpt);
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
     * @param simptomi polje simptoma u koje se sprema svaki uneseni simptom
     */
    private static void unosSimptomi(Scanner unos, Simptom[] simptomi) {
        for(int i = 0; i < KONSTANTA; i++){
            System.out.print("Unesi naziv simptoma: ");
            String ime = unos.nextLine();
            logger.info("Unesen je naziv " + (i+1) + ". simptoma: " + ime);
            System.out.print("Unesi vrijednost simptoma (RIJETKO, SREDNJE ILI ČESTO): ");
            String vrij = unos.nextLine();
            logger.info("Unesena je vrijednost " + (i+1) + ". simptoma: " + vrij);
            simptomi[i] = new Simptom(ime, vrij);
            logger.info("Uspješno unesen " + (i+1) + ". simptom!");
        }
    }

    /**
     * Unos naziva i broja stanovnika županije i pohranjivanje iste u polje županija.
     *
     * @param unos objekt klase <code>Scanner</code> koji služi za unos podataka iz konzole
     * @param zupanije polje županija u koje se sprema svaka unesena županija
     */
    private static void unosZupanije(Scanner unos, Zupanija[] zupanije) {
        for(int i = 0; i < KONSTANTA; i++){
            System.out.print("Unesi naziv županije: ");
            String ime = unos.nextLine();
            logger.info("Unesen je naziv " + (i+1) + ". županije: " + ime);
            boolean provjera = false;
            Integer br = 1;
            do {
                try{
                    System.out.print("Unesi broj stanovnika: ");
                    br = unos.nextInt();
                    logger.info("Unesen je broj stanovnika " + (i+1) +". županije: " + br);
                    provjera = true;
                }
                catch(InputMismatchException iznimka1){
                    logger.error("Pogreška kod unosa broja stanovnika " + (i+1) + ". županije, unesen je String umjesto brojčane vrijednosti!", iznimka1);
                    System.out.println("Neispravan unos! Molim unesi brojčanu vrijednost.");
                    unos.nextLine();
                }
            }while(provjera == false);
            zupanije[i] = new Zupanija(ime, br);
            unos.nextLine();
            logger.info("Uspješno unesena " + (i+1) + ". županija!");
        }
    }

    /**
     * Uspoređuje da li se kontaktirane osobe koje trenutno unosimo već nalaze u polju kontaktiranih osoba polja osoba. U slučaju
     * da se nalaze, baca se iznimka <code>DuplikatKontaktiraneOsobe</code>.
     *
     * @param odabirOs varijabla koja nam služi za dohvat potrebne osobe
     * @param osobe polje osoba u koje je spremljena svaka dosad unesena osoba
     * @param popisPomoc pomoćno polje osoba kojim provjeravamo da li se kontaktirane osobe koje trenutno unosimo
     *                   već nalaze u polju kontaktiranih osoba polja osoba
     * @throws DuplikatKontaktiraneOsobe iznimka koja se baca u slučaju da se trenutne kontaktirane osobe koje unosimo već
     *                                   nalaze u polju kontaktiranih osoba polja osoba
     */
    public static void provjeraKontaktiraneOsobe(int odabirOs, Osoba[] osobe, Osoba[] popisPomoc) throws DuplikatKontaktiraneOsobe{
        for(int i = 0; i < popisPomoc.length; i++){
            if(popisPomoc[i] == osobe[odabirOs-1]){
                throw new DuplikatKontaktiraneOsobe("Pogrešan unos! Već si unio ovu osobu.");
            }
        }
    }

    /**
     * Uspoređuje da li bolest koja se trenutno unosi ima isti broj simptoma kao i prethodno unesena bolest. Ako je broj simptoma jednak,
     * provjerava se da li se radi o istom nazivu simptoma. U slučaju da je naziv također jednak, baca se iznimka
     * <code>BolestIstihSimptoma</code> te se bolest ponovno unosi.
     *
     * @param bolesti polje bolesti u koje je spremljena svaka dosad unesena bolest
     * @param simptomi polje simptoma koje služi kao pomoć za usporedbu jednakosti prethodno unesenih simptoma pojedine bolesti
     * @param brSimpt broj simptoma unesenih od strane korisnika
     * @param pomoc odabir jednog od simptoma za određenu bolest
     * @param xvar pomoćna varijabla koja služi kao zastavica za provjeru ispravnosti uvjeta
     * @param i označava trenutnu bolest i pomaže pri usporedbi prethodno unesenih bolesti i jednakosti simptoma
     * @throws BolestIstihSimptoma iznimka koja se baca u slučaju da prethodno unesena bolest ima isti broj i naziv simptoma kao i
     *                             bolest koju pokušavamo unijeti
     */
    public static void provjeraSimptoma(Bolest[] bolesti, Simptom[] simptomi, int brSimpt, int pomoc, boolean xvar, int i) throws BolestIstihSimptoma{
        for(int k = 0; k < i; k++){
            if(brSimpt == bolesti[k].getSimptomi().length){
                if(brSimpt <= 1){
                    for(int l = 0; l < bolesti[k].getSimptomi().length; l++){
                        if(bolesti[k].getSimptomi()[l].getNaziv() == simptomi[pomoc-1].getNaziv()){
                            xvar = false;
                        }
                    }
                }
                else{
                    for(int l = 0; l < bolesti[k].getSimptomi().length; l++){
                        if(bolesti[k].getSimptomi()[l].getNaziv() == simptomi[pomoc-1].getNaziv()){
                            xvar = false;
                        }
                        else{
                            xvar = true;
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
