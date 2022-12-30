package hr.java.covidportal.main;

import hr.java.covidportal.model.*;

import java.util.Scanner;

public class Glavna {
    private static final Integer KONSTANTA = 3;
    private static final Integer BR_BOLESTI = 4;
    private static final String[] BOLEST_VIRUS = new String[]{"BOLEST", "VIRUS"};

    public static void main(String[] args){
        Scanner unos = new Scanner(System.in);
        Zupanija[] zupanije = new Zupanija[KONSTANTA];
        Simptom[] simptomi = new Simptom[KONSTANTA];
        Bolest[] bolesti = new Bolest[BR_BOLESTI];
        Osoba[] osobe = new Osoba[KONSTANTA];

        System.out.println("Unesi podatke o " + KONSTANTA + " županije:");
        unosZupanije(unos, zupanije);

        System.out.println("Unesi podatke o " + KONSTANTA + " simptoma:");
        unosSimptomi(unos, simptomi);

        System.out.println("Unesi podatke o " + BR_BOLESTI + " bolesti ili virusa:");
        unosBolesti(unos, simptomi, bolesti);

        unosOsobe(unos, zupanije, bolesti, osobe);

        System.out.println("\nPopis osoba:");
        ispisOsoba(osobe);
    }

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

    private static void unosOsobe(Scanner unos, Zupanija[] zupanije, Bolest[] bolesti, Osoba[] osobe) {
        for(int i = 0; i < KONSTANTA; i++){
            System.out.println("Unesi podatke " + (i+1) + ". osobe:");
            System.out.print("Unesi ime osobe: ");
            String ime = unos.nextLine();
            System.out.print("Unesi prezime osobe: ");
            String prezime = unos.nextLine();
            System.out.print("Unesi starost osobe: ");
            int god = unos.nextInt();
            System.out.println("Unesi županiju prebivališta osobe:");
            for(int j = 0; j < zupanije.length; j++){
                System.out.println((j+1) + ". " + zupanije[j].getNaziv());
            }
            System.out.print("Odabir: ");
            int odabirZup = unos.nextInt();
            while(odabirZup < 1 || odabirZup > zupanije.length){
                System.out.print("Neispravan unos! Odaberi ponovno: ");
                odabirZup = unos.nextInt();
            }
            System.out.println("Unesi bolest osobe:");
            for(int j = 0; j < bolesti.length; j++){
                System.out.println((j+1) + ". " + bolesti[j].getNaziv());
            }
            System.out.print("Odabir: ");
            int odabirBol = unos.nextInt();
            while(odabirBol < 1 || odabirBol > bolesti.length){
                System.out.print("Neispravan unos! Odaberi ponovno: ");
                odabirBol = unos.nextInt();
            }
            unos.nextLine();
            if(i == 0){
                osobe[i] = new Osoba.BuilderOsobe(ime, prezime)
                        .osStarost(god)
                        .osZupanija(zupanije[odabirZup-1])
                        .osZarazenBolescu(bolesti[odabirBol-1])
                        .build();
            }
            else{
                System.out.print("Unesi broj osoba koje su bile u kontaktu s ovom osobom: ");
                int brOsoba = unos.nextInt();
                while(brOsoba < 0 || brOsoba > i){
                    System.out.print("Neispravan unos! Odaberi ponovno: ");
                    brOsoba = unos.nextInt();
                }
                unos.nextLine();
                Osoba[] popisPomoc = new Osoba[brOsoba];
                for(int j = 0; j < brOsoba; j++){
                    System.out.println("Odaberi " + (j+1) + ". osobu:");
                    for(int k = 0; k < i; k++){
                        System.out.println((k+1) + ". " + osobe[k].getIme() + " " + osobe[k].getPrezime());
                    }
                    System.out.print("Odabir: ");
                    int odabirOs = unos.nextInt();
                    while(odabirOs < 1 || odabirOs > i){
                        System.out.print("Neispravan unos! Odaberi ponovno: ");
                        odabirOs = unos.nextInt();
                    }
                    popisPomoc[j] = osobe[odabirOs-1];
                    unos.nextLine();
                }
                osobe[i] = new Osoba.BuilderOsobe(ime, prezime)
                        .osStarost(god)
                        .osZupanija(zupanije[odabirZup-1])
                        .osZarazenBolescu(bolesti[odabirBol-1])
                        .osKontaktiraneOsobe(popisPomoc)
                        .build();
            }
        }
    }

    private static void unosBolesti(Scanner unos, Simptom[] simptomi, Bolest[] bolesti) {
        for(int i = 0; i < BR_BOLESTI; i++){
            System.out.println("Unosiš li bolest ili virus?");
            int odabir = 1;
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
                    System.out.println("Neispravan unos! Odaberi ponovno: ");
                }
            }while(odabir < 1 || odabir > BOLEST_VIRUS.length);
            unos.nextLine();
            System.out.print("Unesi naziv bolesti ili virusa: ");
            String ime = unos.nextLine();
            System.out.print("Unesi broj simptoma: ");
            int brSimpt = unos.nextInt();
            unos.nextLine();
            Simptom[] listaSimpt = new Simptom[brSimpt];
            for(int j = 0; j < brSimpt; j++){
                int pomoc = 0;
                while(pomoc < 1 || pomoc > simptomi.length){
                    System.out.println("Odaberi " + (j+1) + ". simptom:");
                    for(int k = 0; k < simptomi.length; k++){
                        System.out.println((k+1) + ". " + simptomi[k].getNaziv() + " " + simptomi[k].getVrijednost());
                    }
                    System.out.print("Odaberi: ");
                    pomoc = unos.nextInt();
                    if(pomoc > 0 && pomoc <= simptomi.length){
                        listaSimpt[j] = simptomi[pomoc-1];
                        unos.nextLine();
                        break;
                    }
                    System.out.println("Neispravan unos, pokušaj ponovno!");
                }
            }
            if(odabir == 1){
                bolesti[i] = new Bolest(ime, listaSimpt);
            }
            else{
                bolesti[i] = new Virus(ime, listaSimpt);
            }
        }
    }

    private static void unosSimptomi(Scanner unos, Simptom[] simptomi) {
        for(int i = 0; i < KONSTANTA; i++){
            System.out.print("Unesi naziv simptoma: ");
            String ime = unos.nextLine();
            System.out.print("Unesi vrijednost simptoma (RIJETKO, SREDNJE ILI ČESTO): ");
            String vrij = unos.nextLine();
            simptomi[i] = new Simptom(ime, vrij);
        }
    }

    private static void unosZupanije(Scanner unos, Zupanija[] zupanije) {
        for(int i = 0; i < KONSTANTA; i++){
            System.out.print("Unesi naziv županije: ");
            String ime = unos.nextLine();
            System.out.print("Unesi broj stanovnika: ");
            Integer br = unos.nextInt();
            zupanije[i] = new Zupanija(ime, br);
            unos.nextLine();
        }
    }
}
