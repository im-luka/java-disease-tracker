package hr.java.covidportal.genericsi;

import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Virus;
import hr.java.covidportal.sort.VirusiKlinikaSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Služi za kreiranje instanci klinike koje su potrebne za ispravo funkcioniranje programa.
 *
 * @param <T> instanca može biti virus
 * @param <S> instanca može biti osoba
 */
public class KlinikaZaInfektivneBolesti<T extends Virus, S extends Osoba> {
    private List<T> listaVirusa;
    private List<S> listaOsoba;

    /**
     * Inicijalizira kapacitet klinike na 10 po defaultu
     */
    public KlinikaZaInfektivneBolesti(){
        this(10);
    }

    /**
     * Inicijalizira listu virusa/osoba ovisno o potrebnom kapacitetu. U slučaju da je kapacitet manji od 0, inicijalizira listu na
     * veličinu od 10.
     *
     * @param kapacitetKlinike veličina liste
     */
    public KlinikaZaInfektivneBolesti(int kapacitetKlinike) {
        if(kapacitetKlinike <= 0){
            listaVirusa = new ArrayList<T>(10);
            listaOsoba = new ArrayList<S>(10);
        }
        else {
            listaVirusa = new ArrayList<T>(kapacitetKlinike);
            listaOsoba = new ArrayList<S>(kapacitetKlinike);
        }
    }

    /**
     * Inicijalizira listu virusa/osoba unesenom listom virusa/osoba.
     *
     * @param listaVirusa lista virusa potrebna za inicijalizaciju
     * @param listaOsoba lista osoba potrebna za inicijalizaciju
     */
    public KlinikaZaInfektivneBolesti(List<T> listaVirusa, List<S> listaOsoba) {
        this.listaVirusa = listaVirusa;
        this.listaOsoba = listaOsoba;
    }

    /**
     * Dodaje virus u listu virusa.
     *
     * @param instanca instanca virusa koja se dodaje u listu
     */
    public void dodajVirus(T instanca){
        listaVirusa.add(instanca);
    }

    /**
     * Dodaje osobu u listu osoba.
     *
     * @param instanca instanca osoba koja se dodaje u listu
     */
    public void dodajOsobu(S instanca){
        listaOsoba.add(instanca);
    }

    /**
     * Ispisuje sve viruse unutar klinike.
     */
    public void ispisVirusa(){
        AtomicInteger brojac = new AtomicInteger(1);
        listaVirusa.stream()
                .forEach(vir -> {System.out.println(brojac + ". " + vir.getNaziv()); brojac.addAndGet(1);});
    }

    /**
     * Ispisuje sve osobe unutar klinike.
     */
    public void ispisOsoba(){
        listaOsoba.stream()
                .forEach(os -> System.out.println(os.getIme() + " " + os.getPrezime() + ", "));
    }

    /**
     * Sortira viruse po nazivu obrnutom od abecednog koristeći lambda izraz.
     */
    public void sortiranjeVirusa(){
        listaVirusa = listaVirusa.stream()
                .sorted(new VirusiKlinikaSorter())
                .collect(Collectors.toList());
    }

    /**
     * Sortira viruse po nazivu obrnutom od abecednog koristeći običan <code>Collection</code> sort.
     */
    public void sortiranjeVirusaBezLambde(){
        listaVirusa.sort(new VirusiKlinikaSorter());
    }

    public List<T> getListaVirusa() {
        return listaVirusa;
    }

    public void setListaVirusa(List<T> listaVirusa) {
        this.listaVirusa = listaVirusa;
    }

    public List<S> getListaOsoba() {
        return listaOsoba;
    }

    public void setListaOsoba(List<S> listaOsoba) {
        this.listaOsoba = listaOsoba;
    }
}
