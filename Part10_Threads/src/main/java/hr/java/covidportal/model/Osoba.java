package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * Služi za kreiranje instanci osobe koje su potrebne za ispravo funkcioniranje programa.
 */
public class Osoba implements Serializable {
    private Long id;
    private String ime;
    private String prezime;
    private LocalDate starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe;

    /**
     * Služi za pomoć kreiranja osobe korištenjem "Builder Patterna"
     */
    public Osoba() {

    }

    /**
     * Služi za lakše kreiranje osobe. Omogućava nam unos osobe bez da unosimo sva svojstva klase <code>Osoba</code>. U
     * slučaju da neko od svojstva ne unesemo, ne dolazi do greške. Također, u slučaju da za neko od svojstva nemamo
     * odgovarajuću vrijednost, ne moramo unositi null vrijednost (npr. ako nema kontaktiranih osoba
     * nećemo unositi null vrijednost nego ćemo unijeti osobu bez svojstva kontaktiraneOsobe).
     */
    public static class BuilderOsobe{
        private Long id;
        private String ime;
        private String prezime;
        private LocalDate starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe;

        public BuilderOsobe(Long id, String ime, String prezime){
            this.id = id;
            this.ime = ime;
            this.prezime = prezime;
        }

        public BuilderOsobe osStarost(LocalDate starost){
            this.starost = starost;
            return this;
        }

        public BuilderOsobe osZupanija(Zupanija zupanija){
            this.zupanija = zupanija;
            return this;
        }

        public BuilderOsobe osZarazenBolescu(Bolest zarazenBolescu){
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        public BuilderOsobe osKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe){
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            if(this.zarazenBolescu instanceof Virus){
                for(Osoba os : kontaktiraneOsobe){
                    os.setZarazenBolescu(this.zarazenBolescu);
                }
            }
            return this;
        }

        public Osoba build(){
            Osoba osoba = new Osoba();
            osoba.id = this.id;
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.starost = this.starost;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;
            return osoba;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Integer getStarost() {
        LocalDate rodendan = this.starost;
        LocalDate sad = LocalDate.now();
        Integer age = Math.toIntExact(ChronoUnit.YEARS.between(rodendan, sad));
        return age;
    }

    public LocalDate getStarostDatum() {
        return starost;
    }

    public void setStarost(LocalDate starost) {
        this.starost = starost;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Osoba osoba = (Osoba) o;
        return Objects.equals(getIme(), osoba.getIme()) &&
                Objects.equals(getPrezime(), osoba.getPrezime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIme(), getPrezime());
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }
}
